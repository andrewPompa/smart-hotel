package com.dynamics.andrzej.smart.hotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewsController {

    @GetMapping("/login/client")
    public String index() {
        return "login_client";
    }

    @RequestMapping("/login/admin")
    public String loginAdmin() {
        return "login_admin";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }
}
