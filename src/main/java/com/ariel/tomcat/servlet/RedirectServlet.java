package com.ariel.tomcat.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/redirect")
public class RedirectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("-------------------/redirect------------------");
        String name = req.getParameter("name");
        String url = "/tomcat/name" + (name != null ? ("?name=" + name) : "");
        resp.sendRedirect(url);

        // 下面两行开启永久重定向，浏览器将缓存新地址
        resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY); // 301
        resp.setHeader("Location", "/tomcat/name");
    }

}
