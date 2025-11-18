package com.liuxu.mq.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuxu.mq.config.RabbitMQConfig;
import com.liuxu.mq.dal.dataobject.MqBusinessApiLogDO;
import com.liuxu.mq.message.CreatePoMessage;
import com.liuxu.mq.producer.CreatePOProducerMessage;
import com.liuxu.mq.producer.vo.PROrderVo;
import com.liuxu.mq.server.api.MqBusinessApiLogService;
import com.liuxu.mq.utils.ExceptionUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 创建PO消息消费者
 *
 * @date: 2025-11-17
 * @author: liuxu
 */
@Slf4j
@Component
public class EAICreatePoConsumerService {

    @Autowired
    private CreatePOProducerMessage createPOProducerMessage;

    @Autowired
    private MqBusinessApiLogService mqBusinessApiLogService;

    @Autowired
    private ObjectMapper objectMapper;

    // 创建PO消费者
    @RabbitListener(queues = RabbitMQConfig.BUSINESS_CREATE_PO_QUEUE_NAME)
    public void createPo(CreatePoMessage createPoMessage, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        PROrderVo prOrderVo = createPoMessage.getPrOrderVo();

        String businessTaskId = createPoMessage.getBusinessTaskId();
        log.info("消费者-接收到创建PO消息,businessTaskId={}, deliveryTag={}, PROrderVo={}", businessTaskId, deliveryTag, objectMapper.writeValueAsString(prOrderVo));

        MqBusinessApiLogDO apiLogDO = MqBusinessApiLogDO.builder()
                .id(businessTaskId)
                .build();

        try {

            apiLogDO.setUrl("http://test/test/createPo");
            // 模拟创建PO
            Thread.sleep(1000);
            // 调用下游系统
            int i = 1 / 0;


            String response = "{code:200,msg:'成功'}";
            apiLogDO.setResponseContext(response);
            apiLogDO.setStatus(1);
            apiLogDO.setCreatedDate(LocalDateTime.now());

            // 确认消息
            channel.basicAck(deliveryTag, false);
        } catch (InterruptedException | ArithmeticException e) {
            // 出现异常
            String errorMsg = ExceptionUtil.printStackTraceToString(e);
            apiLogDO.setErrorMessage(errorMsg);
            apiLogDO.setStatus(2);
            apiLogDO.setCreatedDate(LocalDateTime.now());
            apiLogDO.setRetryCount(1);
            // 重试
            createPoMessage.setErrorStackTrace(errorMsg);
            createPOProducerMessage.retryCreatePo(createPoMessage, 1);
            channel.basicNack(deliveryTag, false, false); // 不重新入队
        }

        mqBusinessApiLogService.update(apiLogDO);

    }


    @RabbitListener(queues = RabbitMQConfig.RETRY_CREATE_PO_QUEUE_NAME)
    public void retryCreatePo(CreatePoMessage createPoMessage, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        PROrderVo prOrderVo = createPoMessage.getPrOrderVo();
        String businessTaskId = createPoMessage.getBusinessTaskId();
        log.info("消费者-【{}】接收到重试创建PO消息,businessTaskId={}, deliveryTag={}, PROrderVo={}", message.getMessageProperties().getHeader("retryCount"),
                businessTaskId, deliveryTag, prOrderVo);
        MqBusinessApiLogDO mqBusinessApiLogDO = new MqBusinessApiLogDO();
        mqBusinessApiLogDO.setId(businessTaskId);

        try {

            // 模拟创建PO
            Thread.sleep(1000);
            // 调用下游系统
            // int i = 1 / 0;


            String response = "{code:200,msg:'成功'}";
            mqBusinessApiLogDO.setResponseContext(response);
            mqBusinessApiLogDO.setStatus(1);
            mqBusinessApiLogDO.setCreatedDate(LocalDateTime.now());

            // 确认消息
            channel.basicAck(deliveryTag, false);
        } catch (InterruptedException | ArithmeticException e) {
            // 出现异常
            String errorMsg = ExceptionUtil.printStackTraceToString(e);
            mqBusinessApiLogDO.setErrorMessage(errorMsg);
            mqBusinessApiLogDO.setStatus(2);
            mqBusinessApiLogDO.setCreatedDate(LocalDateTime.now());

            int retryCount = message.getMessageProperties().getHeader("retryCount");
            mqBusinessApiLogDO.setRetryCount(retryCount);
            if (retryCount > 3) {
                // 拒绝，进入死信队列
                channel.basicReject(deliveryTag, false);
                log.info("消费者-重试创建PO消息失败，进入死信队列,businessTaskId={}, deliveryTag={}, PROrderVo={}", businessTaskId, deliveryTag, prOrderVo);
                return;
            }

            // 重试
            createPoMessage.setErrorStackTrace(errorMsg);
            createPOProducerMessage.retryCreatePo(createPoMessage, ++retryCount);
            // 不使用NACK，告诉MQ消息已经处理，如果NACK的话会直接接入死信交换机
            channel.basicAck(deliveryTag, false); // 不重新入队
        }

        mqBusinessApiLogService.update(mqBusinessApiLogDO);


    }

}
