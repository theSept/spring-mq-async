package com.liuxu.mq.server.api;

import com.liuxu.mq.dal.dataobject.MqBusinessApiLogDO;

/**
 * @date: 2025-11-18
 * @author: liuxu
 */
public interface MqBusinessApiLogService {

    void save(MqBusinessApiLogDO mqBusinessApiLogDO);

    void update(MqBusinessApiLogDO mqBusinessApiLogDO);

}
