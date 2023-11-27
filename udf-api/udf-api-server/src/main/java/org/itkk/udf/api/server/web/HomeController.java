package org.itkk.udf.api.server.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    /**
     * 首页
     *
     * @param map map
     * @return String
     */
    @GetMapping(value = {"/", "/index"})
    public String index(Model map) {
        //放入作用域参数
        map.addAttribute("title", "ITKK");
        //跳转到首页
        return "index";
    }
}
