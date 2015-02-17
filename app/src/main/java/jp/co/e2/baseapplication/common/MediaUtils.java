package jp.co.e2.baseapplication.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

/**
 * メディア・ファイル系の便利なものをまとめたクラス
 */
public class MediaUtils {
    /**
     * URIからファイルパスを取得する
     *
     * @param context コンテキスト
     * @param uri URI
     * @return String path ファイルパス
     */
    public static String getPathFromUri(Context context, Uri uri) {
        String path = null;

        if (uri != null) {
            if ("content".equals(uri.getScheme())) {
                String[] param = {android.provider.MediaStore.Images.ImageColumns.DATA};
                Cursor cursor = context.getContentResolver().query(uri, param, null, null, null);
                cursor.moveToFirst();
                path = cursor.getString(0);
                cursor.close();
            } else {
                path = uri.getPath();
            }
        }

        return path;
    }

    /**
     * 外部ストレージが使用できるかどうか
     *
     * @return boolean 外部ストレージが使える/使えない
     */
    public static boolean IsExternalStorageAvailableAndWriteable() {
        boolean externalStorageAvailable = false;
        boolean externalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            externalStorageAvailable = externalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            externalStorageAvailable = true;
            externalStorageWriteable = false;
        } else {
            externalStorageAvailable = externalStorageWriteable = false;
        }

        return externalStorageAvailable && externalStorageWriteable;
    }

    /**
     * メディアスキャンを実行
     *
     * 4.4から使用できないので、使わないこと！
     *
     * @param context コンテキスト
     */
    @Deprecated
    public static void mediaScan(Context context) {
        String url = "file://" + Environment.getExternalStorageDirectory();
        Uri uri = Uri.parse(url);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, uri));
    }

    /**
     * ファイル/ディレクトリを削除する（中身があってもOK）
     *
     * @param file ファイル/ディレクトリオブジェクト
     */
    public static void deleteDirFile(File file) throws IOException {
        if (!file.exists()) {
            return;
        }

        if (file.isFile()) {
            if (!file.delete()) {
                throw new IOException("failed to delete the file");
            }
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File tmp : files) {
                deleteDirFile(tmp);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete the file");
            }
        }
    }

    /**
     * ファイル/ディレクトリを削除する（中身があってもOK）
     *
     * @param path ファイル/ディレクトリパス
     */
    public static void deleteDirFile(String path) throws IOException {
        deleteDirFile(new File(path));
    }

    /**
     * ファイルパスからファイル名を返す
     *
     * @param path ファイルパス
     * @return String
     */
    public static String geFileName(String path) throws FileNotFoundException {
        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException("File not found");
        }

        return file.getName();
    }

    /**
     * ファイルパスから拡張子を返す
     *
     * @param path ファイルパス
     * @return String
     */
    public static String getFileExt(String path) throws FileNotFoundException {
        String name = geFileName(path);

        if (name == null) {
            return null;
        }

        int lastDotPosition = name.lastIndexOf(".");
        if (lastDotPosition != -1) {
            return name.substring(lastDotPosition + 1);
        }

        return null;
    }

    /**
     * ファイルパスからMIMEタイプを返す
     *
     * @param path ファイルパス
     * @return String
     */
    public static String getMimeType(String path) throws FileNotFoundException {
        String ext = getFileExt(path);

        if (ext == null) {
            return null;
        }

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
    }

    /**
     * バイナリファイルを返す
     *
     * @param path ファイルパス
     * @return File
     * @throws FileNotFoundException
     */
    public static File getBinaryFile(String path) throws FileNotFoundException {
        File file = new File(path);

        if (!file.exists()) {
            throw new FileNotFoundException("file not found");
        }

        return file;
    }
}