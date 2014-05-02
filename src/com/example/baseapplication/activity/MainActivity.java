package com.example.baseapplication.activity;

import android.os.Bundle;

import com.example.baseapplication.R;
import com.example.baseapplication.dialog.SampleDialog;

/**
 * メインアクテビティ
 * 
 * @access public
 */
public class MainActivity extends AppActivity
{
    /**
     * onCreate
     * 
     * @param Bundle savedInstanceState
     * @return void
     * @access protected
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ダイアログ
        SampleDialog sampleDialog = new SampleDialog();
        sampleDialog.show(getFragmentManager(), "dialog");
        sampleDialog.setSampleDialogListener(new SampleDialog.SampleDialogListener() {
            @Override
            public void onClickOk() {
                //OKをクリックされた時の処理
            }

            @Override
            public void onClickCancel() {
                //キャンセルをクリックされた時の処理
            }
        });
    }
}
