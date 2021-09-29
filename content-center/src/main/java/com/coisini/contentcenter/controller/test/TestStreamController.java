package com.coisini.contentcenter.controller.test;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.stream.messaging.Source;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/stream")
//public class TestStreamController {
//
//    @Autowired
//    private Source source;
//
//    @GetMapping("test")
//    public String test() {
//        source.output().send(
//                MessageBuilder.withPayload("消息体").build()
//        );
//
//        return "success";
//    }
//
//}
