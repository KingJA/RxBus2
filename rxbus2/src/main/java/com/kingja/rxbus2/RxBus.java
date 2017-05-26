package com.kingja.rxbus2;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:TODO
 * Create Time:2017/5/25 15:12
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class RxBus {

    private final FlowableProcessor<Object> mFlowableProcessor;
    private static RxBus mRxBus;
    private static Map<Class<?>, Map<Class<?>, Disposable>> mDisposableMap = new HashMap<>();
    private final MethodFinder mMethodFinder;

    /**
     * Wraps this Subject and serializes the calls to the onSubscribe, onNext, onError and onComplete methods, making
     * them thread-safe.
     */
    private RxBus() {
        mFlowableProcessor = PublishProcessor.create().toSerialized();
        mMethodFinder = new MethodFinder();
    }

    public static RxBus getDefault() {
        if (mRxBus == null) {
            synchronized (RxBus.class) {
                if (mRxBus == null) {
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }

    public void post(Object obj) {
        if (mFlowableProcessor.hasSubscribers()) {
            mFlowableProcessor.onNext(obj);
        }
    }

    public void register(Object subsciber) {
        Class<?> subsciberClass = subsciber.getClass();
        List<SubscriberMethod> subscriberMethods = mMethodFinder.findMehod(subsciberClass);
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            addSubscriber(subsciber, subscriberMethod);
        }
    }

    private void addSubscriber(final Object subsciber, final SubscriberMethod subscriberMethod) {
        Class<?> subsciberClass = subsciber.getClass();
        Class<?> eventType = subscriberMethod.getEventType();
        Scheduler threadMode = getThreadMode(subscriberMethod.getThreadMode());
        Disposable disposable = mFlowableProcessor.ofType(eventType).observeOn(threadMode).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                invokeMethod(subsciber, subscriberMethod, o);
            }
        });
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subsciberClass);
        if (disposableMap == null) {
            disposableMap = new HashMap<>();
            mDisposableMap.put(subsciberClass, disposableMap);
        }
        disposableMap.put(eventType, disposable);
    }

    private Scheduler getThreadMode(ThreadMode threadMode) {
        Scheduler scheduler;
        switch (threadMode) {
            case MAIN:
                scheduler = AndroidSchedulers.mainThread();
                break;
            case IO:
                scheduler = Schedulers.io();
                break;
            case COMPUTATION:
                scheduler = Schedulers.computation();
                break;
            case SINGLE:
                scheduler = Schedulers.single();
                break;
            case TRAMPOLINE:
                scheduler = Schedulers.trampoline();
                break;
            case NEW_THREAD:
                scheduler = Schedulers.newThread();
                break;
            default:
                scheduler = AndroidSchedulers.mainThread();
                break;
        }
        return scheduler;
    }

    private void invokeMethod(Object subsciber, SubscriberMethod subscriberMethod, Object obj) {
        try {
            subscriberMethod.method.invoke(subsciber, obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void unRegister(Object subscriber, Class<?> eventType) {
        Class<?> subscriberClass = subscriber.getClass();
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subscriberClass);
        if (disposableMap == null) {
            throw new IllegalArgumentException(subscriberClass.getSimpleName() + " haven't registered RxBus");
        }
        if (!disposableMap.containsKey(eventType)) {
            throw new IllegalArgumentException("The event with type of " + subscriberClass.getSimpleName() + " is not" +
                    " required in " + subscriberClass.getSimpleName());
        }
        Disposable disposable = disposableMap.get(eventType);
        disposable.dispose();
        mDisposableMap.remove(eventType);
    }

    public void unRegister(Object subscriber) {
        Class<?> subscriberClass = subscriber.getClass();
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subscriberClass);
        if (disposableMap == null) {
            throw new IllegalArgumentException(subscriberClass.getSimpleName() + " haven't register RxBus");
        }
        Set<Class<?>> keySet = disposableMap.keySet();
        for (Class<?> evenType : keySet) {
            Disposable disposable = disposableMap.get(evenType);
            disposable.dispose();
        }
        mDisposableMap.remove(subscriberClass);
    }
}
