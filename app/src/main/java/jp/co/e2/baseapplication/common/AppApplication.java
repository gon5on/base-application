package jp.co.e2.baseapplication.common;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import jp.co.e2.baseapplication.R;

/**
 * アプリ固有のアプリケーションクラス
 */
public class AppApplication extends Application {
    private Tracker mTracker;

    /**
     * ${inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();

        getTracker();
    }

    /**
     * GoogleAnalyticsのトラッカーを取得する
     *
     * @return トラッカー
     */
    public Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.ga_tracker);
        }

        return mTracker;
    }
}

