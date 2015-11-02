package com.chernov.android.android_rss;

import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class RssWebViewFragment extends Fragment implements RssWebViewActivity.OnBackPressedListener {

    private WebView webView;
    String url;
    static final String TAG = "myLog";

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
        Log.e(TAG, "restoreState");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        View v = inflater.inflate(R.layout.web_fragment, container, false);

        if (instance == null){
            Log.e(TAG, "instance == null");
            webView = (WebView) v.findViewById(R.id.webView);

            // Initialize the WebView
            webView.getSettings().setSupportZoom(true);
            // zoom - touch, button
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webView.setScrollbarFadingEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true);

            // Load the URLs inside the WebView, not in the external web browser
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }

        return v;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
        Log.e(TAG, "onSaveInstanceState");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            getActivity().finish();
        }
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
