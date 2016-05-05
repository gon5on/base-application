package jp.co.e2.baseapplication.debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * 例外報告ダイアログ
 */
public class DebugReportDialog extends DialogFragment {
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
                sendMail();
            }
        });

        builder.setNegativeButton("Cancel", null);

        return builder.create();
    }

    /**
     * メーラーを起動する
     */
    private void sendMail() {
        //例外内容をプリファレンスから取得して、プリファレンスの中身を空にしておく
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String text = sp.getString(DebugHelper.KEY, "");
        sp.edit().remove(DebugHelper.KEY).apply();

        //メーラーに投げる
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, DebugHelper.TO);
        intent.putExtra(Intent.EXTRA_SUBJECT, DebugHelper.SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("message/rfc822");

        startActivity(intent);
    }
}