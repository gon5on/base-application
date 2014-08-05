package com.example.baseapplication.debug;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

/**
 * UncaughtExceptionハンドリングクラス
 * 
 * @access public
 */
public class UncaughtExHandler implements UncaughtExceptionHandler
{
    private Context mContext;
    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    /**
     * コンストラクタ
     * 
     * @param Context context
     * @access public
     */
    public UncaughtExHandler(Context context)
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
        // 例外内容をプリファレンスに保存
        DebugHelper.save(mContext, e);

        // デフォルト例外ハンドラを戻す
        mDefaultUncaughtExceptionHandler.uncaughtException(thread, e);
    }
}
