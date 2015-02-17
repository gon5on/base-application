package jp.co.e2.baseapplication.asynctask;

import jp.co.e2.baseapplication.common.HttpHelper;

import android.content.Context;

/**
 * サンプル非同期処理
 */
public class SampleAsyncTask extends BaseAsyncTask<Void, Integer, HttpHelper> {
    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public SampleAsyncTask(Integer tag, Context context) {
        super(tag, context);
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected HttpHelper doInBackground(Void... params) {
        //バックキーでキャンセル可能なように、適当にスリープ
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //キャンセルボタンが押された
        if (isCancelled()) {
            return null;
        }

        HttpHelper http = new HttpHelper(mContext);
        http.connect(HttpHelper.GET, "http://google.com");

        return http;
    }
}