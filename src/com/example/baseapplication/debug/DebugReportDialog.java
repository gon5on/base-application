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
        String exStr = PrefarenceUtils.get(getActivity().getApplicationContext(), Debug.KEY, "");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, Debug.TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, Debug.SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, exStr);
        intent.setType("message/rfc822");

        startActivity(intent);

        //プリファレンスの例外内容を空にする
        PrefarenceUtils.delete(getActivity().getApplicationContext(), Debug.KEY);
    }
}