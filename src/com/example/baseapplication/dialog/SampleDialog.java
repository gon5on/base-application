package com.example.baseapplication.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * サンプルダイアログ
 * 
 * @access public
 */
public class SampleDialog extends AppDialog
{
    private static SampleDialog mDialog = null;

    private CallbackListener mCallbackListener = null;

    /**
     * インスタンスを返す
     * 
     * @String Integer listenerType
     * @String String title
     * @String String msg
     * @return SampleDialog
     * @access public
     */
    public static SampleDialog getInstance(Integer listenerType, String title, String msg)
    {
        if (mDialog == null) {
            mDialog = new SampleDialog();

            Bundle bundle = new Bundle();
            bundle.putInt("listenerType", listenerType);
            bundle.putString("title", title);
            bundle.putString("msg", msg);
            mDialog.setArguments(bundle);
        }

        return mDialog;
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
        //bundleから値を取り出す
        String title = getArguments().getString("title");
        String msg = getArguments().getString("msg");

        //ダイアログ生成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);

        //ボタンにイベントをセット
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickSampleDialogOk();
                }
                getDialog().dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickSampleDialogCancel();
                }
                getDialog().dismiss();
            }
        });

        return builder.create();
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
        public void onClickSampleDialogOk();

        public void onClickSampleDialogCancel();
    }
}
