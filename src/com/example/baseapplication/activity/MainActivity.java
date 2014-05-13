package com.example.baseapplication.activity;

import java.util.ArrayList;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.baseapplication.R;
import com.example.baseapplication.common.AndroidUtils;
import com.example.baseapplication.dialog.AppDialog;
import com.example.baseapplication.dialog.AppProgressDialog;
import com.example.baseapplication.dialog.SampleDialog;
import com.example.baseapplication.model.AppSQLiteOpenHelper;
import com.example.baseapplication.model.SampleDao;
import com.example.baseapplication.model.SampleEntity;
import com.example.baseapplication.module.SampleAsyncTask;

/**
 * メインアクテビティ
 * 
 * @access public
 */
public class MainActivity extends AppActivity implements SampleDialog.CallbackListener
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
            getFragmentManager().beginTransaction().add(R.id.container, new MainFragment()).commit();
        }
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
            SampleDialog sampleDialog = SampleDialog.getInstance(AppDialog.LISTENER_ACTIVITY, "サンプル", "サンプルダイアログです");
            sampleDialog.show(getFragmentManager(), "dialog");
            return true;
        } else if (id == R.id.action_content2) {
            return true;
        } else if (id == R.id.action_content3) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * サンプルダイアログでOKボタンが押された
     * 
     * @access public
     * @return void
     */
    @Override
    public void onClickSampleDialogOk()
    {
        AndroidUtils.showToastS(getApplicationContext(), "OK!");
    }

    /**
     * サンプルダイアログでキャンセルボタンが押された
     * 
     * @access public
     * @return void
     */
    @Override
    public void onClickSampleDialogCancel()
    {
        AndroidUtils.showToastS(getApplicationContext(), "cancel!");
    }

    /**
     * MainFragment
     * 
     * @access public
     */
    public static class MainFragment extends Fragment
            implements SampleDialog.CallbackListener, AppProgressDialog.CallbackListener
    {
        private SampleAsyncTask sampleAsyncTask = null;

        /**
         * コンストラクタ
         * 
         * @access public
         */
        public MainFragment()
        {
        }

        /**
         * onCreateView
         * 
         * @param LayoutInflater inflater
         * @param ViewGroup container
         * @param Bundle savedInstanceState
         * @access public
         * @return View
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            View view = inflater.inflate(R.layout.fragment_main, container, false);

            // fragment再生成抑止
            setRetainInstance(true);

            //サンプルデータ一覧を取得
            getSampleData();

            //ボタンを押したらサーバに接続
            view.findViewById(R.id.button).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    connectServer();
                }
            });

            return view;
        }

        /**
         * サンプルデータ一覧を取得
         * 
         * @return ArrayList<SampleEntity>
         * @access private
         */
        private ArrayList<SampleEntity> getSampleData()
        {
            ArrayList<SampleEntity> data = null;
            SQLiteDatabase db = null;

            try {
                AppSQLiteOpenHelper helper = new AppSQLiteOpenHelper(getActivity());
                db = helper.getWritableDatabase();

                SampleDao petDao = new SampleDao(getActivity());
                data = petDao.findAll(db);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }

            return data;
        }

        /**
         * サーバに接続
         * 
         * @return void
         * @access private
         */
        private void connectServer()
        {
            //プログレスダイアログ
            final AppProgressDialog progressDialog = AppProgressDialog.getInstance(AppDialog.LISTENER_FRAGMENT, "処理中");
            progressDialog.setCallbackListener(this);

            //非同期処理
            sampleAsyncTask = new SampleAsyncTask(getActivity());
            sampleAsyncTask.setCallbackListener(new SampleAsyncTask.CallbackListener() {
                @Override
                public void preExecute() {
                    progressDialog.show(getFragmentManager(), "progress");
                }

                @Override
                public void progressUpdate(Integer progress) {
                }

                @Override
                public void cancel() {
                    progressDialog.getDialog().dismiss();
                    AndroidUtils.showToastS(getActivity(), "cancel!");
                }

                @Override
                public void postExecuteSuccess() {
                    progressDialog.getDialog().dismiss();
                    AndroidUtils.showToastS(getActivity(), "finish and success!");
                }

                @Override
                public void postExecuteFaild(Integer status) {
                    progressDialog.getDialog().dismiss();
                    showSampleDialog(status);
                }
            });
            sampleAsyncTask.execute();
        }

        /**
         * サンプルダイアログ表示
         * 
         * @param Integer status
         * @return void
         * @access private
         */
        private void showSampleDialog(Integer status)
        {
            SampleDialog sampleDialog = SampleDialog.getInstance(AppDialog.LISTENER_FRAGMENT, "エラー", "エラーが発生しました（" + status + "）");
            sampleDialog.setCallbackListener(this);
            sampleDialog.show(getFragmentManager(), "dialog");
        }

        /**
         * サンプルダイアログでOKボタンが押された
         * 
         * @access public
         * @return void
         */
        @Override
        public void onClickSampleDialogOk()
        {
            connectServer();        //リトライ
        }

        /**
         * サンプルダイアログでキャンセルボタンが押された
         * 
         * @access public
         * @return void
         */
        @Override
        public void onClickSampleDialogCancel()
        {
        }

        /**
         * プログレスダイアログでキャンセルされた
         * 
         * @access public
         * @return void
         */
        @Override
        public void onProgressDialogCancel()
        {
            if (sampleAsyncTask != null) {
                sampleAsyncTask.cancel(true);
            }
        }
    }
}