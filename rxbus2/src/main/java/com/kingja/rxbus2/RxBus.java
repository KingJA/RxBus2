package com.kingja.rxbus2;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

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
    private static Map<String, Disposable> mDisposableMap = new HashMap<String, Disposable>();

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

    public <T> void register(Context context, Class<T> eventType, final Callback<T> callback) {
        register(context, eventType, callback, AndroidSchedulers.mainThread());
    }

    public <T> void register(Context context, Class<T> eventType, final Callback<T> callback, Scheduler scheduler) {
        Disposable subscribe = mFlowableProcessor.ofType(eventType).observeOn(scheduler).subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                callback.onReceive(t);
            }
        });
        mDisposableMap.put(context.getClass().getSimpleName(), subscribe);

    }

    public void unRegister(Context context) {
        String contextName = context.getClass().getSimpleName();
        Disposable disposable = mDisposableMap.get(contextName);
        if (disposable == null) {
            throw new IllegalArgumentException(contextName + " haven't register RxBus");
        }
        disposable.dispose();
        mDisposableMap.remove(contextName);
    }
}
