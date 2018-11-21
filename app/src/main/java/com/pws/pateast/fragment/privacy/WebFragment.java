package com.pws.pateast.fragment.privacy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.FragmentActivity;

import com.pws.pateast.fragment.suggestion.WebPresenter;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.Utils;

/**
 * Created by intel on 19-Aug-17.
 */

public class WebFragment extends AppFragment implements PrivacyWebView {
    public static final int TYPE_PRIVACY = 0, TYPE_TOS = 1;
    private ProgressBar progressBar;
    private WebPresenter webPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_web;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        WebView webView = getView().findViewById(R.id.webView);
        progressBar = getView().findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new MyBrowser());
        int action = getArguments().getInt(FragmentActivity.EXTRA_DATA);

        webPresenter = new WebPresenter();
        webPresenter.attachView(this);

        switch (action) {
            case TYPE_PRIVACY:
                switch (webPresenter.getLanguage()) {
                    case Preference.LANG_EN:
                        webView.loadUrl(ServiceBuilder.PRIVACY_URL_ENGLISH);
                        break;
                    case Preference.LANG_AR:
                        webView.loadUrl(ServiceBuilder.PRIVACY_URL_ARABIC);
                        break;
                }
                setTitle(getString(R.string.menu_privacy_policy));
                break;
            case TYPE_TOS:
                switch (webPresenter.getLanguage()) {
                    case Preference.LANG_EN:
                        webView.loadUrl(ServiceBuilder.TOS_URL_ENGLISH);
                        break;
                    case Preference.LANG_AR:
                        webView.loadUrl(ServiceBuilder.TOS_URL_ARABIC);
                        break;
                }
                setTitle(getString(R.string.menu_terms));
                break;
        }
    }

    @Override
    public void setTitle(String title) {
        getAppListener().setTitle(title);
    }

    private class MyBrowser extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (newProgress >= 100) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
