package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.LogUtils;
import jp.co.e2.baseapplication.dialog.NoReSetCallBackListenerDialog;
import jp.co.e2.baseapplication.dialog.UseDialogFragmentDialog;

/**
 * 画面再生成フラグメント
 */
public class RegenerateFragment extends Fragment implements NoReSetCallBackListenerDialog.CallbackListener {
    private static final String BUNDLE_TEXT  = "bundle_text";
    private static final String BUNDLE_EDIT_TEXT2 = "bundle_edit_text2";
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
        // 【EditText入力値保持サンプル】
        //
        // R.id.editText1は何もやってないので、文字を入力しておいて画面再生成させると文字が消える

        // R.id.editText2は、画面再生成直前にonSaveInstanceStateにて入力値をbundleに格納して、
        // 画面再生成時にbundleから取り出してEditTextにセットし直しているので、入力した文字が保持される
        if (savedInstanceState != null) {
            TextView editText2 = (TextView) mView.findViewById(R.id.editText2);
            editText2.setText(savedInstanceState.getString(BUNDLE_EDIT_TEXT2));
        }

        //
        // 【EditText入力初期値サンプル】
        // チェックボックスやラジオボタン、ボタンのdisableなども同様の現象が起こる
        //
        // 画面再生成時に通ってしまうので、R.id.editText3は初期値が再設定されてしまう
        TextView editText3 = (TextView) mView.findViewById(R.id.editText3);
        editText3.setText(getArguments().getString(BUNDLE_TEXT));

        // if文の中は画面再生成時は通らないので、R.id.editText4は初期値は再設定されない
        // （入力値を画面再生成直前に入力値をbundleに格納して～というのはやっていないので、入力値は消える）
        if (savedInstanceState != null) {
            TextView editText4 = (TextView) mView.findViewById(R.id.editText4);
            editText4.setText(getArguments().getString(BUNDLE_TEXT));
        }

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
        if (savedInstanceState != null) {
            mView.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AndroidUtils.showToastS(getActivity(), "左のボタンを押しました！");
                }
            });
        }

        // if文の外であれば、画面再生成時にも必ず通るので、画面再生成後でもR.id.button2を押すと反応する
        mView.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidUtils.showToastS(getActivity(), "右のボタンを押しました！");
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
                        .setMessage("画面再生成で消えてしまうダイアログ")
                        .setPositiveButton("OK", null)
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
        // 再生成時にコールバックリスナーの再設定をお行っているので、コールバックを受け取れる
        mView.findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //
        // 【メンバ変数保持サンプル】
        //
        mView.findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountSavedInstanceState++;
                mCountNotSavedInstanceState++;

                LogUtils.d("mCountSavedInstanceState = " + mCountSavedInstanceState);
                LogUtils.d("mCountNotSavedInstanceState = " + mCountNotSavedInstanceState);
            }
        });

        //mCountNotSavedInstanceStateは何もやってないので、画面再生成するとカウントがリセットされる
        LogUtils.d("mCountNotSavedInstanceState = " + mCountNotSavedInstanceState);

        // 画面再生成直前にonSaveInstanceStateにてmCountSavedInstanceStateの値をbundleに格納して、
        // 画面再生成時にbundleから取り出して値をmCountSavedInstanceStateにセットしているので、カウントが保持される
        if (savedInstanceState != null) {
            mCountSavedInstanceState = savedInstanceState.getInt(BUNDLE_COUNT);
        }
        LogUtils.d("mCountSavedInstanceState = " + mCountSavedInstanceState);

        return mView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //再生成が走る前に値を保管しておく
        TextView editText2 = (TextView) mView.findViewById(R.id.editText2);
        outState.putString(BUNDLE_EDIT_TEXT2, editText2.getText().toString());

        outState.putInt(BUNDLE_COUNT, mCountSavedInstanceState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClickNoReSetCallBackListenerDialogDialogOk() {
        AndroidUtils.showToastS(getActivity(), "コールバックされました！");
    }
}