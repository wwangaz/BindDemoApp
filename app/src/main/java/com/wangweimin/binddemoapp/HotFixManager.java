package com.wangweimin.binddemoapp;

import android.content.Context;

import com.dodola.rocoofix.RocooFix;

/**
 * Created by wangweimin on 16/8/3.
 */

public class HotFixManager {
    private Context mContext;

    private PatchManager mPatchManager;

    /**
     * 初始化变量
     * 调用接口判断是否需要加载补丁
     * 根据patch判断
     */
    public HotFixManager(Context context) {
        mContext = context;
        // TODO: 16/8/4 调用接口 获取Url和md5值

        mPatchManager = new PatchManager(null, null);
    }

    /**
     * 安装补丁包
     */
    public void installPatch() {
        RocooFix.applyPatch(mContext, mPatchManager.getCurrentPatchFile());
    }

    /**
     * 回退上一个补丁包
     */
    public void revertPatch() {
        RocooFix.applyPatch(mContext, mPatchManager.getPrePatchFile());
    }

    /**
     * 禁用热修复
     */
    private void stopPatch() {

    }
}
