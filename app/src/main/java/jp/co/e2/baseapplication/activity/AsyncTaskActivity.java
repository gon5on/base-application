package jp.co.e2.baseapplication.activity;

import java.util.ArrayList;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.HttpHelper;
import jp.co.e2.baseapplication.dialog.AppProgressDialog;
import jp.co.e2.baseapplication.dialog.SampleDialog;
import jp.co.e2.baseapplication.entity.SampleEntity;
import jp.co.e2.baseapplication.model.BaseSQLiteOpenHelper;
import jp.co.e2.baseapplication.model.SampleDao;
import jp.co.e2.baseapplication.asynctask.BaseAsyncTask.AsyncTaskCallbackListener;
import jp.co.e2.baseapplication.asynctask.SampleAsyncTask;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 非同期処理アクテビティ
 *
 * @access public
 */
public class AsyncTaskActivity extends BaseActivity {
    private static final int DIALOG_TAG = 1;
    private static final int SAMPLE_ASYNC_TASK_TAG = 1;

    /**
     * onCreate
     *
     * @param savedInstanceState
     * @return void
     * @access protected
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new AsyncTaskFragment()).commit();
        }
    }

    /**
     * AsyncTaskFragment
     *
     * @access public
     */
    public static class AsyncTaskFragment extends Fragment implements SampleDialog.CallbackListener, AppProgressDialog.CallbackListener, AsyncTaskCallbackListener<Integer, HttpHelper> {
        private AppProgressDialog mAppProgressDialog = null;
        private SampleAsyncTask mSampleAsyncTask = null;

        /**
         * コンストラクタ
         *
         * @access public
         */
        public AsyncTaskFragment() {
        }

        /**
         * onCreateView
         *
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return View
         * @access public
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View view = inflater.inflate(R.layout.fragment_asynctask, container, false);

            // fragment再生成抑止、必要に応じて
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
        private ArrayList<SampleEntity> getSampleData() {
            ArrayList<SampleEntity> data = null;
            SQLiteDatabase db = null;

            try {
                BaseSQLiteOpenHelper helper = new BaseSQLiteOpenHelper(getActivity());
                db = helper.getWritableDatabase();

                SampleDao petDao = new SampleDao(getActivity());
                data = petDao.findAll(db);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (db != null) {
                    db.close();
                }
            }

            return data;
        }

        /**
         * サーバに接続
         *
         * @return void
         * @access private
         */
        private void connectServer() {
            //非同期処理
            mSampleAsyncTask = new SampleAsyncTask(SAMPLE_ASYNC_TASK_TAG, getActivity());
            mSampleAsyncTask.setCallbackListener(this);
            mSampleAsyncTask.execute();
        }

        /**
         * サンプルダイアログ表示
         *
         * @param status
         * @return void
         * @access private
         */
        private void showSampleDialog(Integer status) {
            SampleDialog sampleDialog = SampleDialog.getInstance(DIALOG_TAG, "エラー", "エラーが発生しました（" + status + "）");
            sampleDialog.setCallbackListener(this);
            sampleDialog.show(getFragmentManager(), "dialog");
        }

        /**
         * 非同期前処理
         *
         * @param tag
         * @return void
         * @access public
         */
        @Override
        public void onPreExecute(int tag) {
            mAppProgressDialog = AppProgressDialog.getInstance("通信中");
            mAppProgressDialog.setCallbackListener(this);
            mAppProgressDialog.show(getFragmentManager(), "dialog");
        }

        /**
         * 非同期中更新処理
         *
         * @param tag
         * @param values
         * @return void
         * @access public
         */
        @Override
        public void onProgressUpdate(int tag, Integer... values) {
        }

        /**
         * 非同期キャンセル処理
         *
         * @param tag
         * @return void
         * @access public
         */
        @Override
        public void onCancelled(int tag) {
            if (mAppProgressDialog != null) {
                mAppProgressDialog.dismiss();
            }

            AndroidUtils.showToastS(getActivity().getApplicationContext(), "キャンセルしました。");
        }

        /**
         * 非同期後処理
         *
         * @param tag
         * @param http
         * @return void
         * @access public
         */
        @Override
        public void onPostExecute(int tag, HttpHelper http) {
            if (mAppProgressDialog != null) {
                mAppProgressDialog.dismiss();
            }

            if (http.getStatus() == HttpHelper.STATUS_OK) {
                AndroidUtils.showToastS(getActivity().getApplicationContext(), "通信が終了しました。");
            } else {
                showSampleDialog(http.getHttpStatus());
            }
        }

        /**
         * プログレスダイアログでキャンセルされた
         *
         * @return void
         * @access public
         */
        @Override
        public void onProgressDialogCancel() {
            if (mSampleAsyncTask != null) {
                mSampleAsyncTask.cancel(true);
            }
        }

        /**
         * サンプルダイアログでOKボタンが押された
         *
         * @return void
         * @access public
         */
        @Override
        public void onClickSampleDialogOk(int tag) {
            connectServer();        //リトライ
        }

        /**
         * サンプルダイアログでキャンセルボタンが押された
         *
         * @return void
         * @access public
         */
        @Override
        public void onClickSampleDialogCancel(int tag) {
        }
    }
}