package com.wangweimin.binddemoapp.util;

/**
 * Created by wangweimin on 16/8/2.
 */

public class MExceptionManager {
    public static void throwApplicationInitEx(String msg){
        throw new MException(msg);
    }
}
