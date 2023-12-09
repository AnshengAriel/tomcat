package com.ariel.tomcat.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("LogFilter#doFilter");
        System.out.println(((HttpServletRequest) request).getRequestURI());
        chain.doFilter(request, response);
    }

}
