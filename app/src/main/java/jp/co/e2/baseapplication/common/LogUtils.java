package jp.co.e2.baseapplication.common;

import android.util.Log;

import jp.co.e2.baseapplication.config.EnvConfig;

/**
 * ログのラッパークラス
 *
 * 出力するしないをフラグで切り替え可能
 */
public class LogUtils {
    private static final String TAG = "####";
    private static final String NULL = "null";
    private static final boolean LOG_FLG = EnvConfig.LOG_FLG;   //このフラグでログを出力するかどうかを決められる、リリース時は0にすること

    /**
     * verboseログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void v(String tag, Object value) {
        verboseLog(tag, value);
    }

    /**
     * debugログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void d(String tag, Object value) {
        debugLog(tag, value);
    }

    /**
     * infoログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void i(String tag, Object value) {
        infoLog(tag, value);
    }

    /**
     * warnログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void w(String tag, Object value) {
        warnLog(tag, value);
    }

    /**
     * errorログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void e(String tag, Object value) {
        errorLog(tag, value);
    }

    /**
     * verboseログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void v(Object value) {
        verboseLog(TAG, value);
    }

    /**
     * debugログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void d(Object value) {
        debugLog(TAG, value);
    }

    /**
     * infoログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void i(Object value) {
        infoLog(TAG, value);
    }

    /**
     * warnログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void w(Object value) {
        warnLog(TAG, value);
    }

    /**
     * errorログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void e(Object value) {
        errorLog(TAG, value);
    }



    /**
     * verboseログを出力する
     *
     * @param tag タグ
     * @param value 値
     */
    private static void verboseLog(String tag, Object value) {
        if (!LOG_FLG) {
            return;
        }

        if (value == null) {
            Log.v(tag, NULL);
        } else {
            Log.v(tag, String.valueOf(value));
        }
    }

    /**
     * debugログを出力する
     *
     * @param tag タグ
     * @param value 値
     */
    private static void debugLog(String tag, Object value) {
        if (!LOG_FLG) {
            return;
        }

        if (value == null) {
            Log.d(tag, NULL);
        } else {
            Log.d(tag, String.valueOf(value));
        }
    }

    /**
     * infoログを出力する
     *
     * @param tag タグ
     * @param value 値
     */
    private static void infoLog(String tag, Object value) {
        if (!LOG_FLG) {
            return;
        }

        if (value == null) {
            Log.i(tag, NULL);
        } else {
            Log.i(tag, String.valueOf(value));
        }
    }

    /**
     * warmログを出力する
     *
     * @param tag タグ
     * @param value 値
     */
    private static void warnLog(String tag, Object value) {
        if (!LOG_FLG) {
            return;
        }

        if (value == null) {
            Log.w(tag, NULL);
        } else {
            Log.w(tag, String.valueOf(value));
        }
    }

    /**
     * errorログを出力する
     *
     * @param tag タグ
     * @param value 値
     */
    private static void errorLog(String tag, Object value) {
        if (!LOG_FLG) {
            return;
        }

        if (value == null) {
            Log.e(tag, NULL);
        } else {
            Log.e(tag, String.valueOf(value));
        }
    }
}
