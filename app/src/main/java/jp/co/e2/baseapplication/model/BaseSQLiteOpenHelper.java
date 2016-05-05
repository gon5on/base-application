package jp.co.e2.baseapplication.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelperのラッパークラス
 */
public class BaseSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "database.db";             //データベース名
    public static final int DB_VERSION = 1;                         //データベースバージョン

    /**
     * コンストラクタ
     */
    public BaseSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.beginTransaction();

            dbVer1(db);
            dbVer2(db);
            dbVer3(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //バージョンが一緒であれば何もしない
        if (oldVersion == newVersion) {
            return;
        }

        try {
            db.beginTransaction();

            if (oldVersion < 2) {
                dbVer2(db);
            }
            if (oldVersion < 3) {
                dbVer3(db);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * DBバージョンが1の時のDB変更
     *
     * @param db データベースオブジェクト
     */
    private void dbVer1(SQLiteDatabase db) throws Exception {
        db.execSQL(SampleDao.CREATE_TABLE_SQL);
    }

    /**
     * DBバージョンが2に上がった時のDB変更
     *
     * @param db データベースオブジェクト
     */
    private void dbVer2(SQLiteDatabase db) throws Exception {
        //処理を書く
    }

    /**
     * DBバージョンが3に上がった時のDB変更
     *
     * @param db データベースオブジェクト
     */
    private void dbVer3(SQLiteDatabase db) throws Exception {
        //処理を書く
    }
}