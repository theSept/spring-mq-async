package com.liuxu.mq.server.mq;

import com.liuxu.mq.constant.MQTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * RabbitMq推送确认回调
 *
 * @date: 2025-11-08
 * @author: liuxu
 */
@Service
@Slf4j
public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback {

    // 推送确认回到出现错误处理机能
    @Autowired
    MqSendConfirmErrorService mqSendConfirmErrorService;

    /**
     * application.yaml 中配置：
     * spring.rabbitmq.publisher-confirm-type=correlated # 消息推送成功后异步回调
     * <p>
     * 推送消息的时候传递 CorrelationData 参数
     *
     * @param correlationData 消息的关联对象，可以在发送时指定一个唯一ID
     * @param ack             消息是否成功到达交换机
     * @param cause           如果失败，失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String flag = Optional.ofNullable(correlationData).map(CorrelationData::getId).orElse("null");

        if (ack) {
            log.info("交换机-消息确认成功，flag: {}, 已到达交换机", flag);
            // 修改数据库状态，邮件消息推送成功
        } else {

            String[] split = flag.split("#");
            String id = split[0];
            String type = split.length == 2 ? split[1] : null;

            log.error("交换机-消息确认失败，ID: {}, 原因: {}", id, cause);

            // 修改数据库状态，消息未成功推送
            Optional.ofNullable(MQTypeEnum.getMQTypeEnum(type)).ifPresent(mqTypeEnum -> {
                // 保存失败信息
                mqSendConfirmErrorService.saveConfirmError(mqTypeEnum, id, cause);
            });


        }
    }
}
