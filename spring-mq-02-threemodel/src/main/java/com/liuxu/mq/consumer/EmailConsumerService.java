package com.liuxu.mq.consumer;

import com.liuxu.mq.config.RabbitMQConfig;
import com.liuxu.mq.dal.dataobject.MqSendEmailLogDO;
import com.liuxu.mq.message.SendEmailMessage;
import com.liuxu.mq.producer.ProducerMessage;
import com.liuxu.mq.server.email.MqSendEmailLogService;
import com.liuxu.mq.utils.ExceptionUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 消费者服务
 *
 * @date: 2025-11-06
 * @author: liuxu
 */
@Slf4j
@Component
public class EmailConsumerService {

    @Autowired
    private ProducerMessage producerMessage;

    @Autowired
    private MqSendEmailLogService mqSendEmailLogService;

    // 消费者-发送邮件
    @RabbitListener(queues = {RabbitMQConfig.BUSINESS_EMAIL_QUEUE_NAME})
    public void consumerSendEmail(SendEmailMessage sendEmailMessage, Message message, Channel channel) throws IOException {
        // 消息的唯一值
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        String emailID = sendEmailMessage.getEmailLogId();


        try {
            log.info("consumerSendEmail 接收到消息 id={},emailID={}", deliveryTag, emailID);

            // 发送邮件
            sendEmail(emailID);

            // 模拟异常
            int i = 1 / 0;


            // 确认消息, 参数二：false不批量确认
            channel.basicAck(deliveryTag, false);
            log.info("consumerSendEmail 确认消息完成 id={},emailID={}", deliveryTag, emailID);
        } catch (InterruptedException | RuntimeException e) {
            log.info("consumerSendEmail 处理邮件出现异常，进入重试 id={},emailID={}", deliveryTag, emailID);
            sendEmailMessage.setStackTrace(ExceptionUtil.printStackTraceToString(e));
            // 处理邮件出现异常，重试
            producerMessage.retrySendEmail(sendEmailMessage, 1);
            // 解决：NACK会进入，不过这里没配置死了信 没事。。。
            // 拒绝消息，第二个参数：false不批量确认，第三个参数：false不重新入队
            channel.basicNack(deliveryTag, false, false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // 重试-消费邮件
    @RabbitListener(queues = RabbitMQConfig.RETRY_EMAIL_QUEUE_NAME)
    public void retryConsumerSendEmail(SendEmailMessage sendEmailMessage, Message message, Channel channel) throws IOException {
        String emailID = sendEmailMessage.getEmailLogId();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        log.info("retryConsumerSendEmail 重试接收到消息 id={},emailID={}", deliveryTag, emailID);
        try {

            // 模拟异常
            // int i = 1 / 0;

            // 发送邮件
            sendEmail(emailID);

            // ACK，不批量确认
            channel.basicAck(deliveryTag, false);
            log.info("retryConsumerSendEmail 确认消息完成 id={},emailID={}", deliveryTag, emailID);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException | RuntimeException e) {
            int retryCount = Optional.ofNullable(
                    (Integer) message.getMessageProperties().getHeader("x-retry-count")).orElse(0);
            log.info("retryConsumerSendEmail 第[{}]次重试处理邮件出现异常，id={},emailID={}", retryCount, deliveryTag, emailID);

            if (++retryCount > 3) {
                log.info("重试次数超过3次，放弃重试, id={}, emailID={} ", deliveryTag, emailID);
                // 拒绝单条消息-会自动进入死信队列，第二个参数：false不重新推送进入队列。
                channel.basicReject(deliveryTag, false);
                return;
            }

            // 重新发送消息，记录错误日志
            producerMessage.retrySendEmail(sendEmailMessage, retryCount);
            // TODO 重试队列前面几次重试怎么处理？NACK不就进入死性了吗？直接确认掉？
            // 解决：不使用NACK，告诉MQ消息已经处理，如果NACK的话会直接接入死信交换机
            channel.basicAck(deliveryTag, false);
        }

    }


    // 模拟业务，发送邮件操作
    private void sendEmail(String emailId) throws InterruptedException {
        Thread.sleep(2000);
        // 发送成功

        // 更新邮件状态
        mqSendEmailLogService.updateEmail(
                MqSendEmailLogDO.builder()
                        .id(emailId)
                        .status(1)
                        .lastModifiedDate(LocalDateTime.now())
                        .build()
        );

    }


}
