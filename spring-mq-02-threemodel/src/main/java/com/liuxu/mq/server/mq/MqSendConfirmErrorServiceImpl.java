package com.liuxu.mq.server.mq;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuxu.mq.constant.MQTypeEnum;
import com.liuxu.mq.dal.dataobject.MqSendConfirmErrorDO;
import com.liuxu.mq.dal.mysql.MqSendConfirmErrorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @date: 2025-11-07
 * @author: liuxu
 */
@Service
public class MqSendConfirmErrorServiceImpl implements MqSendConfirmErrorService {

    @Autowired
    MqSendConfirmErrorMapper mqSendConfirmErrorMapper;


    @Override
    public String saveConfirmError(MQTypeEnum mqTypeEnum, String correlationDataId, String cause) {
        MqSendConfirmErrorDO mqSendConfirmErrorDO = MqSendConfirmErrorDO.builder()
                .correlationDataId(correlationDataId)
                .cause(cause)
                .status(0) // 未处理
                .exchange(mqTypeEnum.getExchange())
                .routingKey(mqTypeEnum.getRoutingKey())
                .messageBody(null)
                .createdDate(LocalDateTime.now())
                .build();
        mqSendConfirmErrorMapper.insert(mqSendConfirmErrorDO);
        return mqSendConfirmErrorDO.getId();
    }
}
