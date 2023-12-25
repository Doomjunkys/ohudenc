package org.itkk.udf.api.page.web;

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
    @GetMapping(value = {"/", "/index.html"})
    public String index(Model map) {
        //跳转到首页
        return "index";
    }
}
