package com.wangweimin.binddemoapp;


import android.os.Environment;
import android.util.Log;

import com.wangweimin.binddemoapp.IPatch.IPatchDownloader;
import com.wangweimin.binddemoapp.util.MD5VerifyUtil;

import java.io.File;

import cn.aigestudio.downloader.interfaces.SimpleDListener;

/**
 * Created by wangweimin on 16/8/3.
 */

public class PatchManager {
    private static final String TAG = "PatchManager";

    private String fileName = "patch.jar";

    private File currentPatchFile;

    private File prePatchFile;

    /**
     * 初始化操作
     */
    public PatchManager(String url, final String md5) {
        if (url != null && md5 != null) {
            IPatchDownloader.newInstance(url).downloadRemotePatch(new SimpleDListener() {
                @Override
                public void onFinish(File file) {
                    if (file.exists()) {
                        Log.i(TAG, "补丁下载完毕");
                        if (verifyPatch(file, md5)) {
                            prePatchFile = currentPatchFile;
                            currentPatchFile = file;
                        } else {
                            Log.i(TAG, "补丁校验失败");
                        }
                    }
                }

                @Override
                public void onError(int status, String error) {
                }
            });
        } else {
            String patchFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + fileName;
            File patch = new File(patchFilePath);
            if (patch.exists()) {
                prePatchFile = currentPatchFile;
                currentPatchFile = patch;
            }
        }
    }

    public String getCurrentPatchFile() {
        if (currentPatchFile != null)
            return currentPatchFile.getAbsolutePath();
        return null;
    }

    public String getPrePatchFile() {
        if (prePatchFile != null)
            return prePatchFile.getAbsolutePath();
        return null;
    }

    private boolean verifyPatch(File file, String md5) {
        return MD5VerifyUtil.md5Verify(file, md5);
    }


}
