package com.example.baseapplication.module;

import android.content.Context;
import android.os.AsyncTask;

/**
 * AsyncTaskの基底クラス
 * 
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class AppAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
    protected final int mTag;

    protected AsyncTaskCallbackListener<Progress, Result> mListener;
    protected Context mContext = null;

    /**
     * コンストラクタ
     * 
     * @param int tag
     * @access public
     */
    public AppAsyncTask(int tag)
    {
        super();

        mTag = tag;
    }

    /**
     * コンストラクタ
     * 
     * @param int tag
     * @param Context context
     * @access public
     */
    public AppAsyncTask(int tag, Context context)
    {
        super();

        mTag = tag;
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

        if (mListener != null) {
            mListener.onPreExecute(mTag);
        }
    }

    /**
     * 非同期処理
     * 
     * @param Param... param
     * @return void
     * @access protected
     */
    @Override
    protected abstract Result doInBackground(Params... params);

    /**
     * 非同期処理
     * 
     * @param Progress values
     * @return void
     * @access protected
     */
    @Override
    protected void onProgressUpdate(Progress... values)
    {
        super.onProgressUpdate(values);

        if (mListener != null) {
            mListener.onProgressUpdate(mTag, values);
        }
    }

    /**
     * 非同期キャンセル処理
     * 
     * @return void
     * @access protected
     */
    @Override
    protected void onCancelled()
    {
        super.onCancelled();

        if (mListener != null) {
            mListener.onCancelled(mTag);
        }
    }

    /**
     * 非同期処理
     * 
     * @param Result result
     * @return void
     * @access protected
     */
    @Override
    protected void onPostExecute(Result result)
    {
        super.onPostExecute(result);

        if (mListener != null) {
            mListener.onPostExecute(mTag, result);
        }
    }

    /**
     * コールバックリスナーをセットする
     * 
     * @param listener
     */
    public void setCallbackListener(AsyncTaskCallbackListener<Progress, Result> listener)
    {
        mListener = listener;
    }

    /**
     * コールバックリスナーを解除する
     */
    public void removeCallbackListener()
    {
        mListener = null;
    }

    /**
     * 呼び出し元スレッドへ返却するインターフェース
     * 
     * @param <Progress>
     * @param <Result>
     */
    public static interface AsyncTaskCallbackListener<Progress, Result>
    {
        /**
         * AsyncTask#onPreExecute
         * 
         * @param int tag
         * @return void
         * @access public
         */
        public void onPreExecute(int tag);

        /**
         * AsyncTask#onProgressUpdate
         * 
         * @param int tag
         * @param Progress... values
         * @return void
         * @access public
         */
        public void onProgressUpdate(int tag, Progress... values);

        /**
         * AsyncTask#onCancelled
         * 
         * @param int tag
         * @return void
         * @access public
         */
        public void onCancelled(int tag);

        /**
         * AsyncTask#onPostExecute
         * 
         * @param int tag
         * @param Result result
         * @return void
         * @access public
         */
        public void onPostExecute(int tag, Result result);
    }
}
