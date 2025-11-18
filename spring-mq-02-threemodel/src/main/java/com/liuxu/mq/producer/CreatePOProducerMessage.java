package com.liuxu.mq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuxu.mq.config.RabbitMQConfig;
import com.liuxu.mq.constant.MQTypeEnum;
import com.liuxu.mq.dal.dataobject.MqBusinessApiLogDO;
import com.liuxu.mq.message.CreatePoMessage;
import com.liuxu.mq.producer.vo.PROrderVo;
import com.liuxu.mq.server.api.MqBusinessApiLogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @date: 2025-11-17
 * @author: liuxu
 */
@Slf4j
@Component
public class CreatePOProducerMessage {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MqBusinessApiLogService mqBusinessApiLogService;

    @Autowired
    private ObjectMapper objectMapper;

    // 创建PO
    public String createPo(PROrderVo prOrderVo) throws JsonProcessingException {

        // 记录已经api调用

        MqBusinessApiLogDO apiLogDO = MqBusinessApiLogDO.builder()
                .requestContext(objectMapper.writeValueAsString(prOrderVo))
                .status(0)
                .createdDate(LocalDateTime.now())
                .build();
        mqBusinessApiLogService.save(apiLogDO);
        String id = apiLogDO.getId();

        // 业务id + 业务类型
        CorrelationData correlationData = new CorrelationData(id + "#" + MQTypeEnum.CREATE_PO.getBusinessType());

        // 推送消息
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.BUSINESS_CREATE_PO_EXCHANGE_NAME,
                RabbitMQConfig.BUSINESS_CREATE_PO_ROUTING_KEY,
                CreatePoMessage.builder().prOrderVo(prOrderVo).businessTaskId(id).build(),
                correlationData
        );
        log.info("推送-生成PO消息");

        return id;
    }

    // 重试-创建po
    public void retryCreatePo(CreatePoMessage createPoMessage, int retryCount) {
        CorrelationData correlationData = new CorrelationData(createPoMessage.getBusinessTaskId() + "#" + MQTypeEnum.CREATE_PO.getBusinessType());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RETRY_CREATE_PO_EXCHANGE_NAME,
                RabbitMQConfig.RETRY_CREATE_PO_ROUTING_KEY,
                createPoMessage,
                message -> {
                    message.getMessageProperties().setHeader("retryCount", retryCount);
                    // 指数计算延迟推送时间
                    double delay = 1000 * Math.pow(2, retryCount);
                    message.getMessageProperties().setHeader("x-delay", delay); // 设置延迟时间

                    return message;
                },
                correlationData
        );

    }


}
