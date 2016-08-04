package com.wangweimin.binddemoapp.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.provider.Settings;

/**
 * Created by wangweimin on 16/8/2.
 */

public class MAppManager {
    public static Application globalApplication;

    public static void init(Application application) {
        globalApplication = application;
    }

    public static Application getApplication(){
        if(globalApplication == null)
            MExceptionManager.throwApplicationInitEx("please init application");

        return globalApplication;
    }

    public static void AppExit(Context context){
        try{
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(context.getPackageName());
            globalApplication = null;
            System.exit(0);
        }catch (Exception e){
            System.exit(0);
        }
    }
}
