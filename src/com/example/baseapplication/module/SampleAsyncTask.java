package com.example.baseapplication.module;

import android.content.Context;
import android.os.AsyncTask;

import com.example.baseapplication.common.Http;

/**
 * サンプル非同期処理
 * 
 * @access public
 */
public class SampleAsyncTask extends AsyncTask<Void, Integer, Http>
{
    private AsyncTaskCallback mCallback = null;
    private Context mContext = null;

    /**
     * コンストラクタ
     * 
     * @param Context context
     * @access public
     */
    public SampleAsyncTask(Context context)
    {
        mContext = context;
    }

    /**
     * 非同期前処理
     * 
     * @return void
     * @access protected
     */
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        mCallback.preExecute();
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
        Http http = new Http(mContext);
        http.connect(Http.GET, "http://google.com");

        return http;
    }

    /**
     * 非同期後処理
     * 
     * @param Http http doInBackgroundの戻り値
     * @return void
     * @access protected
     */
    @Override
    protected void onPostExecute(Http http)
    {
        super.onPostExecute(http);

        //エラー
        if (http.getStatus() != Http.STATUS_OK) {
            mCallback.postExecuteFaild(http.getStatus());
            return;
        }

        mCallback.postExecuteSuccess();
    }

    /**
     * 中止された際の処理
     * 
     * @return void
     * @access protected
     */
    @Override
    protected void onCancelled()
    {
        super.onCancelled();

        mCallback.cancel();
    }

    /**
     * コールバックを追加
     * 
     * @param AsyncTaskCallback callback
     * @return void
     * @access public
     */
    public void setCallback(AsyncTaskCallback callback)
    {
        mCallback = callback;
    }

    /**
     * コールバックを削除
     * 
     * @return void
     * @access public
     */
    public void removeCallback()
    {
        mCallback = null;
    }

    /**
     * コールバック
     * 
     * @access public
     */
    public interface AsyncTaskCallback
    {
        void preExecute();

        void progressUpdate(Integer progress);

        void postExecuteSuccess();

        void postExecuteFaild(Integer status);

        void cancel();
    }
}