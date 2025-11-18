package com.liuxu.mq.server.api;

import com.liuxu.mq.dal.dataobject.MqBusinessApiLogDO;
import com.liuxu.mq.dal.mysql.MqBusinessApiLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date: 2025-11-18
 * @author: liuxu
 */
@Service
public class MqBusinessApiLogServiceImpl implements MqBusinessApiLogService {

    @Autowired
    private MqBusinessApiLogMapper mqBusinessApiLogMapper;


    @Override
    public void save(MqBusinessApiLogDO mqBusinessApiLogDO) {
        mqBusinessApiLogMapper.insert(mqBusinessApiLogDO);
    }

    @Override
    public void update(MqBusinessApiLogDO mqBusinessApiLogDO) {
        mqBusinessApiLogMapper.updateById(mqBusinessApiLogDO);
    }
}
