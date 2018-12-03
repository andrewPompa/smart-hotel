package com.dynamics.andrzej.smart.hotel.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewsController {

    @GetMapping("/login/client")
    public String loginClient() {
        return "login_client";
    }

    @RequestMapping("/register/client")
    public String registerClient() {
        return "register_client";
    }

    @RequestMapping("/login/admin")
    public String loginAdmin() {
        return "login_admin";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/admin/room-search")
    public String adminSearch() {
        return "admin_room_search";
    }

    @RequestMapping("/admin/clients")
    public String adminClients() {
        return "admin_clients";
    }

    @RequestMapping("/admin/room")
    public String adminRoom() {
        return "admin_room";
    }

    @RequestMapping("/admin/reservation")
    public String adminReservation() {
        return "admin_reservation";
    }
}
