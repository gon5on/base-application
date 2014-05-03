package com.example.baseapplication.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

        //サーバに接続
        connectServer();
    }

    /**
     * onCreateOptionsMenu
     * 
     * @param Menu menu
     * @return Boolean
     * @access public
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * onOptionsItemSelected
     * 
     * @param MenuItem item
     * @return Boolean
     * @access public
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Integer id = item.getItemId();

        if (id == R.id.action_content1) {
            return true;
        } else if (id == R.id.action_content2) {
            return true;
        } else if (id == R.id.action_content3) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * PlaceholderFragment
     * 
     * @access public
     */
    public static class PlaceholderFragment extends Fragment
    {
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
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
