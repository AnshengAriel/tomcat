package com.ariel.tomcat.servlet;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(urlPatterns = "/signin")
public class SignInServlet extends HttpServlet {

    private Map<String, String> users = Map.of("bob", "bob123", "alice", "alice123", "tom", "tomcat");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Sign In</h1>");
        pw.write("<form action=\"/tomcat/signin\" method=\"post\">");
        pw.write("<p>Username: <input name=\"username\"></p>");
        pw.write("<p>Password: <input name=\"password\" type=\"password\"></p>");
        pw.write("<p><button type=\"submit\">Sign In</button> <a href=\"/\">Cancel</a></p>");
        pw.write("</form>");
        pw.flush();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("username");
        String password = req.getParameter("password");
        String expectedPassword = users.get(name.toLowerCase());
        System.out.printf("name[%s] password[%s] expectedPassword[%s]%n", name, password, expectedPassword);
        if (expectedPassword != null && expectedPassword.equals(password)) {
            // 登录成功:将用户信息写入session
            // session是一个Map本地缓存，key为tomcat自动生成的随机字符串，此key会自动设置到cookie中返回给前端
            req.getSession().setAttribute("user", name);
            resp.sendRedirect("/tomcat");
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

}
