package com.example.baseapplication.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.baseapplication.common.DateUtils;

/**
 * サンプルテーブルへのデータアクセスオブジェクト
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

    //CREATE TABLE文
    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + "               INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_SAMPLE1 + "          TEXT," +
                    COLUMN_SAMPLE2 + "          TEXT," +
                    COLUMN_SAMPLE3 + "          TEXT," +
                    COLUMN_CREATED + "          TEXT                NOT NULL," +
                    COLUMN_MODIFIED + "         TEXT                NOT NULL" +
                    ")";

    private Context mContext;                                           //コンテキスト

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
     * インサート・アップデート
     * 
     * @param SQLiteDatabase db
     * @param SampleEntity data
     * @return void
     * @access public
     */
    public boolean save(SQLiteDatabase db, SampleEntity data) throws Exception
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
     * @return SampleEntity values
     * @access public
     */
    public SampleEntity findById(SQLiteDatabase db, Integer id)
    {
        SampleEntity data = new SampleEntity();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SELECT * FROM %s ", TABLE_NAME));
        sb.append(String.format("WHERE %s = ?", COLUMN_ID));

        String[] param = new String[] { String.valueOf(id) };

        Cursor cursor = db.rawQuery(sb.toString(), param);

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
     * @param SQLiteDatabase db
     * @return ArrayList<SampleEntity> data
     * @access public
     */
    public ArrayList<SampleEntity> findAll(SQLiteDatabase db)
    {
        ArrayList<SampleEntity> data = new ArrayList<SampleEntity>();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SELECT * FROM %s ", TABLE_NAME));

        Cursor cursor = db.rawQuery(sb.toString(), null);

        if (cursor.moveToFirst()) {
            SampleEntity values = new SampleEntity();

            do {
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
     * @return void
     * @access public
     */
    public void deleteAll(SQLiteDatabase db)
    {
        db.delete(TABLE_NAME, null, null);
    }
}
