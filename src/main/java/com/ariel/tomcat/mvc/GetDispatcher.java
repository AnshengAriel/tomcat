package com.ariel.tomcat.mvc;

import java.lang.reflect.Method;

public class GetDispatcher extends Dispatcher {

    protected GetDispatcher(Method method, Object controller) {
        super(method, controller);
    }

}
