package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.activity.SubActivity;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.dialog.SampleDialog;

/**
 * ビューとスタイルフラグメント
 * 画面回転にも対応したサンプルにもなっている
 */
public class ViewFragment extends Fragment implements SampleDialog.CallbackListener {
    private static final int TAG_DIALOG = 101;

    private static final String BUNDLE_EDIT_TEXT1 = "bundle_edit_text1";
    private static final String BUNDLE_EDIT_TEXT2 = "bundle_edit_text2";
    private static final String BUNDLE_RADIO = "bundle_radio";
    private static final String BUNDLE_CHECKBOX = "bundle_checkbox";
    private static final String BUNDLE_SWITCH = "bundle_switch";

    private View mView;

    /**
     * ファクトリーメソッド
     *
     * @return fragment フラグメント
     */
    public static ViewFragment newInstance() {
        Bundle args = new Bundle();

        ViewFragment fragment = new ViewFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_view, container, false);

        //イベントをセットする
        setEvent();

        //再生成が走ったら、保管していた値を取り出して画面にセットする
        if (savedInstanceState != null) {
            restoreData(savedInstanceState);
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
        TextView editText1 = (TextView) mView.findViewById(R.id.editText1);
        outState.putString(BUNDLE_EDIT_TEXT1, editText1.getText().toString());

        TextView editText2 = (TextView) mView.findViewById(R.id.editText2);
        outState.putString(BUNDLE_EDIT_TEXT2, editText2.getText().toString());

        RadioGroup radioGroup = (RadioGroup) mView.findViewById(R.id.radioGroup);
        outState.putInt(BUNDLE_RADIO, radioGroup.getCheckedRadioButtonId());

        CheckBox checkBox = (CheckBox) mView.findViewById(R.id.checkbox);
        outState.putBoolean(BUNDLE_CHECKBOX, checkBox.isChecked());

        SwitchCompat switchBtn = (SwitchCompat) mView.findViewById(R.id.switchBtn);
        outState.putBoolean(BUNDLE_SWITCH, switchBtn.isChecked());
    }

    /**
     * イベントをセットする
     */
    private void setEvent() {
        //アクテビティを開く
        mView.findViewById(R.id.buttonActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SubActivity.newInstance(getActivity(), "hogehoge", 1000));
            }
        });

        //ダイアログを開く
        mView.findViewById(R.id.buttonDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = getString(R.string.sample);
                String msg = getString(R.string.sampleDialog);
                String btn = getString(R.string.ok);
                SampleDialog sampleDialog = SampleDialog.getInstance(TAG_DIALOG, title, msg, btn);
                sampleDialog.setCallbackListener(ViewFragment.this);
                sampleDialog.show(getFragmentManager(), "dialog");
            }
        });
    }

    /**
     * 保管していた値を取り出す
     *
     * @param savedInstanceState バンドル
     */
    private void restoreData(Bundle savedInstanceState) {
        TextView editText1 = (TextView) mView.findViewById(R.id.editText1);
        editText1.setText(savedInstanceState.getString(BUNDLE_EDIT_TEXT1));

        TextView editText2 = (TextView) mView.findViewById(R.id.editText2);
        editText2.setText(savedInstanceState.getString(BUNDLE_EDIT_TEXT2));

        RadioGroup radioGroup = (RadioGroup) mView.findViewById(R.id.radioGroup);
        radioGroup.check(savedInstanceState.getInt(BUNDLE_RADIO));

        CheckBox checkBox = (CheckBox) mView.findViewById(R.id.checkbox);
        checkBox.setChecked(savedInstanceState.getBoolean(BUNDLE_CHECKBOX));

        SwitchCompat switchBtn = (SwitchCompat) mView.findViewById(R.id.switchBtn);
        switchBtn.setChecked(savedInstanceState.getBoolean(BUNDLE_SWITCH));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClickSampleDialogOk(int tag) {
        AndroidUtils.showToastS(getActivity(), getString(R.string.ok));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClickSampleDialogCancel(int tag) {
        AndroidUtils.showToastS(getActivity(), getString(R.string.cancel));
    }
}