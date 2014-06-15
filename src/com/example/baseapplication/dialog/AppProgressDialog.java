package com.example.baseapplication.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.baseapplication.dialog.AppProgressDialog.CallbackListener;

/**
 * プログレスダイアログ
 * 
 * @access public
 */
public class AppProgressDialog extends AppDialog<CallbackListener>
{
    /**
     * インスタンスを返す
     * 
     * @String String title
     * @return SampleDialog
     * @access public
     */
    public static AppProgressDialog getInstance(String title)
    {
        AppProgressDialog dialog = new AppProgressDialog();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialog.setArguments(bundle);

        return dialog;
    }

    /**
     * onCreateDialog
     * 
     * @param Bundle savedInstanceState
     * @return Dialog mProgressDialog
     * @access public
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //bundleから値を取り出す
        String title = getArguments().getString("title");

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(title);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);

        return dialog;
    }

    /**
     * onCancel
     * 
     * @param DialogInterface dialog
     * @return void
     * @access public
     */
    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);

        if (mCallbackListener != null) {
            mCallbackListener.onProgressDialogCancel();
        }
    }

    /**
     * コールバックリスナー
     * 
     * @access public
     */
    public interface CallbackListener
    {
        public void onProgressDialogCancel();
    }
}