package com.example.baseapplication.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelperのラッパークラス
 * 
 * @access public
 */
public class BaseSQLiteOpenHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "database.db";             //データベース名
    public static final int DB_VERSION = 1;                         //データベースバージョン

    /**
     * コンストラクタ
     * 
     * @param Context context コンテキスト
     * @access public
     */
    public BaseSQLiteOpenHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * onCreate
     * 
     * @param SQLiteDatabase db
     * @return void
     * @access public
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try {
            db.beginTransaction();

            //テーブル作成
            db.execSQL(SampleDao.CREATE_TABLE_SQL);

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    /**
     * onUpgrade
     * 
     * @param SQLiteDatabase db
     * @param Integer oldVersion 前のバージョン
     * @param Integer newVersion 新しいバージョン
     * @return void
     * @access public
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //upgrade時に使用
    }
}