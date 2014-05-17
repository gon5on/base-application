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
    private CallbackListener mCallbackListener = null;

    /**
     * インスタンスを返す
     * 
     * @String String title
     * @String String msg
     * @return SampleDialog
     * @access public
     */
    public static SampleDialog getInstance(String title, String msg)
    {
        SampleDialog dialog = new SampleDialog();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("msg", msg);
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
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbackListener != null) {
                    mCallbackListener.onClickSampleDialogCancel();
                }
                dismiss();
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
        }
        else if (listenerType == AppDialog.LISTENER_FRAGMENT) {
            mCallbackListener = (CallbackListener) getTargetFragment();
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
        public void onClickSampleDialogOk();

        public void onClickSampleDialogCancel();
    }
}
