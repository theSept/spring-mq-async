package com.liuxu.mq.dal.dataobject;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * MQ调用应用接口的日志实体类
 * 对应数据表: mq_business_api_log
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TableName("mq_business_api_log")
public class MqBusinessApiLogDO {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 请求消息体JSON
     */
    @TableField("request_context")
    private String requestContext;

    /**
     * 响应体JSON
     */
    @TableField("response_context")
    private String responseContext;

    /**
     * 请求路径
     */
    @TableField("url")
    private String url;

    /**
     * 消息处理状态(0:处理中 1:成功 2:失败)
     */
    @TableField("status")
    private Integer status;

    /**
     * 请求失败原因
     */
    @TableField("error_message")
    private String errorMessage;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 创建日期
     */
    @TableField(value = "created_date", fill = FieldFill.INSERT)
    private LocalDateTime createdDate;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 最后修改日期
     */
    @TableField(value = "last_modified_date", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastModifiedDate;

    /**
     * 最后修改人
     */
    @TableField(value = "last_modified_by", fill = FieldFill.INSERT_UPDATE)
    private String lastModifiedBy;

    /**
     * 是否删除（0:false 1:true）
     * 注意: 0被转成false，1被转成true。使用Boolean类型更符合Java规范。
     * 如果您需要精确匹配0和1，也可以使用Integer类型。
     */
    @TableField("is_del")
    private Boolean isDel;

}
