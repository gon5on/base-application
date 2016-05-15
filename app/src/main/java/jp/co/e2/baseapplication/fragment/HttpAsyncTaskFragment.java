package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.asynctask.SampleAsyncTask;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.LogUtils;
import jp.co.e2.baseapplication.config.Config;
import jp.co.e2.baseapplication.dialog.AppProgressDialog;
import jp.co.e2.baseapplication.entity.SampleApiEntity;
import jp.co.e2.baseapplication.entity.SampleEntity;

/**
 * 【AsyncTask版】非同期通信フラグメント
 *
 * 非同期通信を行うサンプル
 */
public class HttpAsyncTaskFragment extends Fragment implements SampleAsyncTask.AsyncTaskCallbackListener<Integer, SampleApiEntity> {
    private static final int TAG_ASYNC_TASK = 101;

    private static final String BUNDLE_RESULT = "bundle_result";

    private View mView;
    private AppProgressDialog mAppProgressDialog = null;

    /**
     * ファクトリーメソッド
     *
     * @return fragment フラグメント
     */
    public static HttpAsyncTaskFragment newInstance() {
        Bundle args = new Bundle();

        HttpAsyncTaskFragment fragment = new HttpAsyncTaskFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_http, container, false);

        //イベントをセット
        setEvent();

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
     * イベントをセット
     */
    private void setEvent() {
        //成功が帰ってくるURLに接続
        mView.findViewById(R.id.buttonConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleAsyncTask sampleAsyncTask = new SampleAsyncTask(TAG_ASYNC_TASK, getActivity());
                sampleAsyncTask.setCallbackListener(HttpAsyncTaskFragment.this);
                sampleAsyncTask.execute(Config.URL_SUCCESS);
            }
        });

        //エラーコードが帰ってくるURLに接続
        mView.findViewById(R.id.buttonConnectError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleAsyncTask sampleAsyncTask = new SampleAsyncTask(TAG_ASYNC_TASK, getActivity());
                sampleAsyncTask.setCallbackListener(HttpAsyncTaskFragment.this);
                sampleAsyncTask.execute(Config.URL_ERROR);
            }
        });

        //存在しないURLに接続
        mView.findViewById(R.id.buttonDisconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SampleAsyncTask sampleAsyncTask = new SampleAsyncTask(TAG_ASYNC_TASK, getActivity());
                sampleAsyncTask.setCallbackListener(HttpAsyncTaskFragment.this);
                sampleAsyncTask.execute(Config.URL_DISCONNECT);
            }
        });
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
        LogUtils.d("onPreExecute!");

        //画面を回転しないように固定する
        //通信中に回転すると、onPostExecute()のgetActivity()がnullになってしまう
        Configuration config = getResources().getConfiguration();
        getActivity().setRequestedOrientation(config.orientation);

        //プログレスダイアログを表示
        mAppProgressDialog = AppProgressDialog.getInstance(getString(R.string.connecting));
        mAppProgressDialog.show(getFragmentManager(), "dialog");
        //mAppProgressDialog.setCancelable(true);

        //結果表示を空にしておく
        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onProgressUpdate(int tag, Integer... values) {
        LogUtils.d("onProgressUpdate!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancelled(int tag) {
        LogUtils.d("onCancelled!");

        //プログレスダイアログを閉じる
        closeProgressDialog();

        //画面固定を解除する
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPostExecute(int tag, SampleApiEntity result) {
        LogUtils.d("onPostExecute!");

        //アクテビティが終了していたら処理を終わらせる
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        //プログレスダイアログを閉じる
        closeProgressDialog();

        //画面固定を解除する
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        //エラーの場合
        if (result == null || result.getCode() != HttpURLConnection.HTTP_OK) {
            AndroidUtils.showToastS(getActivity(), getString(R.string.errorMsgSomethingError));
        }
        //成功の場合、レスポンスを表示
        else {
            showResponseData(result.getData());
        }
    }

    /**
     * レスポンスを表示
     *
     * @param data レスポンスデータ
     */
    private void showResponseData(ArrayList<SampleEntity> data) {
        String str = "";

        for (SampleEntity value : data) {
            str += value.getId() + "\n";
            str += value.getSample1() + "\n";
            str += value.getSample2() + "\n";
            str += value.getSample3() + "\n";
            str += value.getCreated() + "\n";
            str += value.getModified() + "\n\n";
        }

        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(str);
    }
}