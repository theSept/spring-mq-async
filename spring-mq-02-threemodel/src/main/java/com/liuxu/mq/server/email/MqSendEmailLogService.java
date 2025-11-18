package com.liuxu.mq.server.email;

import com.liuxu.mq.dal.dataobject.MqSendEmailLogDO;

/**
 * @date: 2025-11-07
 * @author: liuxu
 */
public interface MqSendEmailLogService {

    /**
     * 记录邮件记录
     *
     * @param mqSendEmailLogDO
     * @return
     */
    String saveEmail(MqSendEmailLogDO mqSendEmailLogDO);

    /**
     * 更新
     */
    void updateEmail(MqSendEmailLogDO mqSendEmailLogDO);

}
