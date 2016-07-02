package jp.co.e2.baseapplication.common;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Android独自の便利なものまとめたクラス
 */
public class AndroidUtils {
    /**
     * トースト表示（短い）
     *
     * @param context コンテキスト
     */
    public static void showToastS(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * トースト表示（長い）
     *
     * @param context コンテキスト
     */
    public static void showToastL(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * アプリバージョン名を取得する
     *
     * @param context コンテキスト
     * @return String versionName バージョン名
     */
    public static String getVerName(Context context) {
        String versionName = null;

        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    /**
     * アプリバージョンコードを取得する
     *
     * @param context コンテキスト
     * @return Integer versionCode バージョンコード
     */
    public static Integer getVerCode(Context context) {
        Integer versionCode = null;

        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionCode = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * アプリのマーケットURIを取得する
     *
     * @param context コンテキスト
     * @return Uri マーケットのURI
     */
    public static Uri getMargetUri(Context context) {
        return Uri.parse("market://details?id=" + context.getPackageName());
    }

    /**
     * dp→pixelに変換
     *
     * @param context コンテキスト
     * @param value dp
     * @return Integer pixel
     */
    public static Integer dpToPixel(Context context, Integer value) {
        double doubleValue = value;

        return dpToPixel(context, doubleValue);
    }

    /**
     * dp→pixelに変換
     *
     * @param context コンテキスト
     * @param value dp
     * @return Integer pixel
     */
    public static Integer dpToPixel(Context context, Double value) {
        float density = context.getResources().getDisplayMetrics().density;

        return (int) (value * density + 0.5f);
    }

    /**
     * ウィンドウの横幅を返す
     *
     * @param context コンテキスト
     * @return Integer ウインドウ横幅
     */
    public static Integer getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        return metrics.widthPixels;
    }

    /**
     * ウィンドウの縦幅を返す
     *
     * @param context コンテキスト
     * @return Integer ウインドウ縦幅
     */
    public static Integer getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;
    }

    /**
     * 被らないリソースIDを生成する
     *
     * @return int リソースID
     */
    public static int generateViewId() {
        AtomicInteger nextGeneratedId = new AtomicInteger(1);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (;;) {
                final int result = nextGeneratedId.get();
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1;
                if (nextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

    /**
     * バージョンを見てURIからパスを取得する
     *
     * @param context コンテキスト
     * @param uri URI
     * @return path パス
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(Context context, Uri uri) {
        String path = null;

        //キットカット以降の取得方法で取得する
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            String strDocId = DocumentsContract.getDocumentId(uri);
            String[] strSplitDocId = strDocId.split(":");
            String strId = strSplitDocId[strSplitDocId.length - 1];

            Cursor crsCursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , new String[]{ MediaStore.MediaColumns.DATA }
                    , "_id=?"
                    , new String[]{ strId }
                    , null);
            if (crsCursor != null && crsCursor.moveToFirst()) {
                path = crsCursor.getString(0);
            }
            if (crsCursor != null) {
                crsCursor.close();
            }
        }
        //キットカットより前の取得方法で取得
        else {
            path = getPathFromUriUnderKitKat(context, uri);
        }

        return path;
    }

    /**
     * 【KitKat以前】URIからパスを取得する
     *
     * @param context コンテキスト
     * @param uri URI
     * @return path パス
     */
    public static String getPathFromUriUnderKitKat(Context context, Uri uri) {
        String path = null;

        String[] strColumns = { MediaStore.Images.Media.DATA };
        Cursor crsCursor = context.getContentResolver().query(uri, strColumns, null, null, null);
        if(crsCursor != null && crsCursor.moveToFirst()) {
            path = crsCursor.getString(0);
        }
        if (crsCursor != null) {
            crsCursor.close();
        }

        return path;
    }

    /**
     * パスをコンテンツURIに変換
     *
     * @param context コンテキスト
     * @param path パス
     * @return コンテンツURI
     * @throws NullPointerException
     */
    public static Uri path2contentUri(Context context, String path) throws NullPointerException {
        Uri uri;

        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] project = { BaseColumns._ID };
        String sel = MediaStore.Images.ImageColumns.DATA + " LIKE ?";
        String[] selArgs = new String[] { path };
        ContentResolver cr = context.getContentResolver();

        Cursor cur = cr.query(baseUri, project, sel, selArgs, null);

        if (cur == null) {
            throw new NullPointerException();
        }

        cur.moveToFirst();
        int idx = cur.getColumnIndex(project[0]);
        long id = cur.getLong(idx);
        cur.close();
        uri = Uri.parse(baseUri.toString() + "/" + id);

        if (uri == null) {
            throw new NullPointerException();
        }

        return uri;
    }
}
