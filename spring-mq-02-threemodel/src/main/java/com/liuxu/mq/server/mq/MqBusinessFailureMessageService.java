package com.liuxu.mq.server.mq;

import com.liuxu.mq.dal.dataobject.MqBusinessFailureMessageDO;

/**
 * MQ业务失败消息服务
 *
 * @date: 2025-11-09
 * @author: liuxu
 */
public interface MqBusinessFailureMessageService {


    /**
     * 保存返回数据主键id
     */
    String save(MqBusinessFailureMessageDO mqBusinessFailureMessageDO);

}
