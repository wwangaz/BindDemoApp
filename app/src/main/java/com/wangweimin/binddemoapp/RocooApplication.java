package com.wangweimin.binddemoapp;

import android.app.Application;
import android.content.Context;

import com.wangweimin.binddemoapp.hotfix.HotFixManager;
import com.wangweimin.binddemoapp.util.MAppManager;

/**
 * Created by wangweimin on 16/7/18.
 */
public class RocooApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MAppManager.init(this);
        HotFixManager manager = new HotFixManager(base);
//        manager.installPatch();
    }
}
