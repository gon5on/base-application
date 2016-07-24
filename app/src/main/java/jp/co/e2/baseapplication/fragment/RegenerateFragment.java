package jp.co.e2.baseapplication.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.dialog.NoReSetCallBackListenerDialog;
import jp.co.e2.baseapplication.dialog.ReSetCallBackListenerDialog;
import jp.co.e2.baseapplication.dialog.UseDialogFragmentDialog;

/**
 * 画面再生成フラグメント
 *
 * 画面再生成をちゃんと考慮した場合と、考慮していない場合のサンプル
 */
public class RegenerateFragment extends Fragment
        implements NoReSetCallBackListenerDialog.CallbackListener, ReSetCallBackListenerDialog.CallbackListener {
    private static final String BUNDLE_TEXT  = "bundle_text";
    private static final String BUNDLE_COUNT = "bundle_count";

    private View mView;
    private int mCountSavedInstanceState = 0;
    private int mCountNotSavedInstanceState = 0;
    public String mText;            //bundleをとおして値を渡さない例のために、MainActivityから直接値を代入している、なのでpubic

    /**
     * ファクトリーメソッド
     *
     * @param text テキスト
     * @return fragment フラグメント
     */
    public static RegenerateFragment newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_TEXT, text);

        RegenerateFragment fragment = new RegenerateFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_regenerate, container, false);

        //
        // 【他クラスへの値受け渡しサンプル】
        // ダイアログへの値受け渡しの場合も同様の現象が起こる
        //
        // 外部から渡された値を、bundleを通さずにR.id.textView1にセットしているので、
        // 画面再生成をすると文字が消える
        TextView textView1 = (TextView) mView.findViewById(R.id.textView1);
        textView1.setText(mText);

        // 外部から渡された値を、bundleを通してR.id.textView2にセットしているので、
        // 画面再生成しても文字が消えない
        TextView textView2 = (TextView) mView.findViewById(R.id.textView2);
        textView2.setText(getArguments().getString(BUNDLE_TEXT));

        //
        // 【クリックイベントサンプル】
        //
        // 画面再生成が走った時にイベント系はリセットされるが、
        // if文の中は画面再生成が走った時に通らないので、画面再生成後はR.id.button1を押しても反応しなくなる
        if (savedInstanceState == null) {
            mView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AndroidUtils.showToastS(getActivity(), getString(R.string.pushLeftButton));
                }
            });
        }

        // if文の外であれば、画面再生成時にも必ず通るので、画面再生成後でもR.id.button2を押すと反応する
        mView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.showToastS(getActivity(), getString(R.string.pushRightButton));
            }
        });

        //
        // 【ダイアログ表示サンプル】
        //
        // R.id.button3を押した際に表示するダイアログは、FragmentManagerを使用していないので、
        // 画面再生成をするとダイアログが消える
        mView.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.unUseDialogFragmentDialog))
                        .setPositiveButton(getString(R.string.ok), null)
                        .show();
            }
        });

        // R.id.button4を押した際に表示するダイアログは、FragmentManagerを継承したクラス内で、
        // new AlertDialog.Builderを行っているので、画面再生成をしてもダイアログが保持される
        mView.findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UseDialogFragmentDialog dialog = new UseDialogFragmentDialog();
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        //
        // 【ダイアログコールバック表示サンプル】
        //
        // R.id.button5を押した際に表示するダイアログは、
        // 再生成時にコールバックリスナーの再設定を行ってないので、コールバックを受け取れない
        mView.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoReSetCallBackListenerDialog dialog = new NoReSetCallBackListenerDialog();
                dialog.setCallbackListener(RegenerateFragment.this);
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        // R.id.button6を押した際に表示するダイアログは、
        // 再生成時にコールバックリスナーの再設定を行っているので、コールバックを受け取れる
        mView.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReSetCallBackListenerDialog dialog = new ReSetCallBackListenerDialog();
                dialog.setCallbackListener(RegenerateFragment.this);
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        //
        // 【ダイアログコールバック表示サンプル】
        //
        // R.id.button5を押した際に表示するダイアログは、
        // 再生成時にコールバックリスナーの再設定を行ってないので、コールバックを受け取れない
        mView.findViewById(R.id.checkBoxEnable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoReSetCallBackListenerDialog dialog = new NoReSetCallBackListenerDialog();
                dialog.setCallbackListener(RegenerateFragment.this);
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        // R.id.button6を押した際に表示するダイアログは、
        // 再生成時にコールバックリスナーの再設定を行っているので、コールバックを受け取れる
        mView.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReSetCallBackListenerDialog dialog = new ReSetCallBackListenerDialog();
                dialog.setCallbackListener(RegenerateFragment.this);
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        //
        // 【Viewの状態を判定サンプル】
        //
        // R.id..button7は何もしていないので、画面再生成が走るとボタンの状態が
        // disabledに戻る
        final Button button7 = (Button) mView.findViewById(R.id.button7);

        // R.id..button8は、画面再生成後に通るonViewStateRestoredで、
        // チェックボックスの値がリストアされた後に、チェックボックスの状態を見て
        // ボタンの有効無効を切り替えているので、状態が保持される
        final Button button8 = (Button) mView.findViewById(R.id.button8);

        final CheckBox checkBoxEnable = (CheckBox) mView.findViewById(R.id.checkBoxEnable);
        checkBoxEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button7.setEnabled(checkBoxEnable.isChecked());
                button8.setEnabled(checkBoxEnable.isChecked());
            }
        });

        //
        // 【メンバ変数保持サンプル】
        //
        final TextView textView3 = (TextView) mView.findViewById(R.id.textView3);
        final TextView textView4 = (TextView) mView.findViewById(R.id.textView4);

        mView.findViewById(R.id.buttonPlusOne).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountNotSavedInstanceState++;
                mCountSavedInstanceState++;

                textView3.setText(String.valueOf(mCountNotSavedInstanceState));
                textView4.setText(String.valueOf(mCountSavedInstanceState));
            }
        });

        //mCountNotSavedInstanceStateは何もやってないので、画面再生成するとカウントがリセットされる
        textView3.setText(String.valueOf(mCountNotSavedInstanceState));

        // 画面再生成直前にonSaveInstanceStateにてmCountSavedInstanceStateの値をbundleに格納して、
        // 画面再生成時にbundleから取り出して値をmCountSavedInstanceStateにセットしているので、カウントが保持される
        if (savedInstanceState != null) {
            mCountSavedInstanceState = savedInstanceState.getInt(BUNDLE_COUNT);
        }
        textView4.setText(String.valueOf(mCountSavedInstanceState));

        //
        // 【非同期処理サンプル】
        //
        // AsyncTaskを使って非同期処理をすると、画面再生成によってonExecute()で
        // activityがnullになるので、どうしても画面再生成に対応できない
        mView.findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SampleAsyncTask().execute();
            }
        });

        // EventBusを使ってメインスレッドに処理を戻せば、
        // 画面再生成にonStartを通って、再度イベントバスがセットされるので、画面再生成にも対応できる
        mView.findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBusSample();
            }
        });

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
        outState.putInt(BUNDLE_COUNT, mCountSavedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        //リストアされたチェックボックスにチェックが入っているかを見て、
        //ボタンの有効無効をセットする
        if (((CheckBox) mView.findViewById(R.id.checkBoxEnable)).isChecked()) {
            mView.findViewById(R.id.button8).setEnabled(true);
        }
    }

    /**
     * 非同期処理を行い、イベントバスで処理を戻すサンプル
     */
    private void eventBusSample() {
        new Thread(new Runnable() {
            public void run() {
                //時間のかかる処理
                int i;
                int i2;
                int i3 = 0;
                for (i = 0; i <= 10; i++) {
                    for (i2 = 0; i2 <= 100000000; i2++) {
                        i3++;
                    }
                }

                //メインスレッドに処理を戻す
                EventBus.getDefault().post(getString(R.string.callBackEventBus));
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClickNoReSetCallBackListenerDialogDialogOk() {
        AndroidUtils.showToastS(getActivity(), getString(R.string.doneCallback));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClickReSetCallBackListenerDialogDialogOk() {
        AndroidUtils.showToastS(getActivity(), getString(R.string.doneCallback));
    }

    /**
     * イベントバスのコールバックメソッド
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String result) {
        AndroidUtils.showToastS(getContext(), result);
    }

    /**
     * AsyncTaskを使用した非同期処理サンプル
     */
    private class SampleAsyncTask extends AsyncTask<Void, Integer, String> {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPreExecute() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String doInBackground(Void... value) {
            //時間のかかる処理
            int i;
            int i2;
            int i3 = 0;
            for (i = 0; i <= 10; i++) {
                for (i2 = 0; i2 <= 100000000; i2++) {
                    i3++;
                }
            }

            return getString(R.string.finishAsyncTask);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(String result) {
            AndroidUtils.showToastS(getContext(), result);
        }
    }
}