package com.wangweimin.binddemoapp.IPatch;

import com.wangweimin.binddemoapp.util.MAppManager;

import cn.aigestudio.downloader.bizs.DLManager;
import cn.aigestudio.downloader.interfaces.SimpleDListener;

/**
 * Created by wangweimin on 16/8/3.
 */

public class IPatchDownloader {
    public static final String TAG = "IPatchDownloader";
    private static IPatchDownloader downloader;

    private String downloadUrl;

    private IPatchDownloader(String url) {
        downloadUrl = url;
    }

    public static IPatchDownloader newInstance(String url) {
        downloader = new IPatchDownloader(url);
        return downloader;
    }

    public void downloadRemotePatch(SimpleDListener listener) {
        DLManager.getInstance(MAppManager.getApplication()).dlStart(downloadUrl, listener);
    }
}
