package com.liuxu.mq.consumer;

import com.liuxu.mq.config.RabbitMQConfig;
import com.liuxu.mq.dal.dataobject.MqBusinessFailureMessageDO;
import com.liuxu.mq.message.MQMessage;
import com.liuxu.mq.server.mq.MqBusinessFailureMessageService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 死信队列消费者
 *
 * @date: 2025-11-06
 * @author: liuxu
 */
@Slf4j
@Component
public class DeadLetterConsumer {

    @Autowired
    private MqBusinessFailureMessageService mqBusinessFailureMessageService;

    @RabbitListener(queues = RabbitMQConfig.DEAD_COMMON_QUEUE_NAME)
    public void deadConsumer(MQMessage mqMessage, Message message, Channel channel) throws IOException {
        log.info("死信队列消费者 接收到消息 MQMessage={}", mqMessage);

        // TODO: 死信队列，存入错误表中，跟发送失败不是一张表记住！
        // exchange、routing-keys、queue、reason、time
        List<Map<String, ?>> header = message.getMessageProperties().getHeader("x-death");
        log.info("x-death =============>\n{}", header);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        // 记录失败消息
        mqBusinessFailureMessageService.save(builderEntity(mqMessage, header.get(0)));

        // 确认消费死信消息
        channel.basicAck(deliveryTag, false);

    }

    /**
     * 构建失败消息实体
     */
    private MqBusinessFailureMessageDO builderEntity(MQMessage sendEmailMessage, Map<String, ?> deathMap) {
        MqBusinessFailureMessageDO failureMessageDO = MqBusinessFailureMessageDO.builder()
                .businessId(sendEmailMessage.getBusinessTaskId())
                .businessType(sendEmailMessage.getBusinessType())
                .errorStackTrace(sendEmailMessage.getErrorStackTrace())
                .build();

        Optional.ofNullable(deathMap).ifPresent(map -> {
            // [{reason=rejected, count=1, exchange=retry.email.exchange, time=Sun Nov 09 12:50:22 CST 2025, routing-keys=[retry.email.routing.key], queue=retry.email.queue}]
            failureMessageDO.setReason(map.get("reason").toString());
            LocalDateTime time = ((Date) (map.get("time"))).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            failureMessageDO.setReasonTime(time);
            failureMessageDO.setRoutingKeys(map.get("routing-keys").toString());
            failureMessageDO.setQueue(map.get("queue").toString());
            failureMessageDO.setExchange(map.get("exchange").toString());
            failureMessageDO.setStatus(0);
        });

        return failureMessageDO;
    }

}
