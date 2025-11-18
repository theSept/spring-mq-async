package com.liuxu.mq.server.mq;

import com.liuxu.mq.dal.dataobject.MqBusinessFailureMessageDO;
import com.liuxu.mq.dal.mysql.MqBusinessFailureMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MQ业务失败消息服务实现类
 *
 * @date: 2025-11-09
 * @author: liuxu
 */
@Slf4j
@Service
public class MqBusinessFailureMessageServiceImpl implements MqBusinessFailureMessageService {

    @Autowired
    private MqBusinessFailureMessageMapper mqBusinessFailureMessageMapper;

    @Override
    public String save(MqBusinessFailureMessageDO mqBusinessFailureMessageDO) {
        mqBusinessFailureMessageMapper.insert(mqBusinessFailureMessageDO);
        return mqBusinessFailureMessageDO.getId();
    }
}
