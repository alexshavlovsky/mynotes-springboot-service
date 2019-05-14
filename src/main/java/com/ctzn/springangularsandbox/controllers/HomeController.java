package com.ctzn.springangularsandbox.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController { // forward all routes "/*" except "/*.*" to index.html
    @GetMapping("/{angularRoute:^[^.]+$}")
    public String index(@PathVariable String angularRoute) {
        return "forward:/index.html";
    }
}
