package com.example.baseapplication.dialog;

import java.util.EventListener;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.example.baseapplication.R;

/**
 * サンプルダイアログ
 * 
 * @access public
 */
public class SampleDialog extends DialogFragment
{
    private SampleDialogListener mListener = null;

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
        Dialog dialog = new Dialog(getActivity());

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_sample);

        // OK ボタン
        dialog.findViewById(R.id.buttonOk).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickOk();
                dismiss();
            }
        });

        // Cancelボタン
        dialog.findViewById(R.id.buttonCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickCancel();
                dismiss();
            }
        });

        return dialog;
    }

    /**
     * イベントリスナーを追加
     * 
     * @param SampleDialogListener listener
     * @return void
     * @access public
     */
    public void setSampleDialogListener(SampleDialogListener listener)
    {
        mListener = listener;
    }

    /**
     * イベントリスナーを削除
     * 
     * @return void
     * @access public
     */
    public void removeDialogListener()
    {
        mListener = null;
    }

    /**
     * イベントリスナー
     * 
     * @return void
     * @access public
     */
    public interface SampleDialogListener extends EventListener
    {
        /**
         * OKボタンが押されたイベントを通知
         */
        public void onClickOk();

        /**
         * Cancelボタンが押されたイベントを通知
         */
        public void onClickCancel();
    }
}