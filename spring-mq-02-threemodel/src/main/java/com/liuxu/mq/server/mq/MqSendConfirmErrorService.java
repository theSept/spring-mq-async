package com.liuxu.mq.server.mq;

import com.liuxu.mq.constant.MQTypeEnum;

/**
 * @date: 2025-11-07
 * @author: liuxu
 */
public interface MqSendConfirmErrorService {

    // 保存
    public String saveConfirmError(MQTypeEnum mqTypeEnum, String correlationDataId, String cause);

}
