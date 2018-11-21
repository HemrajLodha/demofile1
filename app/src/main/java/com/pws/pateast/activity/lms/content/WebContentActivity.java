package com.pws.pateast.activity.lms.content;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pws.pateast.R;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;

/**
 * Created by intel on 29-Jan-18.
 */

public class WebContentActivity extends ResponseAppActivity {
    private WebView webView;
    private Intent intent;
    private Uri uri;
    private String title;
    private int progress;

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_web_content;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        intent = getIntent();
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new InsideWebViewClient());
        webView.setWebChromeClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        title = intent.getStringExtra(Extras.TITLE);
        uri = intent.getData();
        setTitle(title);
        webView.loadUrl(uri.toString());
    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

    private class InsideWebViewClient extends WebViewClient {

        //If you will not use this method url links are opeen in new brower not in webview
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //Show loader on url load
        public void onLoadResource(WebView view, String url) {
            showProgress(progress != 100);
        }

        public void onPageFinished(WebView view, String url) {
            showProgress(false);
        }
    }

    private class MyBrowser extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progress = newProgress;
        }
    }
}
