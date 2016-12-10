package com.gether.bigdata.web;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author: myp
 * @date: 16/10/22
 */
@Controller
@EnableAutoConfiguration
@ApiIgnore
public class SampleController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        System.out.println(123);
        return "Hello gethermy123p!";
    }

    @RequestMapping("/geterror")
    @ResponseBody
    String errorJson() {
        throw new RuntimeException("json error");
    }

    @RequestMapping("/geterror2")
    @ResponseBody
    String error() throws Exception {
        throw new Exception("hehe error");
    }
}
