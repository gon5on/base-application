package com.example.baseapplication.module;

import android.content.Context;

import com.example.baseapplication.common.Http;

/**
 * サンプル非同期処理
 * 
 * @access public
 */
public class SampleAsyncTask extends AppAsyncTask<Void, Integer, Http>
{
    /**
     * コンストラクタ
     * 
     * @param Context context
     * @access public
     */
    public SampleAsyncTask(Integer tag, Context context)
    {
        super(tag, context);
    }

    /**
     * 非同期処理
     * 
     * @param Void... params
     * @return Http http onPostExecuteに渡したい値
     * @access protected
     */
    @Override
    protected Http doInBackground(Void... params)
    {
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

        Http http = new Http(mContext);
        http.connect(Http.GET, "http://google.com");

        return http;
    }
}