package com.example.parentalcontrolapp;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasUsageStatsPermission()) {
            Toast.makeText(this, "Please grant Usage Access Permission", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        } else {
            startService(new Intent(this, AppBlockerService.class));
        }
    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        }
        int mode = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasUsageStatsPermission()) {
            startService(new Intent(this, AppBlockerService.class));
        }
    }
}
