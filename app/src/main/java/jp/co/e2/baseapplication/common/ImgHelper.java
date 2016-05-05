package jp.co.e2.baseapplication.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 画像読み込み用クラス
 */
public class ImgHelper {
    private String mPath;
    private Bitmap mBitmap;

    /**
     * コンストラクタ
     *
     * @param path ファイルパス
     */
    public ImgHelper(String path) {
        mPath = path;
    }

    /**
     * 回転を考慮して、リサイズした画像を返す
     *
     * @param height 読み込む高さ
     * @param width 読み込む幅
     * @throws IOException
     */
    public Bitmap getRotatedResizedImage(int height, int width) throws IOException {
        File file = new File(mPath);

        //ファイルが存在しない
        if (!file.exists()) {
            throw new NullPointerException();
        }

        //回転と読み込みサイズを計算
        Matrix matrix = new Matrix();
        matrix = getResizedMatrix(file, matrix);
        matrix = getRotatedMatrix(file, matrix);

        // 元画像の取得
        Bitmap originalBitmap = getPreResizeBitmap(height, width);
        int origHeight = originalBitmap.getHeight();
        int origWidth = originalBitmap.getWidth();

        // マトリクスをつけることで縮小、向きを反映した画像を生成
        mBitmap =  Bitmap.createBitmap(originalBitmap, 0, 0, origWidth, origHeight, matrix, true);

        return mBitmap;
    }

    /**
     * 画像を保存する
     *
     * @param context コンテキスト
     * @param savePath 保存画像パス
     * @throws IOException
     */
    public void saveImg(Context context, String savePath) throws IOException {
        //保存
        File destination = new File(savePath);
        FileOutputStream outputStream = new FileOutputStream(destination);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();

        //ギャラリーのスキャン
        String path[] = new String[]{ savePath };
        String type[] = new String[]{ "image/jpeg" };
        MediaScannerConnection.scanFile(context, path, type, null);

        //ギャラリーのスキャンに微妙に時間がかかる場合があるので、スリープさせる
        try{
            Thread.sleep(300);
        } catch (InterruptedException e){
            //何もしない
        }
    }

    /**
     * リサイズするマトリクスを取得
     * 縮小の場合のみ、縮小のマトリクスをセットして返す
     *
     * @param file 入力画像
     * @param matrix 元のマトリクス
     * @return matrix リサイズ後のマトリクス
     */
    private Matrix getResizedMatrix(File file, Matrix matrix) {
        // リサイズチェック用にメタデータ読み込み
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        int height = options.outHeight;
        int width = options.outWidth;

        // リサイズ比の取得
        float scale = Math.max((float) 500 / width, (float) 500 / height);

        // 縮小のみのため、scaleは1.0未満の場合のみマトリクス設定
        if (scale < 1.0) {
            matrix.postScale(scale, scale);
        }

        return matrix;
    }

    /**
     * 画像の回転後のマトリクスを取得
     *
     * @param file 入力画像
     * @param matrix 元のマトリクス
     * @return matrix 回転後のマトリクス
     */
    private Matrix getRotatedMatrix(File file, Matrix matrix){
        ExifInterface exifInterface;

        try {
            exifInterface = new ExifInterface(file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return matrix;
        }

        // 画像の向きを取得
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        // 画像を回転させる処理をマトリクスに追加
        switch (orientation) {
            case ExifInterface.ORIENTATION_UNDEFINED:
                break;
            case ExifInterface.ORIENTATION_NORMAL:
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                // 水平方向にリフレクト
                matrix.postScale(-1f, 1f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                // 180度回転
                matrix.postRotate(180f);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                // 垂直方向にリフレクト
                matrix.postScale(1f, -1f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                // 反時計回り90度回転
                matrix.postRotate(90f);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                // 時計回り90度回転し、垂直方向にリフレクト
                matrix.postRotate(-90f);
                matrix.postScale(1f, -1f);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                // 反時計回り90度回転し、垂直方向にリフレクト
                matrix.postRotate(90f);
                matrix.postScale(1f, -1f);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                // 反時計回りに270度回転（時計回りに90度回転）
                matrix.postRotate(-90f);
                break;
        }

        return matrix;
    }

    /**
     * リサイズしたいサイズに一番近い2のべき乗で読み込んだビットマップ画像を返す
     * （out of memory対策）
     *
     * @param height 高さピクセル
     * @param width  幅ピクセル
     * @return Bitmap
     * @throws IOException
     */
    private Bitmap getPreResizeBitmap(int height, int width) throws IOException {
        //ファイルが存在しない
        if (!new File(mPath).exists()) {
            throw new FileNotFoundException();
        }

        //画像ファイル自体は読み込まずに、高さなどのプロパティのみを取得する
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPath, options);

        //縮小比率を取得する
        options.inSampleSize = calcScale(options, height, width);

        //リサイズしたビットマップを作成
        options.inJustDecodeBounds = false;
        Bitmap resizeBitmap = BitmapFactory.decodeFile(mPath, options);

        //画像が読み込めていなければ例外を返す
        if (resizeBitmap == null) {
            throw new IOException();
        }

        return resizeBitmap;
    }

    /**
     * オリジナルとリサイズ後の画像高さ・幅から縮小比率を取得する
     *
     * @param options オリジナル画像の縦横幅セット
     * @param reHeight リサイズ後の高さ
     * @param reWidth リサイズ後の幅
     * @return Integer inSampleSize 縮小比率
     */
    private int calcScale(BitmapFactory.Options options, Integer reHeight, Integer reWidth) {
        Integer oriHeight = options.outHeight;
        Integer oriWidth = options.outWidth;
        Integer scale = 1;

        if (oriHeight > reHeight || oriWidth > reWidth) {
            if (oriHeight > oriWidth) {
                scale = Math.round((float) oriHeight / (float) reHeight);
            } else {
                scale = Math.round((float) oriWidth / (float) reWidth);
            }
        }

        return scale;
    }
}
