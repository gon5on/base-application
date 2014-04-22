package com.example.baseapplication.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.baseapplication.common.DateUtils;

/**
 * ナイボ値テーブルのデータアクセスオブジェクト
 * 
 * @access public
 */
public class SampleDao extends AppDao
{
    // テーブル名
    public static final String TABLE_NAME = "samples";

    // カラム名
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SAMPLE1 = "sample1";
    public static final String COLUMN_SAMPLE2 = "sample2";
    public static final String COLUMN_SAMPLE3 = "sample3";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_MODIFIED = "modified";

    private Context mContext;                                   //コンテキスト

    /**
     * コンストラクタ
     * 
     * @param Context context コンテキスト
     * @access public
     */
    public SampleDao(Context context)
    {
        mContext = context;
    }

    /**
     * テーブル作成
     * 
     * @param SQLiteDatabase db
     * @return void
     * @access public
     */
    public void createTable(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + "               INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SAMPLE1 + "          TEXT," +
                COLUMN_SAMPLE2 + "          TEXT," +
                COLUMN_SAMPLE3 + "          TEXT," +
                COLUMN_CREATED + "          TEXT                NOT NULL," +
                COLUMN_MODIFIED + "         TEXT                NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    /**
     * インサート・アップデート
     * 
     * @param SQLiteDatabase db
     * @param Sample data
     * @return void
     * @access public
     */
    public boolean save(SQLiteDatabase db, Sample data) throws Exception
    {
        long ret;

        ContentValues cv = new ContentValues();
        put(cv, COLUMN_SAMPLE1, data.getSample1());
        put(cv, COLUMN_SAMPLE2, data.getSample2());
        put(cv, COLUMN_SAMPLE3, data.getSample3());
        put(cv, COLUMN_MODIFIED, new DateUtils().format(DateUtils.FMT_DATETIME));

        if (data.getId() == null) {
            put(cv, COLUMN_CREATED, new DateUtils().format(DateUtils.FMT_DATETIME));
            ret = db.insert(TABLE_NAME, "", cv);
        } else {
            String[] param = new String[] { String.valueOf(data.getId()) };
            ret = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", param);
        }

        return (ret != -1) ? true : false;
    }

    /**
     * IDからデータを取得する
     * 
     * @param SQLiteDatabase db
     * @param Integer id
     * @return Sample values
     * @access public
     */
    public Sample findById(SQLiteDatabase db, Integer id)
    {
        Sample values = new Sample();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SELECT * FROM %s ", TABLE_NAME));
        sb.append(String.format("WHERE %s = ?", COLUMN_ID));

        String[] param = new String[] { String.valueOf(id) };

        Cursor cursor = db.rawQuery(sb.toString(), param);

        if (cursor.moveToFirst()) {
            do {
                values.setId(getInteger(cursor, COLUMN_ID));
                values.setSample1(getString(cursor, COLUMN_SAMPLE1));
                values.setSample2(getString(cursor, COLUMN_SAMPLE2));
                values.setSample3(getString(cursor, COLUMN_SAMPLE3));
                values.setCreated(getString(cursor, COLUMN_CREATED));
                values.setModified(getString(cursor, COLUMN_MODIFIED));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return values;
    }

    /**
     * レコード全削除
     * 
     * @return void
     * @access public
     */
    public void deleteAll(SQLiteDatabase db)
    {
        db.delete(TABLE_NAME, null, null);
    }
}
