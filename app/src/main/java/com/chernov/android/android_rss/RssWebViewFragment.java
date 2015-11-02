package com.chernov.android.android_rss;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RssWebViewFragment extends Fragment implements RssActivity.OnBackPressedListener {

    private WebView webView;
    ProgressBar progressBar;
    String url;
    TextView titleText;

    public RssWebViewFragment(String url) {
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // при смене ориентации экрана фрагмент сохраняет свое состояние. onDestroy не вызывается
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        View v = inflater.inflate(R.layout.web_fragment, container, false);

        if (instance == null){
            setComponents(v);
            // Initialize the WebView
            webView.getSettings().setSupportZoom(true);
            // zoom - touch, button
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.setWebViewClient(new myWebClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
        }

        return v;
    }

    private class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            titleText.setText(url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void setComponents(View v) {
        webView = (WebView) v.findViewById(R.id.webView);
        progressBar = (ProgressBar)v.findViewById(R.id.progressBar);
        titleText = (TextView)v.findViewById(R.id.titleTextView);
    }

    private void backList() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new RssFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else
            backList();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        if(webView!=null) {
            webView.clearHistory();
            webView.clearCache(true);
        }
    }
}
