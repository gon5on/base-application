package com.example.baseapplication.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

/**
 * プログレスダイアログ
 * 
 * @access public
 */
public class AppProgressDialog extends DialogFragment
{
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
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("処理中");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        return progressDialog;
    }
}