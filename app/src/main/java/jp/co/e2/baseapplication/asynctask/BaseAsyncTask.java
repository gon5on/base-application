package jp.co.e2.baseapplication.asynctask;

import android.content.Context;
import android.os.AsyncTask;

/**
 * AsyncTaskの基底クラス
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    protected final int mTag;

    protected AsyncTaskCallbackListener<Progress, Result> mListener;
    protected Context mContext = null;

    /**
     * コンストラクタ
     *
     * @param tag タグ
     * @param context コンテキスト
     */
    public BaseAsyncTask(int tag, Context context) {
        super();

        mTag = tag;
        mContext = context;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (mListener != null) {
            mListener.onPreExecute(mTag);
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected abstract Result doInBackground(Params... params);

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);

        if (mListener != null) {
            mListener.onProgressUpdate(mTag, values);
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();

        if (mListener != null) {
            mListener.onCancelled(mTag);
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);

        if (mListener != null) {
            mListener.onPostExecute(mTag, result);
        }
    }

    /**
     * コールバックリスナーをセットする
     *
     * @param listener コールバックリスナー
     */
    public void setCallbackListener(AsyncTaskCallbackListener<Progress, Result> listener) {
        mListener = listener;
    }

    /**
     * コールバックリスナーを解除する
     */
    public void removeCallbackListener() {
        mListener = null;
    }

    /**
     * 呼び出し元スレッドへ返却するインターフェース
     *
     * @param <Progress>
     * @param <Result>
     */
    public interface AsyncTaskCallbackListener<Progress, Result> {
        /**
         * 非同期処理前処理
         *
         * @param tag タグ
         */
        void onPreExecute(int tag);

        /**
         * 非同期処理中更新
         *
         * @param tag タグ
         * @param values 呼び出し元に返したいもの
         */
        void onProgressUpdate(int tag, Progress... values);

        /**
         * 非同期処理キャンセル
         *
         * @param tag タグ
         */
        void onCancelled(int tag);

        /**
         * 非同期処理後
         *
         * @param tag タグ
         * @param result 呼び出し元に返却したいもの
         */
        void onPostExecute(int tag, Result result);
    }
}
