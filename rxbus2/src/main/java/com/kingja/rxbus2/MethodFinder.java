package com.kingja.rxbus2;

import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:TODO
 * Create Time:2017/4/13 15:27
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class MethodFinder {
    private static final int BRIDGE = 0x40;
    private static final int SYNTHETIC = 0x1000;
    private static final Map<Class<?>, List<SubscriberMethod>> METHOD_CACHE = new ConcurrentHashMap<>();
    private static final int MODIFIERS_IGNORE = Modifier.ABSTRACT | Modifier.STATIC | BRIDGE | SYNTHETIC;

    public List<SubscriberMethod> findMehod(Class<?> subscriberClass) {
        List<SubscriberMethod> subscriberMethods = METHOD_CACHE.get(subscriberClass);
        if (subscriberMethods != null) {
            return subscriberMethods;
        }
        subscriberMethods = findByReflect(subscriberClass);
        return subscriberMethods;
    }

    private List<SubscriberMethod> findByReflect(Class<?> subscriberClass) {
        List<SubscriberMethod> subscriberMethods = new ArrayList<>();
        Method[] methods = subscriberClass.getDeclaredMethods();
        for (Method method : methods) {
            int modifiers = method.getModifiers();
            if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1) {
                    Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                    if (subscribeAnnotation != null) {
                        ThreadMode threadMode = subscribeAnnotation.threadMode();
                        Class<?> eventType = parameterTypes[0];
                        subscriberMethods.add(new SubscriberMethod(method, eventType, threadMode));
                    }
                }
            }
        }
        return subscriberMethods;
    }
}
