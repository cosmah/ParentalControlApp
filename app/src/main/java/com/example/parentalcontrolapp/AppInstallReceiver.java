package com.example.parentalcontrolapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Build;

import java.util.Objects;

public class AppInstallReceiver extends BroadcastReceiver {

    private static final String[] BLOCKED_APPS = {"com.zhiliaoapp.musically"};

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_PACKAGE_ADDED.equals(action) ||
            Intent.ACTION_PACKAGE_CHANGED.equals(action) ||
            Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
            String packageName = Objects.requireNonNull(intent.getData()).getEncodedSchemeSpecificPart();
            for (String blockedApp : BLOCKED_APPS) {
                if (packageName.equals(blockedApp)) {
                    PackageInstaller packageInstaller = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        packageInstaller = context.getPackageManager().getPackageInstaller();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        packageInstaller.uninstall(packageName, null);
                    }
                }
            }
        }
    }
}
