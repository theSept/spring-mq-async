package com.liuxu.mq.server.email;

import com.liuxu.mq.dal.dataobject.MqSendEmailLogDO;
import com.liuxu.mq.dal.mysql.MqSendEmailLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date: 2025-11-07
 * @author: liuxu
 */
@Service
public class MqSendEmailLogServiceImpl implements MqSendEmailLogService {
    @Autowired
    MqSendEmailLogMapper mqSendEmailLogMapper;


    @Override
    public String saveEmail(MqSendEmailLogDO mqSendEmailLogDO) {
        mqSendEmailLogMapper.insert(mqSendEmailLogDO);
        return mqSendEmailLogDO.getId();
    }

    @Override
    public void updateEmail(MqSendEmailLogDO mqSendEmailLogDO) {
        mqSendEmailLogMapper.updateById(mqSendEmailLogDO);
    }


}
