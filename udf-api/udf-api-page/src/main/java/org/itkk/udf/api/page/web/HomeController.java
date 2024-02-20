package org.itkk.udf.api.page.web;

import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.starter.file.db.service.DbFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    /**
     *
     */
    @Autowired
    private DbFileService dbFileService;

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
    public String registered(@RequestParam(required = false) String redirectUrl, Model map) {
        if (StringUtils.isNotBlank(redirectUrl)) {
            map.addAttribute("redirectUrl", redirectUrl);
        }
        return "registered";
    }

    /**
     * 登陆页
     *
     * @param map map
     * @return String
     */
    @GetMapping("/login.html")
    public String login(@RequestParam(required = false) String redirectUrl, Model map) {
        if (StringUtils.isNotBlank(redirectUrl)) {
            map.addAttribute("redirectUrl", redirectUrl);
        }
        return "login";
    }
}
