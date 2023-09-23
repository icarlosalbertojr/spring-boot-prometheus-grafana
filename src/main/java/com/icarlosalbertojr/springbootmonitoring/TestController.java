package com.icarlosalbertojr.springbootmonitoring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public void test(@RequestParam(required = false) String test) {
        System.out.println("Testing: " + test);
    }

}
