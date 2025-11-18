package com.liuxu.mq.message;

import com.liuxu.mq.constant.MQTypeEnum;
import com.liuxu.mq.producer.vo.PROrderVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建PO消息体
 *
 * @date: 2025-11-17
 * @author: liuxu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePoMessage implements MQMessage {

    private String errorStackTrace;

    // PO订单信息
    private PROrderVo prOrderVo;

    private String businessTaskId;


    @Override
    public String getBusinessType() {
        return MQTypeEnum.CREATE_PO.getBusinessType();
    }

    @Override
    public String getBusinessTaskId() {
        return businessTaskId;
    }

    @Override
    public String getErrorStackTrace() {
        return errorStackTrace;
    }
}
