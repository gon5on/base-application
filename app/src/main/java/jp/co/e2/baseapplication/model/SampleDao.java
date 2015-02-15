package jp.co.e2.baseapplication.model;

import java.util.ArrayList;

import jp.co.e2.baseapplication.common.DateHelper;
import jp.co.e2.baseapplication.entity.SampleEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * サンプルテーブルへのデータアクセスオブジェクト
 */
public class SampleDao extends BaseDao {
    // テーブル名
    public static final String TABLE_NAME = "samples";

    // カラム名
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SAMPLE1 = "sample1";
    public static final String COLUMN_SAMPLE2 = "sample2";
    public static final String COLUMN_SAMPLE3 = "sample3";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_MODIFIED = "modified";

    //CREATE TABLE文
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + "               INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_SAMPLE1 + "          TEXT," +
            COLUMN_SAMPLE2 + "          TEXT," +
            COLUMN_SAMPLE3 + "          TEXT," +
            COLUMN_CREATED + "          TEXT                NOT NULL," +
            COLUMN_MODIFIED + "         TEXT                NOT NULL" +
            ")";

    private Context mContext;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public SampleDao(Context context) {
        mContext = context;
    }

    /**
     * インサート・アップデート
     *
     * @param db データベースオブジェクト
     * @param data 保存データ
     * @return Boolean
     */
    public Boolean save(SQLiteDatabase db, SampleEntity data) throws Exception {
        long ret;

        ContentValues cv = new ContentValues();
        put(cv, COLUMN_SAMPLE1, data.getSample1());
        put(cv, COLUMN_SAMPLE2, data.getSample2());
        put(cv, COLUMN_SAMPLE3, data.getSample3());
        put(cv, COLUMN_MODIFIED, new DateHelper().format(DateHelper.FMT_DATETIME));

        if (data.getId() == null) {
            put(cv, COLUMN_CREATED, new DateHelper().format(DateHelper.FMT_DATETIME));
            ret = db.insert(TABLE_NAME, "", cv);
        } else {
            String[] param = new String[]{String.valueOf(data.getId())};
            ret = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", param);
        }

        return (ret != -1);
    }

    /**
     * IDからデータを取得する
     *
     * @param db データベースオブジェクト
     * @param id ID
     * @return SampleEntity values
     */
    public SampleEntity findById(SQLiteDatabase db, Integer id) {
        SampleEntity data = new SampleEntity();

        String sql = String.format("SELECT * FROM %s ", TABLE_NAME);
        sql += String.format("WHERE %s = ?", COLUMN_ID);

        String[] param = new String[]{String.valueOf(id)};

        Cursor cursor = db.rawQuery(sql, param);

        if (cursor.moveToFirst()) {
            do {
                data.setId(getInteger(cursor, COLUMN_ID));
                data.setSample1(getString(cursor, COLUMN_SAMPLE1));
                data.setSample2(getString(cursor, COLUMN_SAMPLE2));
                data.setSample3(getString(cursor, COLUMN_SAMPLE3));
                data.setCreated(getString(cursor, COLUMN_CREATED));
                data.setModified(getString(cursor, COLUMN_MODIFIED));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return data;
    }

    /**
     * 全件データを取得する
     *
     * @param db データベースオブジェクト
     * @return ArrayList<SampleEntity> data
     */
    public ArrayList<SampleEntity> findAll(SQLiteDatabase db) {
        ArrayList<SampleEntity> data = new ArrayList<SampleEntity>();

        String sql = String.format("SELECT * FROM %s ", TABLE_NAME);

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                SampleEntity values = new SampleEntity();
                values.setId(getInteger(cursor, COLUMN_ID));
                values.setSample1(getString(cursor, COLUMN_SAMPLE1));
                values.setSample2(getString(cursor, COLUMN_SAMPLE2));
                values.setSample3(getString(cursor, COLUMN_SAMPLE3));
                values.setCreated(getString(cursor, COLUMN_CREATED));
                values.setModified(getString(cursor, COLUMN_MODIFIED));
                data.add(values);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return data;
    }

    /**
     * レコード全削除
     *
     * @return boolean
     */
    public boolean deleteAll(SQLiteDatabase db) {
        Integer ret = db.delete(TABLE_NAME, null, null);

        return (ret != -1);
    }

    /**
     * IDからレコード削除
     *
     * @return boolean
     */
    public boolean deleteById(SQLiteDatabase db, Integer id) {
        String[] param = new String[] {String.valueOf(id)};

        Integer ret = db.delete(TABLE_NAME, COLUMN_ID + " = ?", param);

        return (ret != -1);
    }
}
