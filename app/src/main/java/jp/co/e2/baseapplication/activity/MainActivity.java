package jp.co.e2.baseapplication.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.entity.SampleEntity;
import jp.co.e2.baseapplication.model.BaseSQLiteOpenHelper;
import jp.co.e2.baseapplication.model.SampleDao;

/**
 * メインアクテビティ
 */
public class MainActivity extends BaseActivity {
    /**
     * ファクトリーメソッドもどき
     *
     * @param activity アクテビティ
     * @return Intent intent
     */
    public static Intent newIntent(Activity activity) {
        return new Intent(activity, MainActivity.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        //ツールバーセット
        setToolbar();

        //ドロワーセット
        setDrawer(false);

        //フラグメントを呼び出す
        getFragmentManager().beginTransaction().add(R.id.container, PlaceholderFragment.newInstance()).commit();
    }

    /**
     * PlaceholderFragment
     */
    public static class PlaceholderFragment extends Fragment {
        private View mView;

        /**
         * ファクトリーメソッド
         */
        public static PlaceholderFragment newInstance() {
            Bundle args = new Bundle();

            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setArguments(args);

            return fragment;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.fragment_main, container, false);

            return mView;
        }

        /**
         * サンプルデータ一覧を取得
         *
         * @return ArrayList<SampleEntity>
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