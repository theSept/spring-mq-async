package com.liuxu.mq.producer;

import com.liuxu.mq.config.RabbitMQConfig;
import com.liuxu.mq.constant.MQTypeEnum;
import com.liuxu.mq.dal.dataobject.MqSendEmailLogDO;
import com.liuxu.mq.message.SendEmailMessage;
import com.liuxu.mq.server.email.MqSendEmailLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 生产消息
 *
 * @date: 2025-11-06
 * @author: liuxu
 */
@Slf4j
@Component
public class ProducerMessage {

    // private static final Logger log = LoggerFactory.getLogger(ProducerMessage.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MqSendEmailLogService mqSendEmailLogService;


    /**
     * MQ发送邮件动作
     *
     * @param email
     * @param content
     * @return
     */
    public String sendEmail(String email, String content) {
        log.debug("发送邮件：{}，内容：{}", email, content);

        // 保存邮件
        String emailID = saveEmail(email, content);

        // 用在确认消息是否成功推送到交换机的回调中的一个唯一标识。 业务id + 业务类型
        CorrelationData correlationData = new CorrelationData(emailID + "#" + MQTypeEnum.SEND_EMAIL.getBusinessType());

        // 配置消息头，设置业务类型
        SendEmailMessage sendEmailMessage = SendEmailMessage.builder()
                .emailLogId(emailID)
                .build();


        // 推送消息：消费者去发送邮件
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.BUSINESS_EMAIL_EXCHANGE_NAME, // 指定推送的交换机
                RabbitMQConfig.BUSINESS_EMAIL_ROUTING_KEY, // 指定推送交换机的路由key
                sendEmailMessage,
                correlationData // 确认消息是否成功推送交换机的标识
        );


        return emailID;
    }

    /**
     * MQ重试发送邮件，会根据重试次数指数增加延迟推送消息的时间
     *
     * @param sendEmailMessage 发送的邮件记录
     * @param retryCount       重试的次数，重试的第几次
     */
    public void retrySendEmail(SendEmailMessage sendEmailMessage, int retryCount) {
        String emailID = sendEmailMessage.getEmailLogId();
        log.info("第[{}]次重试-发送邮件：emailID={}", retryCount, emailID);

        // 记录重试次数和错误日志
        saveRetry(retryCount, sendEmailMessage);

        // 用在确认消息是否成功推送到交换机的回调中的一个唯一标识
        CorrelationData correlationData = new CorrelationData(emailID + "#" + MQTypeEnum.SEND_EMAIL.getBusinessType());

        // 重新推送消息：消费者处理业务失败时，去发送邮件
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RETRY_EMAIL_EXCHANGE_NAME,
                RabbitMQConfig.RETRY_EMAIL_ROUTING_KEY,
                sendEmailMessage,
                message -> {
                    message.getMessageProperties().setHeader("x-retry-count", retryCount); // 记录第几次重试
                    // 指数计算延迟推送时间
                    double delay = 1000 * Math.pow(3, retryCount);
                    message.getMessageProperties().setHeader("x-delay", delay); // 设置延迟时间
                    return message;
                },
                correlationData
        );
    }


    /**
     * 记录邮件
     *
     * @param email
     * @param context
     * @return 邮件的唯一值
     */
    private String saveEmail(String email, String context) {
        MqSendEmailLogDO mqSendEmailLogDO = MqSendEmailLogDO.builder()
                .emailTheme("系统提示：xxxxxxxx")
                .content(context)
                .createdDate(LocalDateTime.now())
                .sendEmailAccount("admin.prd@dpa.com")
                .receive(email)
                .status(0)
                .build();
        return mqSendEmailLogService.saveEmail(mqSendEmailLogDO);
    }


    /**
     * 记录重试次数
     *
     * @param retryCount
     * @param sendEmailMessage
     */
    private void saveRetry(int retryCount, SendEmailMessage sendEmailMessage) {
        mqSendEmailLogService.updateEmail(
                MqSendEmailLogDO.builder()
                        .id(sendEmailMessage.getEmailLogId())
                        .retryCount(retryCount)
                        .status(2) // 失败
                        .errorMessage(sendEmailMessage.getErrorStackTrace())
                        .errorTime(LocalDateTime.now())
                        .build()
        );
    }


}
