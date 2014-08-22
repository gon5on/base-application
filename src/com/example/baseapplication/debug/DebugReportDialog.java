package com.example.baseapplication.debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.baseapplication.common.PrefarenceUtils;
import com.example.baseapplication.debug.DebugReportDialog.CallbackListener;
import com.example.baseapplication.dialog.BaseDialog;

/**
 * 例外報告ダイアログ
 * 
 * @access public
 */
public class DebugReportDialog extends BaseDialog<CallbackListener>
{
    /**
     * インスタンスを返す
     * 
     * @return DebugReportDialog
     * @access public
     */
    public static DebugReportDialog getInstance()
    {
        DebugReportDialog dialog = new DebugReportDialog();

        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * onCreateDialog
     * 
     * @param Bundle savedInstanceState
     * @return Dialog
     * @access public
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
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
     * 
     * @return void
     * @access private
     */
    private void sendMail()
    {
        //例外内容をプリファレンスから取得して、プリファレンスの中身を空にしておく
        String text = PrefarenceUtils.get(getActivity().getApplicationContext(), DebugHelper.KEY, "");
        PrefarenceUtils.delete(getActivity().getApplicationContext(), DebugHelper.KEY);

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
     * 
     * @access public
     */
    public interface CallbackListener
    {
        public void onClickDebugReportDialogOk();

        public void onClickDebugReportDialogCancel();
    }
}