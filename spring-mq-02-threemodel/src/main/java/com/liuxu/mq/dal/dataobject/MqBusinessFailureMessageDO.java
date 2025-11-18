package com.liuxu.mq.dal.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息业务消费者处理失败的消息实体类
 * 对应数据库表：mq_business_failure_message
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("mq_business_failure_message") // 指定对应的数据库表名
public class MqBusinessFailureMessageDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID，使用字符串类型
     * 对应数据库列：id
     *
     * @TableId: 指定此为主键，并指定ID生成策略为 Input (手动输入)
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 业务场景类型
     * 对应数据库列：business_type
     *
     * @TableField: 指定数据库中的列名，并与驼峰命名属性对应
     */
    @TableField("business_type")
    private String businessType;
    /**
     * 业务id
     * 对应数据库列：business_id
     */
    @TableField("business_id")
    private String businessId;
    /**
     * 原因（进入死信队列的原因）
     * 对应数据库列：reason
     */
    @TableField("reason")
    private String reason;
    /**
     * 进入死信队列的时间
     * 对应数据库列：reason_time
     *
     * @TableField: 使用 fill = FieldFill.INSERT 表示在插入时自动填充
     */
    @TableField(value = "reason_time", fill = FieldFill.INSERT)
    private LocalDateTime reasonTime;
    /**
     * 消息体JSON
     * 对应数据库列：message_body
     *
     * @TableField: 使用 jdbcType = Types.LONGVARCHAR 或其他大型文本类型来处理 TEXT
     * MyBatis-Plus 通常能自动处理，但显式声明可以避免一些潜在问题
     */
    @TableField("message_body")
    private String messageBody;
    /**
     * 处理消息失败的堆栈错误
     * 对应数据库列：error_stack_trace
     */
    @TableField("error_stack_trace")
    private String errorStackTrace;
    /**
     * 创建时间
     *
     * @TableField(fill = FieldFill.INSERT): 表示插入时自动填充
     * 自动填充功能需要配合 @Component 注解的 MetaObjectHandler 实现类使用
     */
    @TableField(value = "created_date", fill = FieldFill.INSERT)
    private LocalDateTime createdDate;
    /**
     * 创建人
     *
     * @TableField(fill = FieldFill.INSERT): 表示插入时自动填充
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;
    /**
     * 最后修改时间
     *
     * @TableField(fill = FieldFill.INSERT_UPDATE): 表示插入和更新时自动填充
     */
    @TableField(value = "last_modified_date", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastModifiedDate;
    /**
     * 最后修改人
     *
     * @TableField(fill = FieldFill.INSERT_UPDATE): 表示插入和更新时自动填充
     */
    @TableField(value = "last_modified_by", fill = FieldFill.INSERT_UPDATE)
    private String lastModifiedBy;
    /**
     * 是否删除（0:false 1:true）
     * 使用 Boolean 类型比 tinyint 更符合 Java 面向对象的思维
     *
     * @TableField: 显式转换 0/1 为 false/true
     */
    @TableField("is_del")
    private Boolean isDel;
    /**
     * 消息发送的交换机名称（如有）
     * 对应数据库列：exchange
     */
    @TableField("exchange")
    private String exchange;
    /**
     * 消息路由key列表
     * 数据库中是 varchar(255)，可以存储 JSON 字符串表示的列表
     */
    @TableField("routing_keys")
    private String routingKeys;

    /**
     * 队列
     * 对应数据库列：queue
     */
    @TableField("queue")
    private String queue;
    /**
     * 消息处理状态(0:未处理 1:已处理)
     * 使用枚举类型来定义状态，使代码更健壮、可读性更高
     */
    @TableField("status")
    private Integer status;

}