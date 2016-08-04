package com.wangweimin.binddemoapp;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.wangweimin.binddemoapp.util.MPermissionUtil;

/**
 * Created by wangweimin on 16/8/2.
 */

public class BasePermissionActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MPermissionUtil.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MPermissionUtil.RequestCode){

        }
    }
}
