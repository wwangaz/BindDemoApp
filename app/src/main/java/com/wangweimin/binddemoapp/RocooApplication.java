package com.wangweimin.binddemoapp;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.dodola.rocoofix.RocooFix;
import com.wangweimin.binddemoapp.util.MAppManager;

import java.io.File;

/**
 * Created by wangweimin on 16/7/18.
 */
public class RocooApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MAppManager.init(this);
        HotFixManager manager = new HotFixManager(base);
        manager.installPatch();
    }
}
