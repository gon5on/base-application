package jp.co.e2.baseapplication.activity;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.HttpHelper;
import jp.co.e2.baseapplication.dialog.AppProgressDialog;
import jp.co.e2.baseapplication.dialog.SampleDialog;
import jp.co.e2.baseapplication.asynctask.BaseAsyncTask.AsyncTaskCallbackListener;
import jp.co.e2.baseapplication.asynctask.SampleAsyncTask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 非同期処理アクテビティ
 */
public class AsyncTaskActivity extends BaseActivity {
    private static final int DIALOG_TAG = 1;
    private static final int SAMPLE_ASYNC_TASK_TAG = 1;

    /**
     * ファクトリーメソッドもどき
     *
     * @param activity アクテビティ
     * @return Intent intent
     */
    public static Intent newIntent(Activity activity) {
        Intent intent = new Intent(activity, AsyncTaskActivity.class);

        return intent;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstance()).commit();
        }
    }

    /**
     * PlaceholderFragment
     */
    public static class PlaceholderFragment extends Fragment implements SampleDialog.CallbackListener, AppProgressDialog.CallbackListener, AsyncTaskCallbackListener<Integer, HttpHelper> {
        private AppProgressDialog mAppProgressDialog = null;
        private SampleAsyncTask mSampleAsyncTask = null;

        /**
         * ファクトリーメソッド
         *
         * @return PlaceholderFragment fragment
         */
        public static PlaceholderFragment newInstance() {
            Bundle args = new Bundle();

            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(args);

            return fragment;
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View view = inflater.inflate(R.layout.fragment_asynctask, container, false);

            // fragment再生成抑止、必要に応じて
            setRetainInstance(true);

            //ボタンを押したらサーバに接続
            view.findViewById(R.id.button).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectServer();
                }
            });

            return view;
        }

        /**
         * サーバに接続
         */
        private void connectServer() {
            //非同期処理
            mSampleAsyncTask = new SampleAsyncTask(SAMPLE_ASYNC_TASK_TAG, getActivity());
            mSampleAsyncTask.setCallbackListener(this);
            mSampleAsyncTask.execute();
        }

        /**
         * サンプルダイアログ表示
         *
         * @param status ステータス
         */
        private void showSampleDialog(Integer status) {
            SampleDialog sampleDialog = SampleDialog.getInstance(DIALOG_TAG, "エラー", "エラーが発生しました（" + status + "）");
            sampleDialog.setCallbackListener(this);
            sampleDialog.show(getFragmentManager(), "dialog");
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onPreExecute(int tag) {
            mAppProgressDialog = AppProgressDialog.getInstance("通信中");
            mAppProgressDialog.setCallbackListener(this);
            mAppProgressDialog.show(getFragmentManager(), "dialog");
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onProgressUpdate(int tag, Integer... values) {
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onCancelled(int tag) {
            if (mAppProgressDialog != null) {
                mAppProgressDialog.dismiss();
            }

            AndroidUtils.showToastS(getActivity().getApplicationContext(), "キャンセルしました。");
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onPostExecute(int tag, HttpHelper http) {
            if (mAppProgressDialog != null) {
                mAppProgressDialog.dismiss();
            }

            if (http.getStatus() == HttpHelper.STATUS_OK) {
                AndroidUtils.showToastS(getActivity().getApplicationContext(), "通信が終了しました。");
            } else {
                showSampleDialog(http.getHttpStatus());
            }
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onProgressDialogCancel() {
            if (mSampleAsyncTask != null) {
                mSampleAsyncTask.cancel(true);
            }
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onClickSampleDialogOk(int tag) {
            connectServer();        //リトライ
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onClickSampleDialogCancel(int tag) {
        }
    }
}