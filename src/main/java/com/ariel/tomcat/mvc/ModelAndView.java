package com.ariel.tomcat.mvc;

import lombok.Data;

import java.util.Map;

@Data
public class ModelAndView {

    /**
     * 页面地址
     */
    private String view;

    /**
     * 模型
     */
    private Map<String, Object> model;
}
