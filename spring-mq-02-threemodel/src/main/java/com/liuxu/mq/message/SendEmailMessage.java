package com.liuxu.mq.message;

import com.liuxu.mq.constant.MQTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发送邮件的消息
 *
 * @date: 2025-11-08
 * @author: liuxu
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailMessage implements MQMessage, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件日志表中的ID
     */
    private String emailLogId;

    /**
     * 错误信息
     */
    private String stackTrace;

    @Override
    public String getBusinessType() {
        return MQTypeEnum.SEND_EMAIL.name();
    }

    @Override
    public String getBusinessTaskId() {
        return emailLogId;
    }

    @Override
    public String getErrorStackTrace() {
        return stackTrace;
    }
}
