package jp.co.e2.baseapplication.dialog;

import jp.co.e2.baseapplication.dialog.SampleDialog.CallbackListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * サンプルダイアログ
 */
public class SampleDialog extends BaseDialog<CallbackListener> {
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
        bundle.putInt("tag", tag);
        bundle.putString("title", title);
        bundle.putString("msg", msg);
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //bundleから値を取り出す
        final int tag = getArguments().getInt("tag");
        String title = getArguments().getString("title");
        String msg = getArguments().getString("msg");

        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);

        //ボタンにイベントをセット
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickSampleDialogOk(tag);
                }
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        public void onClickSampleDialogOk(int tag);

        /**
         * サンプルダイアログでキャンセルが押された
         * 
         * @param tag タグ
         */
        public void onClickSampleDialogCancel(int tag);
    }
}
