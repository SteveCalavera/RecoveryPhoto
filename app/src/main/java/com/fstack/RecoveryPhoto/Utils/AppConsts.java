package com.fstack.RecoveryPhoto.Utils;

import android.Manifest;

/**
 * Created by IQVIS on 3/19/2017.
 */
public class AppConsts {
    public static final int requestForPermission=0;
    public static final String [] PERMISSIONS={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };
}
