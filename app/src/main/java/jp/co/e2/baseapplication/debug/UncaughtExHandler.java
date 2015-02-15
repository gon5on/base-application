package jp.co.e2.baseapplication.debug;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

/**
 * UncaughtExceptionハンドリングクラス
 */
public class UncaughtExHandler implements UncaughtExceptionHandler {
    private Context mContext;
    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public UncaughtExHandler(Context context) {
        mContext = context;

        // デフォルト例外ハンドラを保持する
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        // 例外内容をプリファレンスに保存
        DebugHelper.save(mContext, e);

        // デフォルト例外ハンドラを戻す
        mDefaultUncaughtExceptionHandler.uncaughtException(thread, e);
    }
}
