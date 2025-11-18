package com.liuxu.mq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liuxu.mq.consumer.EmailConsumerService;
import com.liuxu.mq.producer.CreatePOProducerMessage;
import com.liuxu.mq.producer.ProducerMessage;
import com.liuxu.mq.producer.vo.PROrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @date: 2025-11-06
 * @author: liuxu
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private ProducerMessage producerMessage;

    @Autowired
    private CreatePOProducerMessage createPOProducerMessage;

    @GetMapping("/send-email")
    public String launchProcess() {
        String s = producerMessage.sendEmail("usertest.com", "尊敬的xx 采购申请单xxx需要您的审批....");
        return "OK-> " + s;
    }


    @GetMapping("/create-po")
    public String createPo() throws JsonProcessingException {
        String id = createPOProducerMessage.createPo(PROrderVo.builder().wbs("wbs-EAD")
                .applicant("6943334").vendorCode("000553242").commodityCode("FAINAL-FAAFF")
                .applicantDate(LocalDateTime.now()).build());
        return "API->" + id;
    }

}
