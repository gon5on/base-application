package com.example.baseapplication.debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.baseapplication.common.PrefarenceUtils;

/**
 * 例外報告ダイアログ
 * 
 * @access public
 */
public class DebugReportDialog extends DialogFragment
{
    private String mExStr = null;                     //例外内容

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
                sendMail();
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        //例外内容が空の場合は、プリファレンスから取得して、プリファレンスの中身を空にしておく
        if (mExStr == null) {
            mExStr = PrefarenceUtils.get(getActivity().getApplicationContext(), Debug.KEY, "");
            PrefarenceUtils.delete(getActivity().getApplicationContext(), Debug.KEY);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, Debug.TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, Debug.SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, mExStr);
        intent.setType("message/rfc822");

        startActivity(intent);
    }

    /**
     * 例外内容をセットする
     * 
     * @param String exStr
     * @return void
     * @access private
     */
    public void set(String exStr)
    {
        mExStr = exStr;
    }
}