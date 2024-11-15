package com.example.demo.common.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("hello")
    public String[] hello() {

        log.info("-------------");
        log.info("-----------------");
        log.info("hello");

        log.info("===================");

        return new String[]{"hello", "world"};

    }

}
