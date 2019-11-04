package com.ctzn.mynotesservice.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FrontendEnvironmentController {

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    // this controller will override default frontend client
    // environment properties stored in /static/env.js
    @GetMapping(value = "/env.js", produces = "application/javascript")
    public String overrideEnvironment() {
        return String.format(
                "(function (window) {\n" +
                        "  window._env = {\n" +
                        "    apiBaseUrl: '%s',\n" +
                        "  };\n" +
                        "}(this));\n",
                apiBaseUrl);
    }

}
