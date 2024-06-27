package com.example.parentalcontrolapp;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppBlockerService extends Service {
    private static final String[] BLOCKED_APPS = {"com.example.blockedapp1", "com.example.blockedapp2"};

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            while (true) {
                String currentApp = getForegroundApp();
                for (String app : BLOCKED_APPS) {
                    if (app.equals(currentApp)) {
                        Intent launchIntent = new Intent(Intent.ACTION_MAIN);
                        launchIntent.addCategory(Intent.CATEGORY_HOME);
                        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchIntent);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private String getForegroundApp() {
        UsageStatsManager usm = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        }
        long time = System.currentTimeMillis();
        List<UsageStats> appList = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        }
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
            }
            if (!mySortedMap.isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        }
        return "";
    }
}
