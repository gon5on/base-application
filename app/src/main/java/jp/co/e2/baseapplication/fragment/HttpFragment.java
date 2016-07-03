package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.config.Config;
import jp.co.e2.baseapplication.dialog.AppProgressDialog;
import jp.co.e2.baseapplication.entity.SampleEntity;
import jp.co.e2.baseapplication.http.SampleHttp;

/**
 * 非同期通信フラグメント
 *
 * 非同期通信を行うサンプル
 * イベントバスを使用しているので、画面回転に対応しているが、
 * イベントバスの登録と解除をonStartとonStopで行っているので、
 * 画面がバックグラウンドにいるときなどは、イベントバスからの通知を受け取れないので注意！
 */
public class HttpFragment extends Fragment {
    private static final String BUNDLE_RESULT = "bundle_result";

    private View mView;
    private static AppProgressDialog mAppProgressDialog = null;     //画面回転に対応するために仕方なくstatic

    /**
     * ファクトリーメソッド
     *
     * @return fragment フラグメント
     */
    public static HttpFragment newInstance() {
        Bundle args = new Bundle();

        HttpFragment fragment = new HttpFragment();
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
    public void onStart() {
        super.onStart();

        // EventBusを登録する
        EventBus.getDefault().register(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStop() {
        // EventBusを登録解除する
        EventBus.getDefault().unregister(this);

        super.onStop();
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
                callApi(Config.URL_SUCCESS);
            }
        });

        //エラーコードが帰ってくるURLに接続
        mView.findViewById(R.id.buttonConnectError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(Config.URL_ERROR);
            }
        });

        //存在しないURLに接続
        mView.findViewById(R.id.buttonDisconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(Config.URL_DISCONNECT);
            }
        });
    }

    /**
     * APIを呼ぶ
     */
    private void callApi(String url) {
        //プログレスダイアログを表示
        mAppProgressDialog = AppProgressDialog.getInstance(getString(R.string.connecting));
        mAppProgressDialog.show(getFragmentManager(), "dialog");
        mAppProgressDialog.setCancelable(false);

        //結果表示を空にしておく
        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(null);

        //APIに通信する
        new SampleHttp().execute(url);
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
     * イベントバスのコールバックメソッド
     *
     * イベントバスは引数の型を見て、一致する型のコールバックメソッドに処理が戻る
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SampleHttp.SampleEvent result) {
        //プログレスダイアログを閉じる
        closeProgressDialog();

        //成功の場合、レスポンスを表示
        if (result.isSuccessful()) {
            showResponseData(result.getSampleApiEntity().getData());
        }
        //エラーの場合
        else {
            AndroidUtils.showToastS(getActivity(), getString(R.string.errorMsgSomethingError));
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
