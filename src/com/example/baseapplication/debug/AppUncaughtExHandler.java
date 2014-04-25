package com.example.baseapplication.debug;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

import com.example.baseapplication.common.PrefarenceUtils;

/**
 * UncaughtExceptionハンドリングクラス
 * 
 * @access public
 */
public class AppUncaughtExHandler implements UncaughtExceptionHandler
{
    private Context mContext;
    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    /**
     * コンストラクタ
     * 
     * @param Context context
     * @access public
     */
    public AppUncaughtExHandler(Context context)
    {
        mContext = context;

        // デフォルト例外ハンドラを保持する。
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * uncaughtException
     * 
     * @param Thread thread
     * @param Throwable ex
     * @return void
     * @access public
     */
    @Override
    public void uncaughtException(Thread thread, Throwable e)
    {
        // プリファレンスから保存済の値を取得
        String text = PrefarenceUtils.get(mContext, Debug.KEY, "");

        // デバッグ用の文言を生成して追加
        text += Debug.createExDebugText(mContext, e);

        // プリファレンスに保存
        PrefarenceUtils.save(mContext, Debug.KEY, text);

        // デフォルト例外ハンドラを戻す
        mDefaultUncaughtExceptionHandler.uncaughtException(thread, e);
    }
}
