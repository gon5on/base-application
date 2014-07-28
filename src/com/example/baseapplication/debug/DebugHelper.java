package com.example.baseapplication.debug;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import android.app.FragmentManager;
import android.content.Context;

import com.example.baseapplication.common.AndroidUtils;
import com.example.baseapplication.common.DateUtils;
import com.example.baseapplication.common.PrefarenceUtils;

/**
 * 例外をキャッチしてメールで報告するクラス
 * 
 * setUncaughtExHandlerメソッドとshowReportDialogメソッドを
 * アクテビティから呼んであげると、通常キャッチできない例外をハンドリングして、メールで送信することができる
 * 
 * 意図的にキャッチした例外もメールで送信したい場合は、すべてのcatch節でsaveメソッドを呼んでおけばOK
 * 
 * @access public
 */
public class DebugHelper
{
    public static final Integer DEBUG_FLG = 0;                  //このフラグで例外を報告するかどうか決められる、リリース時には0にすること

    public static final String KEY = "exception";               //一時的にプリファレンスに例外を保存しておくための名前
    public static final String SUBJECT = "例外報告";             //例外報告メールタイトル
    public static final String[] TO = { "xxxx@xxx.com" };       //例外報告メール送信先;

    /**
     * 例外をハンドリングする処理を仕込む
     * 
     * @param Context context
     * @return void
     * @access public
     */
    public static void setUncaughtExHandler(Context context)
    {
        if (DEBUG_FLG != 1) {
            return;
        }

        AppUncaughtExHandler appUncaughtExHandler = new AppUncaughtExHandler(context);
        Thread.setDefaultUncaughtExceptionHandler(appUncaughtExHandler);
    }

    /**
     * プリファレンスに例外が保存してあれば、メールで内容を送信する
     * 
     * @param Context context
     * @param FragmentManeger fragmentManeger
     * @return void
     * @access public
     */
    public static void showReportDialog(Context context, FragmentManager fragmentManeger)
    {
        if (DEBUG_FLG != 1) {
            return;
        }

        String exStr = PrefarenceUtils.get(context, DebugHelper.KEY, "");

        if (exStr.equals("") == false) {
            DebugReportDialog debugReportDialog = new DebugReportDialog();
            debugReportDialog.show(fragmentManeger, "dialog");
        }
    }

    /**
     * 例外を受け取って、プリファレンスに保存する
     * 
     * @param Context context
     * @param Exception e
     * @return void
     * @access public
     */
    public static void save(Context context, Throwable e)
    {
        if (DEBUG_FLG != 1) {
            return;
        }

        String text = DebugHelper.createExDebugText(context, e);

        savePrefarence(context, text);
    }

    /**
     * 例外を受け取って、プリファレンスに保存する
     * 
     * @param Context context
     * @param Exception e
     * @return void
     * @access public
     */
    public static void save(Context context, Exception e)
    {
        if (DEBUG_FLG != 1) {
            return;
        }

        String text = DebugHelper.createExDebugText(context, e);

        savePrefarence(context, text);
    }

    /**
     * 例外と配列を受け取って、プリファレンスに保存する
     * 
     * @param Context context
     * @param Exception e
     * @param HashMap<String, String> map
     * @return void
     * @access public
     */
    public static void save(Context context, Exception e, HashMap<String, String> map)
    {
        if (DEBUG_FLG != 1) {
            return;
        }

        String text = DebugHelper.createExDebugText(context, e, map);

        savePrefarence(context, text);
    }

    ////////////////////////////////////////////////////////////////////以下、private

    /**
     * 文字列をプリファレンスに保存する
     * 
     * @param Context context
     * @param String text
     * @return void
     * @access private
     */
    private static void savePrefarence(Context context, String text)
    {
        String savedText = PrefarenceUtils.get(context, DebugHelper.KEY, "");
        PrefarenceUtils.save(context, KEY, (savedText + text));
    }

    /**
     * 例外の内容を文字列にして、日時などを他情報も付与する
     * 
     * @param Context context
     * @param Exception e 例外
     * @return String
     * @access private
     */
    private static String createExDebugText(Context context, Exception e)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にして、日時などを他情報も付与する
     * 
     * @param Context context
     * @param Throwable e 例外
     * @return String
     * @access private
     */
    private static String createExDebugText(Context context, Throwable e)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にして、日時などを他情報も付与する
     * 
     * @param Context context
     * @param Exception e 例外
     * @param HashMap<String, String> map 追加したい項目の配列
     * @return String
     * @access private
     */
    private static String createExDebugText(Context context, Exception e, HashMap<String, String> map)
    {
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にする
     * 
     * @param Exception e 例外
     * @return String
     * @access private
     */
    private static String exToString(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();

        return sw.toString();
    }

    /**
     * 例外の内容を文字列にする
     * 
     * @param Throwable e 例外
     * @return String
     * @access private
     */
    private static String exToString(Throwable e)
    {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }

    /**
     * デバッグ用の例外文言を生成する
     * 
     * 日付、アプリバージョン、アプリバージョンコード、OSバージョン、モデル名は自動で付与
     * 
     * @param Context context
     * @param HashMap<String, String> map
     * @return String
     * @access private
     */
    private static String createExDebugText(Context context, HashMap<String, String> map)
    {
        String date = new DateUtils().format(DateUtils.FMT_DATETIME);

        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------------\n");
        sb.append("date = " + date + "\n");
        sb.append("---------------------------------------\n");
        sb.append("app ver = " + AndroidUtils.getVerName(context) + "\n");
        sb.append("---------------------------------------\n");
        sb.append("app code = " + AndroidUtils.getVerCode(context) + "\n");
        sb.append("---------------------------------------\n");
        sb.append("os ver = " + AndroidUtils.getOsVer() + "\n");
        sb.append("---------------------------------------\n");
        sb.append("model = " + AndroidUtils.getModel() + "\n");
        sb.append("---------------------------------------\n");

        for (String key : map.keySet()) {
            sb.append(key + " = " + map.get(key) + "\n");
            sb.append("---------------------------------------\n");
        }

        sb.append("\n\n\n");

        return sb.toString();
    }
}