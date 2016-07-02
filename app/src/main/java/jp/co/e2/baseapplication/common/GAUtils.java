package jp.co.e2.baseapplication.common;

import android.app.Application;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

/**
 * GoogleAnalyticsログ送信クラス
 */
public class GAUtils {
    /**
     * スクリーンビューを送る
     *
     * savedInstanceStateがnullの場合のみ、送信するようにしないと、
     * 画面の再生成が走っただけでもログが送られてしまうので注意すること
     *
     * @param application アプリケーションクラス
     * @param screenName スクリーン名
     */
    public static void sendScreen(Application application, String screenName) {
        Tracker tracker = ((AppApplication)application).getTracker();

        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * イベントを送る
     *
     * @param application アプリケーションクラス
     * @param category カテゴリ
     */
    public static void sendEvent(Application application, String category) {
        Tracker tracker = ((AppApplication)application).getTracker();

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .build());
    }

    /**
     * イベントを送る
     *
     * @param application GoogleAnalyticsアプリケーションクラス
     * @param category カテゴリ
     * @param action アクション
     */
    public static void sendEvent(Application application, String category, String action) {
        Tracker tracker = ((AppApplication)application).getTracker();

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }

    /**
     * イベントを送る
     *
     * @param application アプリケーションクラス
     * @param category カテゴリ
     * @param action アクション
     * @param label ラベル
     */
    public static void sendEvent(Application application, String category, String action, String label) {
        Tracker tracker = ((AppApplication)application).getTracker();

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    /**
     * イベントを送る
     *
     * @param application アプリケーションクラス
     * @param category カテゴリ
     * @param action アクション
     * @param label ラベル
     * @param value 値
     */
    public static void sendEvent(Application application, String category, String action, String label, long value) {
        Tracker tracker = ((AppApplication)application).getTracker();

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

    /**
     * 例外を送る
     *
     * @param application アプリケーションクラス
     * @param e 例外
     * @param fatalFlg 致命的かどうかフラグ
     */
    public static void sendEvent(Application application, Exception e, boolean fatalFlg) {
        Tracker tracker = ((AppApplication)application).getTracker();

        tracker.send(new HitBuilders.ExceptionBuilder()
                .setDescription(new StandardExceptionParser(application.getApplicationContext(), null)
                        .getDescription(Thread.currentThread().getName(), e))
                .setFatal(fatalFlg)
                .build());
    }
}
