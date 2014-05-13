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
    private CallbackListener mCallbackListener = null;
    private Context mContext = null;

    /**
     * コンストラクタ
     * 
     * @param Context context
     * @access public
     */
    public SampleAsyncTask(Context context)
    {
        super();

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

        if (mCallbackListener != null) {
            mCallbackListener.preExecute();
        }
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

        if (isCancelled()) {
            return null;
        }

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
            mCallbackListener.postExecuteFaild(http.getStatus());
            return;
        }

        if (mCallbackListener != null) {
            mCallbackListener.postExecuteSuccess();
        }
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

        if (mCallbackListener != null) {
            mCallbackListener.cancel();
        }
    }

    /**
     * コールバックリスナーを追加
     * 
     * @param CallbackListener callback
     * @return void
     * @access public
     */
    public void setCallbackListener(CallbackListener callback)
    {
        mCallbackListener = callback;
    }

    /**
     * コールバックリスナーを削除
     * 
     * @return void
     * @access public
     */
    public void removeCallbackListener()
    {
        mCallbackListener = null;
    }

    /**
     * コールバックリスナー
     * 
     * @access public
     */
    public interface CallbackListener
    {
        void preExecute();

        void progressUpdate(Integer progress);

        void postExecuteSuccess();

        void postExecuteFaild(Integer status);

        void cancel();
    }
}