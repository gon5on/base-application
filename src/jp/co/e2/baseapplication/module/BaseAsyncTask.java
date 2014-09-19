package jp.co.e2.baseapplication.module;

import android.content.Context;
import android.os.AsyncTask;

/**
 * AsyncTaskの基底クラス
 * 
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 * @access public
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
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
    public BaseAsyncTask(int tag)
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
    public BaseAsyncTask(int tag, Context context)
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
     * @return Result
     * @access protected
     */
    @Override
    protected abstract Result doInBackground(Params... params);

    /**
     * 非同期処理更新
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
     * 非同期後処理
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
     * @param AsyncTaskCallbackListener<Progress, Result> listener
     * @return void
     * @access public
     */
    public void setCallbackListener(AsyncTaskCallbackListener<Progress, Result> listener)
    {
        mListener = listener;
    }

    /**
     * コールバックリスナーを解除する
     * 
     * @return void
     * @access public
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
     * @access public
     */
    public static interface AsyncTaskCallbackListener<Progress, Result>
    {
        /**
         * 非同期処理前処理
         * 
         * @param int tag
         * @return void
         * @access public
         */
        public void onPreExecute(int tag);

        /**
         * 非同期処理中更新
         * 
         * @param int tag
         * @param Progress... values
         * @return void
         * @access public
         */
        public void onProgressUpdate(int tag, Progress... values);

        /**
         * 非同期処理キャンセル
         * 
         * @param int tag
         * @return void
         * @access public
         */
        public void onCancelled(int tag);

        /**
         * 非同期処理後
         * 
         * @param int tag
         * @param Result result
         * @return void
         * @access public
         */
        public void onPostExecute(int tag, Result result);
    }
}
