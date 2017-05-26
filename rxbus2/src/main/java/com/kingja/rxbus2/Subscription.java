package com.kingja.rxbus2;

/**
 * Description:TODO
 * Create Time:2017/4/13 15:56
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class Subscription {
    final Object subscriber;
    final SubscriberMethod subscriberMethod;

    Subscription(Object subscriber, SubscriberMethod subscriberMethod) {
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
    }
}
