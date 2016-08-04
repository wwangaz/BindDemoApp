package com.wangweimin.binddemoapp.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;

import java.util.Iterator;

/**
 * Created by wangweimin on 16/8/2.
 */

public class MAppInfoManager {
    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取应用程序版本名称
     * @param context
     * @return 当前应用版本名称
     */
    public static String getVersionName(Context context){
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取应用程序版本号
     * @param context
     * @return 当前应用版本号
     */
    public static int getVersionCode(Context context){
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取当前进程名称
     * @param context
     * @return 当前进程名称
     */
    public static String getProcessName(Context context){
        int pid = Process.myPid();
        Iterator localIterator = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses().iterator();
        while (localIterator.hasNext()){
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) localIterator.next();
            if(runningAppProcessInfo.pid == pid)
                return runningAppProcessInfo.processName;
        }
        return null;
    }
}

