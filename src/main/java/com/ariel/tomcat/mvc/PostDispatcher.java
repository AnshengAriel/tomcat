package com.ariel.tomcat.mvc;

import java.lang.reflect.Method;

public class PostDispatcher extends Dispatcher {

    protected PostDispatcher(Method method, Object controller) {
        super(method, controller);
    }

}
