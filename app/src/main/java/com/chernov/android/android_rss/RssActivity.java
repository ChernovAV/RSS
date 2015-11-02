package com.chernov.android.android_rss;

import android.support.v4.app.Fragment;

public class RssActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        return new RssFragment();
    }
}
