package com.liuxu.mq.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @date: 2025-11-07
 * @author: liuxu
 */
@Builder
@Data
@TableName("mq_send_email_log")
public class MqSendEmailLogDO {
    /**
     * CREATE TABLE `mq_send_email_log` (
     *   `id` varchar(50) NOT NULL,
     *   `correlation_data_id` varchar(50) DEFAULT NULL COMMENT 'Correlation消息唯一标识',
     *   `receive` text COMMENT '邮件收件人',
     *   `sendEmailAccount` varchar(255) DEFAULT NULL COMMENT '邮件发送人',
     *   `emailTheme` varchar(255) DEFAULT NULL COMMENT '邮件主题',
     *   `content` text COMMENT '邮件内容',
     *   `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮件状态(0:mq处理中 1:成功 2:失败)',
     *   `errorMessage` text COMMENT '发送失败的错误信息',
     *   `errorTime` datetime DEFAULT NULL COMMENT '发送失败时间',
     *   `created_date` datetime DEFAULT NULL,
     *   `created_by` varchar(50) DEFAULT NULL,
     *   `last_modified_date` datetime DEFAULT NULL,
     *   `last_modified_by` varchar(50) DEFAULT NULL,
     *   `is_del` tinyint(3) unsigned zerofill DEFAULT NULL COMMENT '是否删除（0:false 1:true）',
     *   PRIMARY KEY (`id`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='mq发送处理邮件记录';
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
     * 邮件收件人
     */
    private String receive;
    /**
     * 邮件发送人
     */
    private String sendEmailAccount;
    /**
     * 邮件主题
     */
    private String emailTheme;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 邮件状态(0:mq处理中 1:成功 2:失败)
     */
    private Integer status;
    /**
     * 发送失败的错误信息
     */
    private String errorMessage;
    /**
     * 重试次数
     */
    private Integer retryCount;
    /**
     * 发送失败时间
     */
    private LocalDateTime errorTime;
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
    private String isDel;
}
