package com.gether.bigdata.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author: myp
 * @date: 16/10/22
 */
@Controller
@RequestMapping("/views")
@ApiIgnore
public class ViewController {

    @RequestMapping("/index")
    public String index(ModelMap map) {
        // 加入一个属性，用来在模板中读取
        map.addAttribute("host", "http://gether.me");
        // return模板文件的名称，对应src/main/resources/templates/index.html
        return "index";
    }
}
