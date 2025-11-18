package com.liuxu.mq.message;

/**
 * @date: 2025-11-08
 * @author: liuxu
 */
public interface MQMessage {

    /**
     * 消息的业务类型
     */
    String getBusinessType();

    /**
     * 获取业务任务id
     */
    String getBusinessTaskId();

    String getErrorStackTrace();


}
