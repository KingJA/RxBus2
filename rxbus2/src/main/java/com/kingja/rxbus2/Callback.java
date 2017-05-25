package com.kingja.rxbus2;

/**
 * Description:TODO
 * Create Time:2017/5/25 16:07
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public interface Callback<T> {
    void onReceive(T t);
}
