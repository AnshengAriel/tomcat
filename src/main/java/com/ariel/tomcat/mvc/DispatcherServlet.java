package com.ariel.tomcat.mvc;

import com.ariel.tomcat.mvc.controller.UserController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/mvc/*")
public class DispatcherServlet extends HttpServlet {

    private final Map<String, GetDispatcher> getMappings = new HashMap<>();
    private final Map<String, PostDispatcher> postMappings = new HashMap<>();

    public DispatcherServlet() {
        UserController userController = new UserController();
        for (Method method : UserController.class.getDeclaredMethods()) {
            getMappings.put("/tomcat/mvc/user/" + method.getName(), new GetDispatcher(method, userController));
            postMappings.put("/tomcat/mvc/user/" + method.getName(), new PostDispatcher(method, userController));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        System.out.println("uri = " + uri);
        ModelAndView modelAndView = getMappings.get(uri).invoke(req, resp);
        if (modelAndView.getView().startsWith("redirect:")) {
            try {
                resp.sendRedirect(modelAndView.getView().substring(9));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 设置响应类型:
        resp.setContentType("text/html");
        // 获取输出流:
        PrintWriter pw = resp.getWriter();
        // 写入响应:
        pw.write("<h1>Hello, world!</h1>");
        // 最后不要忘记flush强制输出:
        pw.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        ModelAndView modelAndView = postMappings.get(uri).invoke(req, resp);
        if (modelAndView.getView().startsWith("redirect:")) {
            try {
                resp.sendRedirect(modelAndView.getView().substring(9));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 设置响应类型:
        resp.setContentType("text/html");
        // 获取输出流:
        PrintWriter pw = resp.getWriter();
        // 写入响应:
        pw.write("<h1>Hello, world!</h1>");
        // 最后不要忘记flush强制输出:
        pw.flush();
    }
}
