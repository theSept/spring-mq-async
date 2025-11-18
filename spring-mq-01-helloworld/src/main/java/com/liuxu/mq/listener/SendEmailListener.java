package com.liuxu.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.liuxu.mq.config.RabbitMQConfig.EMAIL_QUEUE_NAME;

/**
 * @date: 2025-11-04
 * @author: liuxu
 */
@Slf4j(topic = "email.listener")
@Component
public class SendEmailListener {

    @RabbitListener(queues = EMAIL_QUEUE_NAME) // 监听指定队列
    public void sendEmailListener(String context, Message message, Channel channel) {
        try {
            // deliveryTag:MQ投递消息给消费者时为每条消息分配的一个唯一递增标识（在同一个channel内唯一）
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            // deliveryLong：延迟队列的TTL过期时间
            // Long deliveryLong = message.getMessageProperties().getDelayLong();
            int flag = message.getMessageProperties().getHeader("flag");
            String messageId = message.getMessageProperties().getMessageId();

            log.info("监听到消息 context={} flag={} messageId={}", context, flag, messageId);

            Thread.sleep(2000);

            // 手动ACK，第二个参数表示：false不批量确认
            channel.basicAck(deliveryTag, false);
            // 拒绝消息，第二个参数：false不批量确认，第三个参数：false不重新入队
            // channel.basicNack(deliveryTag,false,false);
            // 拒绝单条消息，第二个参数：false不重新推送进入队列。
            // channel.basicReject(deliveryTag,false);
            log.info("监听到消息，手动ACK完成");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
