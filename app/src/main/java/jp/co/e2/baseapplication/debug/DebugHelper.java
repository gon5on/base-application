package jp.co.e2.baseapplication.debug;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.DateHelper;
import jp.co.e2.baseapplication.config.EnvConfig;

/**
 * 例外をキャッチしてメールで報告するクラス
 *
 * setUncaughtExHandlerメソッドとshowReportDialogメソッドを
 * アクテビティから呼んであげると、通常キャッチできない例外をハンドリングして、メールで送信することができる
 *
 * 意図的にキャッチした例外もメールで送信したい場合は、すべてのcatch節でsaveメソッドを呼んでおけばOK
 */
public class DebugHelper {
    public static final boolean DEBUG_FLG = EnvConfig.DEBUG_FLG;    //このフラグで例外を報告するかどうか決められる、リリース時には0にすること

    public static final String KEY = "exception";                   //一時的にプリファレンスに例外を保存しておくための名前
    public static final String SUBJECT = "例外報告";                 //例外報告メールタイトル
    public static final String[] TO = {"tamura@e-2.co.jp"};         //例外報告メール送信先

    /**
     * 例外をハンドリングする処理を仕込む
     *
     * @param context コンテキスト
     */
    public static void setUncaughtExHandler(Context context) {
        if (DEBUG_FLG) {
            UncaughtExHandler uncaughtExHandler = new UncaughtExHandler(context);
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExHandler);
        }
    }

    /**
     * プリファレンスに例外が保存してあれば、メールで内容を送信する
     *
     * @param context コンテキスト
     * @param fragmentManager フラグメントマネージャ
     */
    public static void showReportDialog(Context context, FragmentManager fragmentManager) {
        if (DEBUG_FLG) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            String exStr = sp.getString(DebugHelper.KEY, "");

            if (!exStr.equals("")) {
                DebugReportDialog debugReportDialog = DebugReportDialog.getInstance();
                debugReportDialog.show(fragmentManager, "dialog");
            }
        }
    }

    /**
     * 例外を受け取って、プリファレンスに保存する
     *
     * @param context コンテキスト
     * @param e 例外
     */
    public static void save(Context context, Throwable e) {
        if (DEBUG_FLG) {
            String text = DebugHelper.createExDebugText(context, e);

            savePreference(context, text);
        }
    }

    /**
     * 例外を受け取って、プリファレンスに保存する
     *
     * @param context コンテキスト
     * @param e 例外
     */
    public static void save(Context context, Exception e) {
        if (DEBUG_FLG) {
            String text = DebugHelper.createExDebugText(context, e);

            savePreference(context, text);
        }
    }

    /**
     * 例外と配列を受け取って、プリファレンスに保存する
     *
     * @param context コンテキスト
     * @param e 例外
     * @param map 例外以外に格納したい情報配列
     */
    public static void save(Context context, Exception e, LinkedHashMap<String, String> map) {
        if (DEBUG_FLG) {
            String text = DebugHelper.createExDebugText(context, e, map);

            savePreference(context, text);
        }
    }

    ////////////////////////////////////////////////////////////////////以下、private

    /**
     * 文字列をプリファレンスに保存する
     *
     * @param context コンテキスト
     * @param text テキスト
     */
    private static void savePreference(Context context, String text) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String savedText = sp.getString(DebugHelper.KEY, "");

        sp.edit().putString(DebugHelper.KEY, (savedText + text)).apply();
    }

    /**
     * 例外の内容を文字列にして、日時などを他情報も付与する
     *
     * @param context コンテキスト
     * @param e       例外
     * @return String
     */
    private static String createExDebugText(Context context, Exception e) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にして、日時などを他情報も付与する
     *
     * @param context コンテキスト
     * @param e       例外
     * @return String
     */
    private static String createExDebugText(Context context, Throwable e) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にして、日時などを他情報も付与する
     *
     * @param context コンテキスト
     * @param e       例外
     * @param map     追加したい項目の配列
     * @return String
     */
    private static String createExDebugText(Context context, Exception e, LinkedHashMap<String, String> map) {
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にする
     *
     * @param e 例外
     * @return String
     */
    private static String exToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();

        return sw.toString();
    }

    /**
     * 例外の内容を文字列にする
     *
     * @param e 例外
     * @return String
     */
    private static String exToString(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }

    /**
     * デバッグ用の例外文言を生成する
     *
     * 日付、アプリバージョン、アプリバージョンコード、OSバージョン、モデル名は自動で付与
     *
     * @param context コンテキスト
     * @param data 配列
     * @return String
     */
    private static String createExDebugText(Context context, LinkedHashMap<String, String> data) {
        String date = new DateHelper().format(DateHelper.FMT_DATETIME);

        String text = "---------------------------------------\n";
        text += "date = " + date + "\n";
        text += "---------------------------------------\n";
        text += "package = " + context.getPackageName() + "\n";
        text += "---------------------------------------\n";
        text += "app ver = " + AndroidUtils.getVerName(context) + "\n";
        text += "---------------------------------------\n";
        text += "app code = " + AndroidUtils.getVerCode(context) + "\n";
        text += "---------------------------------------\n";
        text += "os ver = " + Build.VERSION.RELEASE + "\n";
        text += "---------------------------------------\n";
        text += "model = " + Build.MODEL + "\n";
        text += "---------------------------------------\n";

        for (String key : data.keySet()) {
            text += key + " = " + data.get(key) + "\n";
            text += "---------------------------------------\n";
        }

        text += "\n\n\n";

        return text;
    }
}