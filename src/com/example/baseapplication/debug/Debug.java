package com.example.baseapplication.debug;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import android.app.FragmentManager;
import android.content.Context;

import com.example.baseapplication.common.AndroidUtils;
import com.example.baseapplication.common.DateUtils;
import com.example.baseapplication.common.PrefarenceUtils;

public class Debug
{
    public static final Integer DEBUG_FLG = 1;                  //このフラグで例外を報告するかどうか決められる、リリース時には0にすること

    public static final String KEY = "exception";               //一時的にプリファレンスに例外を保存しておくための名前
    public static final String SUBJECT = "例外報告";             //例外報告メールタイトル
    public static final String[] TO = { "xxxx@xxx.com" };       //例外報告メール送信先;

    /**
     * 例外が保存してあれば、メールで内容を送信する
     * 
     * @param Context context
     * @param FragmentManeger fragmentManeger
     * @return void
     * @access private
     */
    public static void createExDebugText(Context context, FragmentManager fragmentManeger)
    {
        if (DEBUG_FLG != 1) {
            return;
        }

        String exStr = PrefarenceUtils.get(context, Debug.KEY, "");

        if (exStr.equals("") == false) {
            DebugReportDialog debugReportDialog = new DebugReportDialog();
            debugReportDialog.show(fragmentManeger, "dialog");
        }
    }

    /**
     * 例外の内容を文字列にする
     * 
     * @param Context context
     * @param Exception e 例外
     * @return void
     * @access private
     */
    public static String createExDebugText(Context context, Exception e)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にする
     * 
     * @param Context context
     * @param Throwable e 例外
     * @return void
     * @access private
     */
    public static String createExDebugText(Context context, Throwable e)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にする
     * 
     * @param Context context
     * @param Exception e 例外
     * @param HashMap<String, String> map 出力追加したい項目の配列
     * @return void
     * @access private
     */
    public static String createExDebugText(Context context, Exception e, HashMap<String, String> map)
    {
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にする
     * 
     * @param Context context
     * @param Throwable e 例外
     * @param HashMap<String, String> map 出力追加したい項目の配列
     * @return void
     * @access private
     */
    public static String createExDebugText(Context context, Throwable e, HashMap<String, String> map)
    {
        map.put("exception", exToString(e));

        return createExDebugText(context, map);
    }

    /**
     * 例外の内容を文字列にする
     * 
     * @param Exception e 例外
     * @return void
     * @access private
     */
    public static String exToString(Exception e)
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
     * @return void
     * @access private
     */
    public static String exToString(Throwable e)
    {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }

    /**
     * デバッグ用の例外文言を生成する
     * 
     * @param Context context
     * @param HashMap<String, String> map
     * @return String
     * @access public
     */
    private static String createExDebugText(Context context, HashMap<String, String> map)
    {
        String date = new DateUtils().format(DateUtils.FMT_DATETIME);

        String text = "";
        text += "---------------------------------------\n";
        text += "date = " + date + "\n";
        text += "---------------------------------------\n";
        text += "app ver = " + AndroidUtils.getVerName(context) + "\n";
        text += "---------------------------------------\n";
        text += "app code = " + AndroidUtils.getVerCode(context) + "\n";
        text += "---------------------------------------\n";
        text += "os ver = " + AndroidUtils.getOsVer() + "\n";
        text += "---------------------------------------\n";
        text += "model = " + AndroidUtils.getModel() + "\n";
        text += "---------------------------------------\n";

        for (String key : map.keySet()) {
            text += key + " = " + map.get(key) + "\n";
            text += "---------------------------------------\n";
        }

        return text;
    }
}
