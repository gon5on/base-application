package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.activity.SubActivity;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.dialog.SampleDialog;

/**
 * ビューとスタイルフラグメント
 */
public class ViewFragment extends Fragment {
    private static final int TAG_DIALOG = 101;

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
                showSampleDialog();
            }
        });

        return mView;
    }

    /**
     * サンプルダイアログ表示
     */
    private void showSampleDialog() {
        SampleDialog sampleDialog = SampleDialog.getInstance(TAG_DIALOG, "サンプル", "サンプルダイアログです");
        sampleDialog.setCallbackListener(new SampleDialog.CallbackListener() {
            @Override
            public void onClickSampleDialogOk(int tag) {
                AndroidUtils.showToastS(getActivity(), "OK!");
            }

            @Override
            public void onClickSampleDialogCancel(int tag) {
                AndroidUtils.showToastS(getActivity(), "cancel!");
            }
        });
        sampleDialog.show(getFragmentManager(), "dialog");
    }
}