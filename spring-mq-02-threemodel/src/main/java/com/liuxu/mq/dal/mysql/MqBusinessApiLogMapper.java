package com.liuxu.mq.dal.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuxu.mq.dal.dataobject.MqBusinessApiLogDO;
import com.liuxu.mq.dal.dataobject.MqSendEmailLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @date: 2025-11-18
 * @author: liuxu
 */
@Mapper
public interface MqBusinessApiLogMapper extends BaseMapper<MqBusinessApiLogDO> {

}
