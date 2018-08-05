package com.gether.bigdata.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author: myp
 * @date: 16/10/22
 */
@Controller
@ApiIgnore
public class HelloController {

  @RequestMapping("/hello")
  @ResponseBody
  public String index() throws Exception {
    return "hello";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    return "login";
  }
}
