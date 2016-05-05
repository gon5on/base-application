package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.asynctask.SampleAsyncTask;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.dialog.AppProgressDialog;

/**
 * 非同期通信フラグメント
 *
 * 非同期通信を行うサンプル
 */
public class AsynkTaskFragment extends Fragment {
    private static final int TAG_ASYNK_TASK = 101;
    private static final String URL = "http://google.com";

    private View mView;
    private AppProgressDialog mAppProgressDialog = null;

    /**
     * ファクトリーメソッド
     *
     * @return fragment フラグメント
     */
    public static AsynkTaskFragment newInstance() {
        Bundle args = new Bundle();

        AsynkTaskFragment fragment = new AsynkTaskFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_asynk_task, container, false);

        //ボタンを押したらサーバに接続
        mView.findViewById(R.id.buttonConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectServer();
            }
        });

        return mView;
    }

    /**
     * サーバに接続する
     */
    private void connectServer() {
        SampleAsyncTask sampleAsyncTask = new SampleAsyncTask(TAG_ASYNK_TASK, getActivity());
        sampleAsyncTask.setCallbackListener(new SampleAsyncTask.AsyncTaskCallbackListener<Integer, String>() {
            @Override
            public void onPreExecute(int tag) {
                AndroidUtils.showToastS(getActivity(), "onPreExecute!");
                prepareConnect();
            }

            @Override
            public void onProgressUpdate(int tag, Integer... values) {
                AndroidUtils.showToastS(getActivity(), "onProgressUpdate!");
            }

            @Override
            public void onCancelled(int tag) {
                AndroidUtils.showToastS(getActivity(), "onCancelled!");
                closeProgressDialog();
            }

            @Override
            public void onPostExecute(int tag, String result) {
                AndroidUtils.showToastS(getActivity(), "onPostExecute!");
                showResult(result);
            }
        });
        sampleAsyncTask.execute(URL);
    }

    /**
     * 接続前準備
     */
    private void prepareConnect() {
        mAppProgressDialog = AppProgressDialog.getInstance("通信中");
        mAppProgressDialog.show(getFragmentManager(), "dialog");
        mAppProgressDialog.setCancelable(false);

        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(null);
    }

    /**
     * 通信結果を表示する
     *
     * @param result レスポンスボディ
     */
    private void showResult(String result) {
        if (getActivity().isFinishing()) {
            return;
        }

        //レスポンスボディを適当に頭から500文字表示
        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(result.substring(0, 500));

        closeProgressDialog();
    }

    /**
     * プログレスダイアログを閉じる
     */
    private void closeProgressDialog() {
        if (mAppProgressDialog != null) {
            mAppProgressDialog.dismiss();
        }
    }
}