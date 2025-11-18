package com.liuxu.mq.config;

import com.liuxu.mq.server.mq.RabbitConfirmCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

/**
 * @date: 2025-11-06
 * @author: liuxu
 */
@ConditionalOnClass(RabbitTemplate.class)
@Configuration
@Slf4j
public class RabbitMQConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, RabbitConfirmCallback rabbitConfirmCallback) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 后处理器：发送消息前
        rabbitTemplate.setBeforePublishPostProcessors(message -> {
            // 设置消息持久化
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);

            return message;
        });

        // 设置消息转换器
        rabbitTemplate.setMessageConverter(messageConverter());

        // 配置消息推送确认回调：确认消息是否正常推送的交换机
        rabbitTemplate.setConfirmCallback(rabbitConfirmCallback);

        return rabbitTemplate;
    }

    // 业务交换机
    public static final String BUSINESS_EMAIL_EXCHANGE_NAME = "business.email.exchange";
    // 重试交换机
    public static final String RETRY_EMAIL_EXCHANGE_NAME = "retry.email.exchange";
    // 死信交换机
    public static final String DEAD_EXCHANGE_NAME = "dead.common.exchange";


    // 业务-邮件-队列
    public static final String BUSINESS_EMAIL_QUEUE_NAME = "business.email.queue";
    // 重试-邮件队列
    public static final String RETRY_EMAIL_QUEUE_NAME = "retry.email.queue";
    // 死信-公共队列
    public static final String DEAD_COMMON_QUEUE_NAME = "dead.common.queue";


    // 业务-邮件-路由key
    public static final String BUSINESS_EMAIL_ROUTING_KEY = "business.email.routing.key";
    // 重试-邮件-路由key
    public static final String RETRY_EMAIL_ROUTING_KEY = "retry.email.routing.key";
    // 死信-公共-路由key
    public static final String DEAD_COMMON_ROUTING_KEY = "dead.common.routing.key";

    // ===============================交换机===============================
    // ===============================交换机===============================

    // 业务交换机
    @Bean
    public DirectExchange businessExchange() {
        // 持久化，不自动删除
        return new DirectExchange(BUSINESS_EMAIL_EXCHANGE_NAME, true, false);
    }

    @Bean // 重试交换机
    public CustomExchange retryExchange() {
        return new CustomExchange(
                RETRY_EMAIL_EXCHANGE_NAME, // 交换机名称
                "x-delayed-message", // 延迟消息类型
                true, // 持久化
                false, // 不自动删除
                Map.of("x-delayed-type", "direct") // 设置交换机类型为 direct
        );
    }

    @Bean // 死信交换机
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE_NAME, true, false);
    }

    // ===============================队列===============================
    // ===============================队列===============================
    // ===============================队列===============================

    // 业务-邮件-队列
    @Bean
    public Queue businessEmailQueue() {
        return QueueBuilder.durable(BUSINESS_EMAIL_QUEUE_NAME)
                .build();
    }

    // 重试-邮件-队列
    @Bean
    public Queue retryEmailQueue() {
        return QueueBuilder.durable(RETRY_EMAIL_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DEAD_EXCHANGE_NAME) // 指定死信交换机
                .withArgument("x-dead-letter-routing-key", DEAD_COMMON_ROUTING_KEY) // 死信路由key
                .build();
    }

    // 死信-邮件-队列
    @Bean
    public Queue deadEmailQueue() {
        return QueueBuilder.durable(DEAD_COMMON_QUEUE_NAME)
                .build();
    }

    // ===============================banding===============================
    // ===============================banding===============================
    // ===============================banding===============================
    //  业务邮件队列-交换机
    @Bean
    public Binding businessEmailBinding() {
        return BindingBuilder.bind(businessEmailQueue()).to(businessExchange()).with(BUSINESS_EMAIL_ROUTING_KEY);
    }

    //  重试邮件队列-交换机
    @Bean
    public Binding retryEmailBinding() {
        return BindingBuilder.bind(retryEmailQueue()).to(retryExchange()).with(RETRY_EMAIL_ROUTING_KEY).noargs();
    }

    //  死信邮件队列-交换机
    @Bean
    public Binding deadEmailQueueBinding() {
        return BindingBuilder.bind(deadEmailQueue()).to(deadExchange()).with(DEAD_COMMON_ROUTING_KEY);
    }


    //===============================调用下游系统的MQ配置===================
    // 业务-PO-队列
    public static final String BUSINESS_CREATE_PO_QUEUE_NAME = "business.create.po.queue";
    // 重试-PO队列
    public static final String RETRY_CREATE_PO_QUEUE_NAME = "retry.create.po.queue";

    // 业务-po-key
    public static final String BUSINESS_CREATE_PO_ROUTING_KEY = "business.create.po.routing.key";
    // 重试-po-key
    public static final String RETRY_CREATE_PO_ROUTING_KEY = "retry.create.po.routing.key";

    // 业务-交换机
    public static final String BUSINESS_CREATE_PO_EXCHANGE_NAME = "business.create.po.exchange";
    // 重试-交换机
    public static final String RETRY_CREATE_PO_EXCHANGE_NAME = "retry.create.po.exchange";


    // 业务-创建po-交换机
    @Bean
    public DirectExchange businessCreatePoExchange() {
        // 持久化，不自动删除
        return new DirectExchange(BUSINESS_CREATE_PO_EXCHANGE_NAME, true, false);
    }

    // 业务-创建PO-队列
    @Bean
    public Queue businessCreatePoQueue() {
        return QueueBuilder.durable(BUSINESS_CREATE_PO_QUEUE_NAME)
                .build();
    }

    // 重试-创建po-交换机
    @Bean // 重试交换机
    public CustomExchange retryCreatePoExchange() {
        return new CustomExchange(
                RETRY_CREATE_PO_EXCHANGE_NAME, // 交换机名称
                "x-delayed-message", // 延迟消息类型
                true, // 持久化
                false, // 不自动删除
                Map.of("x-delayed-type", "direct") // 设置交换机类型为 direct
        );
    }

    // 重试-创建PO-队列
    @Bean
    public Queue retryCreatePoQueue() {
        return QueueBuilder.durable(RETRY_CREATE_PO_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", DEAD_EXCHANGE_NAME) // 死信交换机
                .withArgument("x-dead-letter-routing-key", DEAD_COMMON_ROUTING_KEY) // 死信路由key
                .build();
    }

    // banding
    // 业务-po-banding
    @Bean
    public Binding businessCreatePoBinding() {
        return BindingBuilder.bind(businessCreatePoQueue()).to(businessCreatePoExchange()).with(BUSINESS_CREATE_PO_ROUTING_KEY);
    }

    // 重试-po-banding
    @Bean
    public Binding retryCreatePoBinding() {
        return BindingBuilder.bind(retryCreatePoQueue()).to(retryCreatePoExchange()).with(RETRY_CREATE_PO_ROUTING_KEY).noargs();
    }


}
