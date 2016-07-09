package jp.co.e2.baseapplication.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;

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
    private CallbackListener mListener;

    /**
     * コンストラクタ
     *
     * @param path ファイルパス
     */
    public ImgHelper(String path) {
        mPath = path;
    }

    /**
     * コールバックリスナーをセットする
     *
     * @param listener コールバックリスナー
     */
    public void setCallbackListener(CallbackListener listener) {
        mListener = listener;
    }

    /**
     * 回転を考慮して、リサイズした画像を返す
     *
     * 画像の縦横長い辺が、引数のsizeを超えないように比率を保ってリサイズする
     *
     * @param reSize リサイズしたいの縦横長い方どちらかの長さ
     * @throws IOException
     */
    public Bitmap getRotatedResizedImage(int reSize) throws IOException {
        return getRotatedResizedImage(reSize, reSize);
    }

    /**
     * 回転を考慮して、リサイズした画像を返す
     *
     * 画像の縦が長い辺であれば、引数のreHeightを、
     * 画像の横が長い辺であれば、引数のreWidthを、超えないように比率を保ってリサイズする
     *
     * @param reHeight リサイズしたい高さ
     * @param reWidth リサイズしたい幅
     * @throws IOException
     */
    public Bitmap getRotatedResizedImage(int reHeight, int reWidth) throws IOException {
        File file = new File(mPath);

        //ファイルが存在しない
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        //ある程度縮小した元画像を取得
        Bitmap originalBitmap = getPreResizeBitmap(reHeight, reWidth);

        //読み込みサイズを考慮したマトリクスを作成
        Matrix matrix = new Matrix();
        int origHeight = originalBitmap.getHeight();
        int origWidth = originalBitmap.getWidth();
        matrix = getResizedMatrix(matrix, origHeight, origWidth, reHeight, reWidth);

        //回転を考慮したマトリクスを作成
        matrix = getRotatedMatrix(matrix);

        // マトリクスをつけることで縮小、向きを反映した画像を生成
        mBitmap =  Bitmap.createBitmap(originalBitmap, 0, 0, origWidth, origHeight, matrix, true);

        LogUtils.d(mBitmap.getHeight());
        LogUtils.d(mBitmap.getWidth());

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
        if (mBitmap == null) {
            throw new NullPointerException();
        }

        //保存
        File destination = new File(savePath);
        FileOutputStream outputStream = new FileOutputStream(destination);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();

        //ギャラリーのスキャン
        String path[] = new String[]{ savePath };
        String type[] = new String[]{ "image/jpeg" };
        MediaScannerConnection.scanFile(context, path, type, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                if (mListener != null) {
                    mListener.onScanCompleted(uri);
                }
            }
        });
    }

    /**
     * bitmapをリサイクルする
     */
    public void bitmapRecycle() {
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }

    /**
     * リサイズするマトリクスを取得
     * 縮小の場合のみ、縮小のマトリクスをセットして返す
     *
     * 画像の縦が長い辺であれば、引数のreHeightを、
     * 画像の横が長い辺であれば、引数のreWidthを、超えないように比率を保ってマトリクスを計算する
     *
     * @param matrix のマトリクス
     * @param orgHeight 元の高さ
     * @param orgWidth 元の幅
     * @param reHeight リサイズしたい高さ
     * @param reWidth リサイズしたい幅
     * @return matrix リサイズ後のマトリクス
     */
    private Matrix getResizedMatrix(Matrix matrix, int orgHeight, int orgWidth, int reHeight, int reWidth) {
        // リサイズ比の取得
        float scale = Math.min((float) reWidth / orgWidth, (float) reHeight / orgHeight);

        // 縮小のみのため、scaleは1.0未満の場合のみマトリクス設定
        if (scale < 1.0) {
            matrix.postScale(scale, scale);
        }

        return matrix;
    }

    /**
     * 画像の回転後のマトリクスを取得
     *
     * @param matrix 元のマトリクス
     * @return matrix 回転後のマトリクス
     */
    private Matrix getRotatedMatrix(Matrix matrix){
        ExifInterface exifInterface;

        try {
            exifInterface = new ExifInterface(mPath);
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
     * @param reHeight リサイズしたい高さピクセル
     * @param reWidth  リサイズしたい幅ピクセル
     * @return Bitmap
     * @throws IOException
     */
    private Bitmap getPreResizeBitmap(int reHeight, int reWidth) throws IOException {
        //ファイルが存在しない
        if (!new File(mPath).exists()) {
            throw new FileNotFoundException();
        }

        //画像ファイル自体は読み込まずに、高さなどのプロパティのみを取得する
        BitmapFactory.Options options = getImgProperty();

        //縮小比率を取得する
        options.inSampleSize = calcScale(options, reHeight, reWidth);

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
     * @param reHeight リサイズしたいの高さ
     * @param reWidth リサイズしたいの幅
     * @return Integer scale 縮小比率
     */
    private int calcScale(BitmapFactory.Options options, Integer reHeight, Integer reWidth) {
        final int orgHeight = options.outHeight;
        final int orgWidth = options.outWidth;
        int scale = 1;

        if (orgHeight > reHeight || orgWidth > reWidth) {
            if (orgWidth > orgHeight) {
                scale = Math.round((float) orgHeight / (float) reHeight);
            } else {
                scale = Math.round((float) orgWidth / (float) reWidth);
            }
        }

        return scale;
    }

    /**
     * 画像のプロパティを取得する
     *
     * 画像ファイル自体は読み込まずに、プロパティのみ取得する
     *
     * @return options プロパティ
     */
    private BitmapFactory.Options getImgProperty() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mPath, options);

        return options;
    }

    /**
     * コールバックリスナー
     */
    public interface CallbackListener {
        /**
         * ギャラリーのスキャンが完了した
         *
         * @param uri 保存した画像のURI
         */
        void onScanCompleted(Uri uri);
    }
}