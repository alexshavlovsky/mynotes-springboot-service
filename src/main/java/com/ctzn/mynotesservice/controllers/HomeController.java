package com.ctzn.mynotesservice.controllers;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@ConditionalOnProperty(name = "app.static.server.enabled", havingValue = "true")
public class HomeController { // forward all routes "/*" except "/*.*" to index.html
    @GetMapping("/{angularRoute:^[^.]+$}")
    public String index(@PathVariable String angularRoute) {
        return "forward:/index.html";
    }
}
