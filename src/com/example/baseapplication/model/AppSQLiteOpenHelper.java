package com.example.baseapplication.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelperのラッパークラス
 * 
 * @access public
 */
public class AppSQLiteOpenHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "naibo.db";        //データベース名
    public static final int DB_VERSION = 1;                 //データベースバージョン

    private static AppSQLiteOpenHelper mSingleton = null;

    private Context mContext;                               //コンテキスト

    /**
     * コンストラクタ
     * 
     * @param Context context コンテキスト
     * @access public
     */
    public AppSQLiteOpenHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);

        mContext = context;
    }

    /**
     * インスタンスを返す
     * 
     * @param Context context コンテキスト
     * @access public
     */
    public static synchronized AppSQLiteOpenHelper getInstance(Context context)
    {
        if (mSingleton == null) {
            mSingleton = new AppSQLiteOpenHelper(context);
        }

        return mSingleton;
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
            SampleDao sampleDao = new SampleDao(mContext);
            sampleDao.createTable(db);

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