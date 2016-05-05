package jp.co.e2.baseapplication.debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import jp.co.e2.baseapplication.common.PreferenceUtils;
import jp.co.e2.baseapplication.dialog.BaseDialog;

/**
 * 例外報告ダイアログ
 */
public class DebugReportDialog extends BaseDialog<DebugReportDialog.CallbackListener> {
    /**
     * インスタンスを返す
     *
     * @return DebugReportDialog
     */
    public static DebugReportDialog getInstance() {
        DebugReportDialog dialog = new DebugReportDialog();

        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("例外報告");
        builder.setMessage("例外が起こりました。メーラーを起動するので、例外の内容を送信してください。");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickDebugReportDialogOk();
                }
                sendMail();
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickDebugReportDialogCancel();
                }
                dismiss();
            }
        });

        return builder.create();
    }

    /**
     * メーラーを起動する
     */
    private void sendMail() {
        //例外内容をプリファレンスから取得して、プリファレンスの中身を空にしておく
        String text = PreferenceUtils.get(getActivity().getApplicationContext(), DebugHelper.KEY, "");
        PreferenceUtils.delete(getActivity().getApplicationContext(), DebugHelper.KEY);

        //メーラーに投げる
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, DebugHelper.TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, DebugHelper.SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("message/rfc822");

        startActivity(intent);
    }

    /**
     * コールバックリスナー
     */
    public interface CallbackListener {
        /**
         * 例外報告ダイアログでOKが押された
         */
        void onClickDebugReportDialogOk();

        /**
         * 例外報告ダイアログでキャンセルが押された
         */
        void onClickDebugReportDialogCancel();
    }
}