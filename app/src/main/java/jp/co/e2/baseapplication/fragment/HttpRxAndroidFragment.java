package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.api.SampleApi;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.LogUtils;
import jp.co.e2.baseapplication.config.Config;
import jp.co.e2.baseapplication.dialog.AppProgressDialog;
import jp.co.e2.baseapplication.entity.SampleApiEntity;
import jp.co.e2.baseapplication.entity.SampleEntity;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * 【RxAndroid版】非同期通信フラグメント
 *
 * 非同期通信を行うサンプル
 *
 * https://kinjouj.github.io/2015/06/rxandroid.html
 */
public class HttpRxAndroidFragment extends Fragment {
    private static final String BUNDLE_RESULT = "bundle_result";

    private View mView;
    private AppProgressDialog mAppProgressDialog = null;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private static Subscription mSubscription;

    /**
     * ファクトリーメソッド
     *
     * @return fragment フラグメント
     */
    public static HttpRxAndroidFragment newInstance() {
        Bundle args = new Bundle();

        HttpRxAndroidFragment fragment = new HttpRxAndroidFragment();
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
        //初回表示だったら、初期化する
        else {
            if (mSubscription != null) {
                mSubscription = null;
            }
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
     * {@inheritDoc}
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //コールバックを解除
        mSubscriptions.unsubscribe();
    }

    /**
     * イベントをセット
     */
    private void setEvent() {
        //成功が帰ってくるURLに接続
        mView.findViewById(R.id.buttonConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(Config.URL_SUCCESS);
            }
        });

        //エラーコードが帰ってくるURLに接続
        mView.findViewById(R.id.buttonConnectError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(Config.URL_ERROR);
            }
        });

        //存在しないURLに接続
        mView.findViewById(R.id.buttonDisconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect(Config.URL_DISCONNECT);
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
     * HTTP接続を行う
     *
     * @param url URL
     */
    private void connect(String url) {
        //プログレスダイアログを表示
        mAppProgressDialog = AppProgressDialog.getInstance(getString(R.string.connecting));
        mAppProgressDialog.show(getFragmentManager(), "dialog");
        //mAppProgressDialog.setCancelable(true);

        //結果表示を空にしておく
        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(null);

        //通信を行う
        mSubscription = SampleApi
                .request(url)
                .subscribe(new Observer<SampleApiEntity>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.d("onCompleted!");

                        //プログレスダイアログを閉じる
                        closeProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d("onError!");

                        //プログレスダイアログを閉じる
                        closeProgressDialog();

                        AndroidUtils.showToastS(getActivity(), getString(R.string.errorMsgSomethingError));
                    }

                    @Override
                    public void onNext(SampleApiEntity data) {
                        LogUtils.d("onNext!");

                        //プログレスダイアログを閉じる
                        closeProgressDialog();

                        //レスポンスを表示
                        showResponseData(data.getData());
                    }
                });

        //コールバックを一括解除可能なように、CompositeSubscriptionに追加しておく
        mSubscriptions.add(mSubscription);
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