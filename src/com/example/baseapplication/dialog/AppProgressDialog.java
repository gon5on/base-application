package com.example.baseapplication.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * プログレスダイアログ
 * 
 * @access public
 */
public class AppProgressDialog extends AppDialog
{
    private static AppProgressDialog mDialog = null;

    private CallbackListener mCallbackListener = null;

    /**
     * インスタンスを返す
     * 
     * @String Integer listenerType
     * @String String title
     * @return SampleDialog
     * @access public
     */
    public static AppProgressDialog getInstance(Integer listenerType, String title)
    {
        if (mDialog == null) {
            mDialog = new AppProgressDialog();

            Bundle bundle = new Bundle();
            bundle.putInt("listenerType", listenerType);
            bundle.putString("title", title);
            mDialog.setArguments(bundle);
        }

        return mDialog;
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
     * onAttach
     * 
     * @param Activity activity
     * @return void
     * @access public
     */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        Integer listenerType = getArguments().getInt("listenerType");

        if (listenerType == AppDialog.LISTENER_ACTIVITY) {
            mCallbackListener = (CallbackListener) activity;
        } else {
            mCallbackListener = (CallbackListener) getTargetFragment();
        }
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