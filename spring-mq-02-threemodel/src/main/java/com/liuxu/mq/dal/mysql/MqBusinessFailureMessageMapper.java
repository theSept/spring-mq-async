package com.liuxu.mq.dal.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuxu.mq.dal.dataobject.MqBusinessFailureMessageDO;
import com.liuxu.mq.dal.dataobject.MqSendEmailLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @date: 2025-11-09
 * @author: liuxu
 */
@Mapper
public interface MqBusinessFailureMessageMapper extends BaseMapper<MqBusinessFailureMessageDO> {

}
