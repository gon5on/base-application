package com.example.baseapplication.activity;

import android.os.Bundle;

import com.example.baseapplication.R;
import com.example.baseapplication.dialog.AppProgressDialog;
import com.example.baseapplication.dialog.SampleDialog;
import com.example.baseapplication.module.SampleAsyncTask;

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

        //サーバに接続
        connectServer();
    }

    /**
     * サーバに接続
     * 
     * @return void
     * @access private
     */
    private void connectServer()
    {
        final AppProgressDialog progressDialog = new AppProgressDialog();

        SampleAsyncTask sampleAsyncTask = new SampleAsyncTask(getApplicationContext());
        sampleAsyncTask.setCallback(new SampleAsyncTask.AsyncTaskCallback() {
            @Override
            public void preExecute() {
                progressDialog.show(getFragmentManager(), "progress");
            }

            @Override
            public void progressUpdate(Integer progress) {
            }

            @Override
            public void cancel() {
                progressDialog.dismiss();
            }

            @Override
            public void postExecuteSuccess() {
                progressDialog.dismiss();
            }

            @Override
            public void postExecuteFaild(Integer status) {
                progressDialog.dismiss();
                showErroeDialog();
            }
        });
        sampleAsyncTask.execute();
    }

    /**
     * エラーダイアログ表示
     * 
     * @return void
     * @access private
     */
    private void showErroeDialog()
    {
        SampleDialog sampleDialog = new SampleDialog();
        sampleDialog.show(getFragmentManager(), "dialog");

        sampleDialog.setListener(new SampleDialog.EventListener() {
            @Override
            public void onClickOk() {
                connectServer();        //リトライ
            }

            @Override
            public void onClickCancel() {
            }
        });
    }
}
