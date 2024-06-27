package com.example.parentalcontrolapp;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {
    private static final String[] BLOCKED_WEBSITES = {"carfiq.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isBlockedUrl(url)) {
                    view.loadUrl("about:blank");
                }
            }
        });

        webView.loadUrl("https://www.google.com");
    }

    private boolean isBlockedUrl(String url) {
        for (String blockedWebsite : BLOCKED_WEBSITES) {
            if (url.contains(blockedWebsite)) {
                return true;
            }
        }
        return false;
    }
}
