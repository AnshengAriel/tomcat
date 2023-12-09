package com.ariel.tomcat.mvc;

import com.ariel.tomcat.mvc.controller.UserController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public abstract class Dispatcher {

    private final Method method;

    private final Object controller;

    private final String[] argNames;

    private final Class<?>[] argTypes;

    protected Dispatcher(Method method, Object controller) {
        this.method = method;
        this.controller = controller;
        Parameter[] parameters = method.getParameters();
        this.argNames = new String[parameters.length];
        this.argTypes = new Class[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            argNames[i] = parameters[i].getName();
            argTypes[i] = parameters[i].getType();
        }
    }

    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response) {
        Object[] args = new Object[argNames.length];
        for (int i = 0; i < argTypes.length; i++) {
            Class<?> argType = argTypes[i];
            if (argType == HttpServletRequest.class) {
                args[i] = request;
            } else if (argType == HttpServletResponse.class) {
                args[i] = response;
            } else if (argType == HttpSession.class) {
                args[i] = request.getSession();
            } else if (Map.class.isAssignableFrom(argType)) {
                args[i] = request.getParameterMap();
            } else if (argType == Integer.class) {
                args[i] = Integer.valueOf(getOrDefault(i, request, "0"));
            } else if (argType == String.class) {
                args[i] = getOrDefault(i, request, "");
            } else if (argType == boolean.class) {
                args[i] = Boolean.valueOf(getOrDefault(i, request, "false"));
            } else {
                throw new RuntimeException("Unknown Arguments");
            }
        }
        Object result;
        try {
            result = method.invoke(controller, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        if (result == null) {
            return new ModelAndView();
        }
        if (result instanceof String) {
            String s = (String) result;
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setView(s);
            return modelAndView;
        }
        return (ModelAndView) result;
    }

    private String getOrDefault(int index, HttpServletRequest request, String defaultValue) {
        String parameter = request.getParameter(argNames[index]);
        return parameter != null ? parameter : defaultValue;
    }

    public static void main(String[] args) {
        Method[] methods = UserController.class.getDeclaredMethods();
        for (Method method : methods) {
            for (Parameter parameter : method.getParameters()) {
                System.out.println("parameter.getName() = " + parameter.getName());
                System.out.println("parameter.getType() = " + parameter.getType());
                System.out.println("parameter.getParameterizedType() = " + parameter.getParameterizedType());
            }
        }
    }
}
