package com.wangweimin.binddemoapp;

/**
 * Created by wangweimin on 16/8/10.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
