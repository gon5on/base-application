package jp.co.e2.baseapplication.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.dialog.SampleDialog.CallbackListener;

/**
 * サンプルダイアログ
 */
public class SampleDialog extends BaseDialog<CallbackListener> {
    private static final String TAG = "tag";
    private static final String TITLE = "title";
    private static final String MSG = "msg";

    /**
     * ファクトリーメソッド
     *
     * @param tag タグ
     * @param title タイトル
     * @param msg メッセージ
     * @return SampleDialog
     */
    public static SampleDialog getInstance(int tag, String title, String msg) {
        SampleDialog dialog = new SampleDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(TAG, tag);
        bundle.putString(TITLE, title);
        bundle.putString(MSG, msg);
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //bundleから値を取り出す
        final int tag = getArguments().getInt(TAG);
        String title = getArguments().getString(TITLE);
        String msg = getArguments().getString(MSG);

        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);

        //ボタンにイベントをセット
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickSampleDialogOk(tag);
                }
                dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickSampleDialogCancel(tag);
                }
                dismiss();
            }
        });

        return builder.create();
    }

    /**
     * コールバックリスナー
     */
    public interface CallbackListener {
        /**
         * サンプルダイアログでOKが押された
         * 
         * @param tag タグ
         */
        void onClickSampleDialogOk(int tag);

        /**
         * サンプルダイアログでキャンセルが押された
         * 
         * @param tag タグ
         */
        void onClickSampleDialogCancel(int tag);
    }
}
