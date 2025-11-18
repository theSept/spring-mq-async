package com.liuxu.mq.controller;

import com.liuxu.mq.producer.EmailSend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date: 2025-11-05
 * @author: liuxu
 */
@RestController
@RequestMapping("/api")
public class TestMQ01Controller {

    @Autowired
    private EmailSend emailSend;


    @GetMapping("/send-eamil")
    public String launchProcess() {
        String s = emailSend.sendMail("usertest.com", "尊敬的xx 采购申请单xxx需要您的审批....");
        return "OK-> " + s;
    }

}
