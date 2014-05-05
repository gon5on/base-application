package com.example.baseapplication.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * プログレスダイアログ
 * 
 * @access public
 */
public class AppProgressDialog extends DialogFragment
{
    private CallbackListener mCallbackListener = null;

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
        mCallbackListener = (CallbackListener) getTargetFragment();                 //コールバックリスナーを取り出してセット

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("処理中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);

        return progressDialog;
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
     * コールバックリスナーを追加
     * 
     * @param CallbackListener callbackListener
     * @return void
     * @access public
     */
    public void setCallbackListener(CallbackListener callbackListener)
    {
        setTargetFragment((Fragment) callbackListener, 0);      //コールバックリスナーを一時保存、第2引数は適当
    }

    /**
     * コールバックリスナーを削除
     * 
     * @return void
     * @access public
     */
    public void removeCallbackListener()
    {
        mCallbackListener = null;
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