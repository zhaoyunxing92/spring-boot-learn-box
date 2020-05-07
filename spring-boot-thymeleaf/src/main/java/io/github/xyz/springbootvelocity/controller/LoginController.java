package io.github.xyz.springbootvelocity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhaoyunxing
 * @date 2020/5/6 16:08
 */
@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("name", "zhaoyunxing");
        model.addAttribute("pwd", "123456");
        model.addAttribute("title", "防诈平台登录");
        return "login";
    }
    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("name", "zhaoyunxing");
        model.addAttribute("pwd", "123456");
        model.addAttribute("title", "防诈平台-首页");
        return "pages/index";
    }

    @GetMapping(value = "/show")
    public String show(Model model) {
        model.addAttribute("uid", "123456789");
        model.addAttribute("name", "Jerry");
        return "show";
    }
}
