CREATE TABLE `mq_business_failure_message` (
   `id` varchar(50) NOT NULL,
   `business_type` varchar(50) DEFAULT NULL COMMENT '业务场景类型',
   `business_id` varchar(50) DEFAULT NULL COMMENT '业务id',
   `reason` varchar(100) DEFAULT NULL COMMENT '原因（进入死信队列的原因）',
   `reason_time` datetime DEFAULT NULL COMMENT '进入死信队列的时间',
   `message_body` text COMMENT '消息体JSON',
   `error_stack_trace` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '处理消息失败的堆栈错误',
   `created_date` datetime DEFAULT NULL,
   `created_by` varchar(50) DEFAULT NULL,
   `last_modified_date` datetime DEFAULT NULL,
   `last_modified_by` varchar(50) DEFAULT NULL,
   `is_del` tinyint(3) unsigned zerofill DEFAULT NULL COMMENT '是否删除（0:false 1:true）',
   `exchange` varchar(255) DEFAULT NULL COMMENT '消息发送的交换机名称（如有）',
   `routing_keys` varchar(255) DEFAULT NULL COMMENT '消息路由key列表',
   `queue` varchar(255) DEFAULT NULL COMMENT '队列',
   `status` tinyint DEFAULT NULL COMMENT '消息处理状态(0:未处理 1:已处理)',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='mq处理业务失败的消息';


CREATE TABLE `mq_send_confirm_error` (
     `id` varchar(50) NOT NULL,
     `correlation_data_id` varchar(50) DEFAULT NULL COMMENT 'Correlation消息唯一标识',
     `cause` text COMMENT '消息发送失败的原因',
     `created_date` datetime DEFAULT NULL,
     `created_by` varchar(50) DEFAULT NULL,
     `last_modified_date` datetime DEFAULT NULL,
     `last_modified_by` varchar(50) DEFAULT NULL,
     `is_del` tinyint(3) unsigned zerofill DEFAULT NULL COMMENT '是否删除（0:false 1:true）',
     `exchange` varchar(255) DEFAULT NULL COMMENT '消息发送的交换机名称（如有）',
     `routing_key` varchar(255) DEFAULT NULL COMMENT '消息路由key',
     `message_body` text COMMENT '消息体',
     `status` tinyint DEFAULT NULL COMMENT '消息处理状态(0:未处理 1:已发送成功)',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='mq推送消息确认失败记录';



CREATE TABLE `mq_send_email_log` (
     `id` varchar(50) NOT NULL,
     `correlation_data_id` varchar(50) DEFAULT NULL COMMENT 'Correlation消息唯一标识',
     `receive` text COMMENT '邮件收件人',
     `send_email_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮件发送人',
     `email_theme` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮件主题',
     `content` text COMMENT '邮件内容',
     `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮件状态(0:mq处理中 1:成功 2:失败)',
     `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '发送失败的错误信息',
     `error_time` datetime DEFAULT NULL COMMENT '发送失败时间',
     `created_date` datetime DEFAULT NULL,
     `created_by` varchar(50) DEFAULT NULL,
     `last_modified_date` datetime DEFAULT NULL,
     `last_modified_by` varchar(50) DEFAULT NULL,
     `is_del` tinyint(3) unsigned zerofill DEFAULT NULL COMMENT '是否删除（0:false 1:true）',
     `retry_count` int unsigned DEFAULT NULL COMMENT '重试次数',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='mq发送邮件记录';


CREATE TABLE `mq_business_api_log` (
   `id` varchar(50) NOT NULL,
   `request_context` text COMMENT '请求消息体JSON',
   `response_context` text COMMENT '响应体JSON',
   `url` varchar(200) DEFAULT NULL COMMENT '请求路径',
   `status` tinyint DEFAULT NULL COMMENT '消息处理状态(0:处理中 1:成功 2:失败 )',
   `error_message` text COMMENT '请求失败原因',
   `retry_count` int DEFAULT NULL COMMENT '重试次数',
   `created_date` datetime DEFAULT NULL,
   `created_by` varchar(50) DEFAULT NULL,
   `last_modified_date` datetime DEFAULT NULL,
   `last_modified_by` varchar(50) DEFAULT NULL,
   `is_del` tinyint(3) unsigned zerofill DEFAULT NULL COMMENT '是否删除（0:false 1:true）',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='mq调用应用接口日志';

