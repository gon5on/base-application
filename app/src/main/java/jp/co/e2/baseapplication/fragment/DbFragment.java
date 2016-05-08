package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.entity.SampleEntity;
import jp.co.e2.baseapplication.model.BaseSQLiteOpenHelper;
import jp.co.e2.baseapplication.model.SampleDao;

/**
 * データベースフラグメント
 *
 * DBにアクセスしてデータをインサート・セレクトするサンプルコード
 * 何度もアクセスするなら、SQLiteDatabaseなどはメンバ変数にしてもいいかもしれない
 */
public class DbFragment extends Fragment {
    private View mView;

    /**
     * ファクトリーメソッド
     *
     * @return fragment フラグメント
     */
    public static DbFragment newInstance() {
        Bundle args = new Bundle();

        DbFragment fragment = new DbFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_db, container, false);

        try {
            showSampleData();
        }
        catch (Exception e) {
            e.printStackTrace();
            AndroidUtils.showToastS(getActivity(), getString(R.string.errorMsgSomethingError));
        }

        return mView;
    }

    /**
     * サンプルデータを表示
     */
    private void showSampleData() throws Exception {
        //DBからデータ取得
        ArrayList<SampleEntity> data = getSampleData();

        if (data.size() == 0) {
            //DBにデータインサート
            insertSampleData();

            //DBから再度データ取得
            data = getSampleData();
        }

        //データ表示
        String str = "";
        for (SampleEntity value : data) {
            str += value.getId() + "\n";
            str += value.getSample1() + "\n";
            str += value.getSample2() + "\n";
            str += value.getSample3() + "\n";
            str += value.getCreated() + "\n";
            str += value.getModified() + "\n\n";
        }

        TextView textViewData = (TextView) mView.findViewById(R.id.textViewData);
        textViewData.setText(str);
    }

    /**
     * サンプルデータ一覧を取得
     *
     * @return ArrayList<SampleEntity>
     */
    private ArrayList<SampleEntity> getSampleData() throws Exception {
        ArrayList<SampleEntity> data = null;
        SQLiteDatabase db = null;

        try {
            BaseSQLiteOpenHelper helper = new BaseSQLiteOpenHelper(getActivity());
            db = helper.getWritableDatabase();

            SampleDao sampleDao = new SampleDao();
            data = sampleDao.findAll(db);

        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return data;
    }

    /**
     * サンプルデータをインサート
     */
    private void insertSampleData() throws Exception {
        SQLiteDatabase db = null;

        try {
            BaseSQLiteOpenHelper helper = new BaseSQLiteOpenHelper(getActivity());
            db = helper.getWritableDatabase();
            SampleDao sampleDao = new SampleDao();

            for (int i = 0; i < 3; i++) {
                SampleEntity data = new SampleEntity();
                data.setSample1("サンプルデータ" + (i + 1) + "_1");
                data.setSample2("サンプルデータ" + (i + 2) + "_2");
                data.setSample3("サンプルデータ" + (i + 3) + "_3");

                sampleDao.save(db, data);
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}