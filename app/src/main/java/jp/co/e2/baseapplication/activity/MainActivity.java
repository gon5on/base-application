package jp.co.e2.baseapplication.activity;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.dialog.SampleDialog;
import jp.co.e2.baseapplication.entity.SampleEntity;
import jp.co.e2.baseapplication.model.BaseSQLiteOpenHelper;
import jp.co.e2.baseapplication.model.SampleDao;

import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * メインアクテビティ
 *
 * @access public
 */
public class MainActivity extends BaseActivity implements SampleDialog.CallbackListener {
    private static final int DIALOG_TAG = 1;

    /**
     * ${inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstance()).commit();
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();

        if (id == R.id.action_content1) {
            SampleDialog sampleDialog = SampleDialog.getInstance(DIALOG_TAG, "サンプル", "サンプルダイアログです");
            sampleDialog.setCallbackListener(this);
            sampleDialog.show(getFragmentManager(), "dialog");
            return true;
        } else if (id == R.id.action_content2) {
            startActivity(AsyncTaskActivity.newIntent(MainActivity.this));
            return true;
        } else if (id == R.id.action_content3) {
            startActivity(BillingActivity.newIntent(MainActivity.this));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onClickSampleDialogOk(int tag) {
        AndroidUtils.showToastS(getApplicationContext(), "OK!");
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onClickSampleDialogCancel(int tag) {
        AndroidUtils.showToastS(getApplicationContext(), "cancel!");
    }

    /**
     * PlaceholderFragment
     *
     * @access public
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * ファクトリーメソッド
         *
         * @return PlaceholderFragment fragment
         * @access public
         */
        public static PlaceholderFragment newInstance() {
            Bundle args = new Bundle();

            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(args);

            return fragment;
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            View view = inflater.inflate(R.layout.fragment_main, container, false);

            // fragment再生成抑止、必要に応じて
            setRetainInstance(true);

            //サンプルデータ一覧を取得
            getSampleData();

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
    }
}