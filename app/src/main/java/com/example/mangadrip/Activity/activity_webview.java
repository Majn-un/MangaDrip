package com.example.mangadrip.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mangadrip.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.Map;

public class activity_webview extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webview);

        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookies = CookieManager.getInstance().getCookie(view.getUrl());
                Log.d("YuhS",""+cookies);
                Log.d("In","we in");
                // save cookies or call new fun to handle actions
                //  newCookies(cookies);
            }
        };
        webView.setWebViewClient(webViewClient);
        webView.loadUrl("https://www.google.com");


    }
}
