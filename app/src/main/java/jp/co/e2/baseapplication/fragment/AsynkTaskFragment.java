package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
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
 * 画面回転にも対応したサンプルにもなっている
 */
public class AsynkTaskFragment extends Fragment implements SampleAsyncTask.AsyncTaskCallbackListener<Integer, String> {
    private static final int TAG_ASYNK_TASK = 101;
    private static final String BUNDLE_RESULT = "bundle_result";
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
                SampleAsyncTask sampleAsyncTask = new SampleAsyncTask(TAG_ASYNK_TASK, getActivity());
                sampleAsyncTask.setCallbackListener(AsynkTaskFragment.this);
                sampleAsyncTask.execute(URL);
            }
        });

        //再生成が走ったら、保管していた値を取り出す
        if (savedInstanceState != null) {
            TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
            textViewResult.setText(savedInstanceState.getString(BUNDLE_RESULT));
        }

        return mView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //再生成が走る前に値を保管しておく
        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        outState.putString(BUNDLE_RESULT, textViewResult.getText().toString());
    }

    /**
     * プログレスダイアログを閉じる
     */
    private void closeProgressDialog() {
        if (mAppProgressDialog != null) {
            mAppProgressDialog.dismiss();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPreExecute(int tag) {
        AndroidUtils.showToastS(getActivity(), "onPreExecute!");

        mAppProgressDialog = AppProgressDialog.getInstance(getString(R.string.connecting));
        mAppProgressDialog.show(getFragmentManager(), "dialog");
        mAppProgressDialog.setCancelable(false);

        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onProgressUpdate(int tag, Integer... values) {
        AndroidUtils.showToastS(getActivity(), "onProgressUpdate!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancelled(int tag) {
        AndroidUtils.showToastS(getActivity(), "onCancelled!");

        //プログレスダイアログを閉じる
        closeProgressDialog();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPostExecute(int tag, String result) {
        if (!isAdded()) {
            return;
        }

        AndroidUtils.showToastS(getActivity(), "onPostExecute!");

        //レスポンスボディを適当に頭から500文字表示
        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(result.substring(0, 500));

        //プログレスダイアログを閉じる
        closeProgressDialog();
    }
}