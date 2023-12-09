package com.ariel.tomcat.mvc.controller;

public class UserController {

    public String login(String username, String password) {
        System.out.println("username = " + username);
        System.out.println("password = " + password);
        return "success";
    }
}
