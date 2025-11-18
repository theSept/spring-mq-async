package com.liuxu.mq.producer.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 采购申请单
 *
 * @date: 2025-11-17
 * @author: liuxu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PROrderVo {

    private String applicant;

    private LocalDateTime applicantDate;

    private String vendorCode;

    private String wbs;

    private String commodityCode;

}
