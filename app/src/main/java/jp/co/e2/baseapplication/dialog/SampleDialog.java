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
    private static final String POSITIVE_BTN = "positive_btn";
    private static final String NEGATIVE_BTN = "negative_btn";

    /**
     * ファクトリーメソッド
     *
     * @param tag タグ
     * @param title タイトル
     * @param msg メッセージ
     * @param positiveBtn ポジティブボタン名
     * @return SampleDialog
     */
    public static SampleDialog getInstance(int tag, String title, String msg, String positiveBtn) {
        SampleDialog dialog = new SampleDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(TAG, tag);
        bundle.putString(TITLE, title);
        bundle.putString(MSG, msg);
        bundle.putString(POSITIVE_BTN, positiveBtn);
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * ファクトリーメソッド
     *
     * @param tag タグ
     * @param title タイトル
     * @param msg メッセージ
     * @param positiveBtn ポジティブボタン名
     * @param negativeBtn ネガティブボタン名
     * @return SampleDialog
     */
    public static SampleDialog getInstance(int tag, String title, String msg, String positiveBtn, String negativeBtn) {
        SampleDialog dialog = new SampleDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(TAG, tag);
        bundle.putString(TITLE, title);
        bundle.putString(MSG, msg);
        bundle.putString(POSITIVE_BTN, positiveBtn);
        bundle.putString(NEGATIVE_BTN, negativeBtn);
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
        String positiveBtn = getArguments().getString(POSITIVE_BTN);
        String negativeBtn = getArguments().getString(NEGATIVE_BTN);

        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppDialogStyle);
        builder.setTitle(title);
        builder.setMessage(msg);

        //ボタンにイベントをセット
        builder.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickSampleDialogOk(tag);
                }
            }
        });

        if (negativeBtn != null) {
            builder.setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallbackListener != null) {
                        mCallbackListener.onClickSampleDialogCancel(tag);
                    }
                }
            });
        }

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
