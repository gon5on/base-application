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
    private CallbackListener mCallbackListener = null;

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
     * ダイアログのイベントリスナーを登録する
     * 
     * アクテビティから呼ばれているのか、フラグメントから呼ばれているのかを判別して、
     * 適当な方法でイベントリスナーを登録する
     * 
     * @param CallbackListener listener
     * @return void
     * @access public
     */
    public void setCallbackListener(CallbackListener listener)
    {
        Integer listenerType;

        if (listener instanceof Activity) {
            listenerType = AppDialog.LISTENER_ACTIVITY;
        }
        else if (listener instanceof Fragment) {
            listenerType = AppDialog.LISTENER_FRAGMENT;
            setTargetFragment((Fragment) listener, 0);
        }
        else {
            throw new IllegalArgumentException(listener.getClass() + " must be either an Activity or a Fragment");
        }

        Bundle bundle = getArguments();
        bundle.putInt("listenerType", listenerType);
        setArguments(bundle);
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