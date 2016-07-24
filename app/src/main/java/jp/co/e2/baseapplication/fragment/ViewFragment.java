package jp.co.e2.baseapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.activity.MainActivity;
import jp.co.e2.baseapplication.activity.SubActivity;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.dialog.SampleDialog;

/**
 * ビューとスタイルフラグメント
 * 画面回転にも対応したサンプルにもなっている
 */
public class ViewFragment extends Fragment implements SampleDialog.CallbackListener {
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

        //イベントをセットする
        setEvent();

        return mView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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