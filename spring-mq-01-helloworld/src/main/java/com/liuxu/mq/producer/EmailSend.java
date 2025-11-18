package com.liuxu.mq.producer;

import com.liuxu.mq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @date: 2025-11-04
 * @author: liuxu
 */
@Slf4j
@Component
public class EmailSend {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String sendMail(String email, Object context) {
        log.info("发送邮件 MQ email={}, 消息context={}", email, context);
        String id = saveEmail(email, context);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE_NAME,
                RabbitMQConfig.EMAIL_ROUTING_KEY, id,
                (msg) -> { // 消息后处理器
                    // 设置消息特定的属性
                    msg.getMessageProperties().setMessageId("123456789Lx");
                    msg.getMessageProperties().setHeader("flag", 1); // 标记第一次处理
                    return msg;
                }
        );

        log.info("发送邮件 MQ 发送完成");
        return id;
    }

    private String saveEmail(String email, Object context) {
        log.info("邮件记录数据库...");
        return UUID.randomUUID().toString();
    }


}
