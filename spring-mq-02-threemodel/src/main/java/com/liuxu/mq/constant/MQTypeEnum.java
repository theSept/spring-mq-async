package com.liuxu.mq.constant;

import com.liuxu.mq.config.RabbitMQConfig;
import com.liuxu.mq.server.mq.MqSendConfirmErrorService;

/**
 * MQ类型枚举
 *
 * @date: 2025-11-07
 * @author: liuxu
 */
public enum MQTypeEnum {

    SEND_EMAIL("SEND_EMAIL", RabbitMQConfig.BUSINESS_EMAIL_EXCHANGE_NAME, RabbitMQConfig.BUSINESS_EMAIL_ROUTING_KEY) {
    },
    CREATE_PO("CREATE_PO", RabbitMQConfig.BUSINESS_CREATE_PO_EXCHANGE_NAME, RabbitMQConfig.BUSINESS_CREATE_PO_ROUTING_KEY) {
    },

    ;
    /**
     * 消息的业务类型
     */
    private String businessType;
    /**
     * 交换机
     */
    private String exchange;
    /**
     * 路由key
     */
    private String routingKey;


    // 保存消息推送交换机失败日志
    // public void saveConfirmCallbackLog(MqSendConfirmErrorService mqSendConfirmErrorService, String id, String cause) {
    //     mqSendConfirmErrorService.saveConfirmError(this, id, cause);
    // }

    /**
     * 根据业务类型获取对于的枚举
     *
     * @param businessType 业务类型
     * @return MQ类型枚举，不存在返回{@code null}
     */
    public static MQTypeEnum getMQTypeEnum(String businessType) {
        for (MQTypeEnum mqTypeEnum : values()) {
            if (mqTypeEnum.businessType.equals(businessType)) {
                return mqTypeEnum;
            }
        }
        return null;
    }


    MQTypeEnum(String businessType, String exchange, String routingKey) {
        this.businessType = businessType;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getExchange() {
        return exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}
