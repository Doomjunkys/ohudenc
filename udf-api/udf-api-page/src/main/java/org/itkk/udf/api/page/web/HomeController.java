package org.itkk.udf.api.page.web;

import org.itkk.udf.api.common.CommonConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        return "index";
    }

    /**
     * 注册页
     *
     * @param map map
     * @return String
     */
    @GetMapping("/registered.html")
    public String registered(@RequestParam("redirectUrl") String redirectUrl, Model map) {
        map.addAttribute("redirectUrl", redirectUrl);
        map.addAttribute("tokenName", CommonConstant.PARAMETER_NAME_TOKEN);
        return "registered";
    }
}
