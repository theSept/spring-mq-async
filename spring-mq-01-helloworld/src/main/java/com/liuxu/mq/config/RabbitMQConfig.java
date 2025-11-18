package com.liuxu.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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

/**
 * @date: 2025-11-04
 * @author: liuxu
 */
@Configuration
@ConditionalOnClass(RabbitTemplate.class)
public class RabbitMQConfig {

    // 交换机
    public static final String EMAIL_EXCHANGE_NAME = "my.mail.Exchange";
    // 队列
    public static final String EMAIL_QUEUE_NAME = "my.mail.Exchange";
    // 绑定的路由key
    public static final String EMAIL_ROUTING_KEY = "email.welcome";


    // 修改MQ的消息使用转换器 ，
    @Bean
    public MessageConverter messageConverter() {
        // Jackson2JsonMessageConverter 转换器会将 Message 类型也进行转换成JSON。
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Primary // 优先使用
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 全局配置
        // 添加后处理器-发布前
        rabbitTemplate.setBeforePublishPostProcessors(message -> {
            // 设置消息持久胡：2-消息持久化
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });
        // 设置消息转换器
        rabbitTemplate.setMessageConverter(messageConverter());

        return rabbitTemplate;
    }


    // 1.声明交换机
    @Bean
    public DirectExchange emailExchange() {
        // 持久化：Broker 重启后，交换机依然存在。所有绑定到它的队列和路由规则都保持不变。
        // 第二个参数durable:true 交换机持久化
        return new DirectExchange(EMAIL_EXCHANGE_NAME, true, false);
    }


    // 2. 声明队列
    @Bean
    public Queue welcomeEmailQueue() {
        // 参数说明：name, durable
        // durable: true 表示队列会持久化 。Broker 重启后，队列和其中未消费的消息都存在。
        return QueueBuilder.durable(EMAIL_QUEUE_NAME).build();
    }

    // 3. 声明绑定关系
    @Bean
    public Binding welcomeEmailBinding() {
        // 将 welcomeEmailQueue 队列绑定到 emailExchange 交换机，并指定路由键
        return BindingBuilder
                .bind(welcomeEmailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

}
