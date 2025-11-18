package com.liuxu.mq.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 推送消息给MQ失败记录表
 *
 * @date: 2025-11-07
 * @author: liuxu
 */
@Builder
@TableName("mq_send_confirm_error")
@Data
public class MqSendConfirmErrorDO {
    /**
     * CREATE TABLE `mq_send_confirm_error` (
     * `id` varchar(50) NOT NULL,
     * `correlation_data_id` varchar(50) DEFAULT NULL COMMENT 'Correlation消息唯一标识',
     * `cause` text COMMENT '消息发送失败的原因',
     * `created_date` datetime DEFAULT NULL,
     * `created_by` varchar(50) DEFAULT NULL,
     * `last_modified_date` datetime DEFAULT NULL,
     * `last_modified_by` varchar(50) DEFAULT NULL,
     * `is_del` tinyint(3) unsigned zerofill DEFAULT NULL COMMENT '是否删除（0:false 1:true）',
     * `exchange` varchar(255) DEFAULT NULL COMMENT '消息发送的交换机名称（如有）',
     * `routing_key` varchar(255) DEFAULT NULL COMMENT '消息路由key',
     * `message_body` text COMMENT '消息体',
     * PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='mq发送消息确认失败记录';
     */
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * Correlation消息唯一标识
     */
    private String correlationDataId;
    /**
     * 消息发送失败的原因
     */
    private String cause;

    /**
     * 消息发送的交换机名称（如有）
     */
    private String exchange;
    /**
     * 消息路由key
     */
    private String routingKey;
    /**
     * 消息体
     */
    private String messageBody;

    /**
     * 消息处理状态(0:未处理 1:已发送成功)
     */
    private int status;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 修改时间
     */
    private LocalDateTime lastModifiedDate;
    /**
     * 修改人
     */
    private String lastModifiedBy;
    /**
     * 是否删除（0:false 1:true）
     */
    private int isDel;

}
