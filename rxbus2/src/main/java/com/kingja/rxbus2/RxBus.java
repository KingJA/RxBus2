package com.kingja.rxbus2;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

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

    /**
     * Wraps this Subject and serializes the calls to the onSubscribe, onNext, onError and onComplete methods, making
     * them thread-safe.
     */
    private RxBus() {
        mFlowableProcessor = PublishProcessor.create().toSerialized();
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

    public <T> void register(Object subscriber, Class<T> eventType, final Callback<T> callback) {
        register(subscriber, eventType, callback, AndroidSchedulers.mainThread());
    }

    public <T> void register(Object subscriber, Class<T> eventType, final Callback<T> callback, Scheduler scheduler) {
        Disposable disposable = mFlowableProcessor.ofType(eventType).observeOn(scheduler).subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                callback.onReceive(t);
            }
        });
        Class<?> subscriberClass = subscriber.getClass();
        Map<Class<?>, Disposable> disposableMap = mDisposableMap.get(subscriberClass);
        if (disposableMap == null) {
            disposableMap = new HashMap<>();
            mDisposableMap.put(subscriberClass, disposableMap);
        }
        disposableMap.put(eventType, disposable);
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
