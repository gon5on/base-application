package jp.co.e2.baseapplication.common;

import android.net.Uri;
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
    public static void v(String tag, String value) {
        verboseLog(tag, value);
    }

    /**
     * debugログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void d(String tag, String value) {
        debugLog(tag, value);
    }

    /**
     * infoログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void i(String tag, String value) {
        infoLog(tag, value);
    }

    /**
     * warnログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void w(String tag, String value) {
        warnLog(tag, value);
    }

    /**
     * errorログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void e(String tag, String value) {
        errorLog(tag, value);
    }

    /**
     * verboseログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void v(String value) {
        verboseLog(TAG, value);
    }

    /**
     * debugログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void d(String value) {
        debugLog(TAG, value);
    }

    /**
     * infoログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void i(String value) {
        infoLog(TAG, value);
    }

    /**
     * warnログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void w(String value) {
        warnLog(TAG, value);
    }

    /**
     * errorログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void e(String value) {
        errorLog(TAG, value);
    }





    /**
     * verboseログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void v(String tag, Integer value) {
        verboseLog(tag, value);
    }

    /**
     * debugログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void d(String tag, Integer value) {
        debugLog(tag, value);
    }

    /**
     * infoログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void i(String tag, Integer value) {
        infoLog(tag, value);
    }

    /**
     * warnログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void w(String tag, Integer value) {
        warnLog(tag, value);
    }

    /**
     * errorログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void e(String tag, Integer value) {
        errorLog(tag, value);
    }

    /**
     * verboseログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void v(Integer value) {
        verboseLog(TAG, value);
    }

    /**
     * debugログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void d(Integer value) {
        debugLog(TAG, value);
    }

    /**
     * infoログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void i(Integer value) {
        infoLog(TAG, value);
    }

    /**
     * warnログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void w(Integer value) {
        warnLog(TAG, value);
    }

    /**
     * errorログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void e(Integer value) {
        errorLog(TAG, value);
    }






    /**
     * verboseログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void v(String tag, Boolean value) {
        verboseLog(tag, value);
    }

    /**
     * debugログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void d(String tag, Boolean value) {
        debugLog(tag, value);
    }

    /**
     * infoログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void i(String tag, Boolean value) {
        infoLog(tag, value);
    }

    /**
     * warnログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void w(String tag, Boolean value) {
        warnLog(tag, value);
    }

    /**
     * errorログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void e(String tag, Boolean value) {
        errorLog(tag, value);
    }

    /**
     * verboseログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void v(Boolean value) {
        verboseLog(TAG, value);
    }

    /**
     * debugログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void d(Boolean value) {
        debugLog(TAG, value);
    }

    /**
     * infoログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void i(Boolean value) {
        infoLog(TAG, value);
    }

    /**
     * warnログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void w(Boolean value) {
        warnLog(TAG, value);
    }

    /**
     * errorログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void e(Boolean value) {
        errorLog(TAG, value);
    }





    /**
     * verboseログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void v(String tag, long value) {
        verboseLog(tag, value);
    }

    /**
     * debugログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void d(String tag, long value) {
        debugLog(tag, value);
    }

    /**
     * infoログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void i(String tag, long value) {
        infoLog(tag, value);
    }

    /**
     * warnログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void w(String tag, long value) {
        warnLog(tag, value);
    }

    /**
     * errorログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void e(String tag, long value) {
        errorLog(tag, value);
    }

    /**
     * verboseログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void v(long value) {
        verboseLog(TAG, value);
    }

    /**
     * debugログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void d(long value) {
        debugLog(TAG, value);
    }

    /**
     * infoログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void i(long value) {
        infoLog(TAG, value);
    }

    /**
     * warnログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void w(long value) {
        warnLog(TAG, value);
    }

    /**
     * errorログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void e(long value) {
        errorLog(TAG, value);
    }






    /**
     * verboseログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void v(String tag, Double value) {
        verboseLog(tag, value);
    }

    /**
     * debugログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void d(String tag, Double value) {
        debugLog(tag, value);
    }

    /**
     * infoログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void i(String tag, Double value) {
        infoLog(tag, value);
    }

    /**
     * warnログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void w(String tag, Double value) {
        warnLog(tag, value);
    }

    /**
     * errorログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void e(String tag, Double value) {
        errorLog(tag, value);
    }

    /**
     * verboseログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void v(Double value) {
        verboseLog(TAG, value);
    }

    /**
     * debugログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void d(Double value) {
        debugLog(TAG, value);
    }

    /**
     * infoログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void i(Double value) {
        infoLog(TAG, value);
    }

    /**
     * warnログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void w(Double value) {
        warnLog(TAG, value);
    }

    /**
     * errorログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void e(Double value) {
        errorLog(TAG, value);
    }






    /**
     * verboseログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void v(String tag, float value) {
        verboseLog(tag, value);
    }

    /**
     * debugログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void d(String tag, float value) {
        debugLog(tag, value);
    }

    /**
     * infoログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void i(String tag, float value) {
        infoLog(tag, value);
    }

    /**
     * warnログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void w(String tag, float value) {
        warnLog(tag, value);
    }

    /**
     * errorログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void e(String tag, float value) {
        errorLog(tag, value);
    }

    /**
     * verboseログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void v(float value) {
        verboseLog(TAG, value);
    }

    /**
     * debugログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void d(float value) {
        debugLog(TAG, value);
    }

    /**
     * infoログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void i(float value) {
        infoLog(TAG, value);
    }

    /**
     * warnログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void w(float value) {
        warnLog(TAG, value);
    }

    /**
     * errorログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void e(float value) {
        errorLog(TAG, value);
    }






    /**
     * verboseログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void v(String tag, Uri value) {
        verboseLog(tag, value);
    }

    /**
     * debugログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void d(String tag, Uri value) {
        debugLog(tag, value);
    }

    /**
     * infoログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void i(String tag, Uri value) {
        infoLog(tag, value);
    }

    /**
     * warnログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void w(String tag, Uri value) {
        warnLog(tag, value);
    }

    /**
     * errorログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void e(String tag, Uri value) {
        errorLog(tag, value);
    }

    /**
     * verboseログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void v(Uri value) {
        verboseLog(TAG, value);
    }

    /**
     * debugログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void d(Uri value) {
        debugLog(TAG, value);
    }

    /**
     * infoログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void i(Uri value) {
        infoLog(TAG, value);
    }

    /**
     * warnログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void w(Uri value) {
        warnLog(TAG, value);
    }

    /**
     * errorログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void e(Uri value) {
        errorLog(TAG, value);
    }






    /**
     * verboseログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void v(String tag, CharSequence value) {
        verboseLog(tag, value);
    }

    /**
     * debugログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void d(String tag, CharSequence value) {
        debugLog(tag, value);
    }

    /**
     * infoログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void i(String tag, CharSequence value) {
        infoLog(tag, value);
    }

    /**
     * warnログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void w(String tag, CharSequence value) {
        warnLog(tag, value);
    }

    /**
     * errorログ
     *
     * @param tag タグ
     * @param value 値
     */
    public static void e(String tag, CharSequence value) {
        errorLog(tag, value);
    }

    /**
     * verboseログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void v(CharSequence value) {
        verboseLog(TAG, value);
    }

    /**
     * debugログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void d(CharSequence value) {
        debugLog(TAG, value);
    }

    /**
     * infoログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void i(CharSequence value) {
        infoLog(TAG, value);
    }

    /**
     * warnログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void w(CharSequence value) {
        warnLog(TAG, value);
    }

    /**
     * errorログ（タグ固定ver）
     *
     * @param value 値
     */
    public static void e(CharSequence value) {
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
