package com.kingja.rxbus2;

import java.lang.reflect.Method;

/**
 * Description:TODO
 * Create Time:2017/4/13 15:28
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class SubscriberMethod {
    public ThreadMode getThreadMode() {
        return threadMode;
    }

    final ThreadMode threadMode;
    final Method method;
    final Class<?> eventType;

    public SubscriberMethod(Method method, Class<?> eventType,ThreadMode threadMode) {
        this.method = method;
        this.eventType = eventType;
        this.threadMode = threadMode;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getEventType() {
        return eventType;
    }
}
