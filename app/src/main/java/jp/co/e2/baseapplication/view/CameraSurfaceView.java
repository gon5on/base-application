package jp.co.e2.baseapplication.view;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import jp.co.e2.baseapplication.common.LogUtils;
import jp.co.e2.baseapplication.common.MediaUtils;

/**
 * 独自カメラ
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    public static final int TAKE_PICTURE_SIZE = 1500;                   //撮影画像サイズの最大値（撮影可能サイズの中からこれを超えない一番近いサイズで撮影される）

    public static final int ORIENTATION_DEFAULT = 0;                    //通常の向き
    public static final int ORIENTATION_HORIZONTAL_RIGHT = 90;          //右回転した横向き
    public static final int ORIENTATION_UPSIDE_DOWN = 180;              //さかさまの向き
    public static final int ORIENTATION_HORIZONTAL_LEFT = -90;          //左回転した横向き

    public static final int DEGREE_DEFAULT = 0;                         //回転不要
    public static final int DEGREE_HORIZONTAL_RIGHT = 90;               //90度回転
    public static final int DEGREE_UPSIDE_DOWN = 180;                   //180度回転
    public static final int DEGREE_HORIZONTAL_LEFT = 270;               //270度回転

    private Context mContext;
    private Camera mCamera = null;
    private SurfaceHolder mHolder = null;
    private String mFlashFlg = Parameters.FLASH_MODE_AUTO;
    private boolean mIsProcessing = false;
    private CallbackListener mCallbackListener;
    private SensorManager mSensorManager;

    private int mFrontCameraId = -1;
    private int mBackCameraId = -1;
    private int mCurrentCameraId = -1;

    //傾きセンサ関連
    protected final static double RAD2DEG = 180 / Math.PI;
    private static final int CHANGE_ORIENTATION_DEGREE = 60;            //向きが変わったと判定する角度

    private static final int MATRIX_SIZE = 16;
    float[] mInR = new float[MATRIX_SIZE];
    float[] mOutR = new float[MATRIX_SIZE];
    float[] mI = new float[MATRIX_SIZE];

    float[] mOrientationValues   = new float[3];
    float[] mMagneticValues      = new float[3];
    float[] mAccelerometerValues = new float[3];

    private int mPreOrientation = -1;


    /**
     * コンストラクタ
     *
     * @param context Context
     */
    public CameraSurfaceView(Context context) {
        super(context);
        initialize(context);
    }

    /**
     * コンストラクタ
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    /**
     * 初期化
     *
     * @param context Context
     */
    private void initialize(Context context) {
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        //カメラの数を取得
        int cameraCnt = Camera.getNumberOfCameras();

        //フロントカメラとバックカメラのカメラIDを取得しておく
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < cameraCnt; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mBackCameraId = i;
                mCurrentCameraId = i;
            } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mFrontCameraId = i;
            }
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera = Camera.open(mCurrentCameraId);
            mCamera.setPreviewDisplay(holder);

            //撮影サイズ設定
            setPictureSize();

            //プレビューサイズ設定
            setPreviewSize();

            //傾きセンサー
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);

        } catch (Exception e) {
            e.printStackTrace();

            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }

            if (mCallbackListener != null) {
                mCallbackListener.unknownException();
            }
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            mCamera.stopPreview();

            //縦画面設定
            setPortrait();

            mCamera.startPreview();
        }
        catch (Exception e) {
            e.printStackTrace();

            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
            }

            if (mCallbackListener != null) {
                mCallbackListener.unknownException();
            }
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        mIsProcessing = false;
        mSensorManager.unregisterListener(this);
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            //1本の指でタッチされたときは、オートフォーカス機能を呼び出す
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mCamera.autoFocus(null);
            }
        }
        //オートフォーカス失敗
        catch(RuntimeException e) {
            if (mCallbackListener != null) {
                mCallbackListener.autoFocusFailed();
            }
        }

        return true;
    }

    /**
     * 撮影サイズ設定
     */
    public void setPictureSize(){
        //端末がサポートするサイズ一覧取得
        Parameters params = mCamera.getParameters();
        List<Size> sizes = params.getSupportedPictureSizes();

        //指定サイズを超えない最大の撮影サイズを設定する
        if (sizes != null && sizes.size() > 0) {
            Size setSize = sizes.get(0);
            for (Size size : sizes){
                if (Math.max(size.width, size.height) <= TAKE_PICTURE_SIZE) {
                    setSize = size;
                    break;
                }
            }

            LogUtils.d("picture width: " + setSize.width);
            LogUtils.d("picture height: " + setSize.height);

            params.setPictureSize(setSize.width, setSize.height);
            mCamera.setParameters(params);
        }
    }

    /**
     * プレビューサイズ設定
     */
    public void setPreviewSize(){
        Parameters cameraParam = mCamera.getParameters();

        Size previewSize = getOptimalPreviewSize(cameraParam.getSupportedPreviewSizes(), getHeight(), getWidth());
        cameraParam.setPreviewSize(previewSize.width, previewSize.height);

        LogUtils.d("previous width: " + previewSize.width);
        LogUtils.d("previous height: " + previewSize.height);

        mCamera.setParameters(cameraParam);
    }

    /**
     * 縦画面設定
     */
    private void setPortrait() {
        Parameters cameraParam = mCamera.getParameters();

        if (isPortrait()) {
            mCamera.setDisplayOrientation(90);
            cameraParam.setRotation(90);
        } else {
            mCamera.setDisplayOrientation(0);
            cameraParam.setRotation(0);
        }

        mCamera.setParameters(cameraParam);
    }

    /**
     * 縦画面かどうか
     *
     * @return boolean
     */
    private boolean isPortrait() {
        return (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    /**
     * シャッターが押された
     */
    public void onClickShutterButton() {
        //連打対策
        if (mIsProcessing) {
            return;
        }
        mIsProcessing = true;

        //フラッシュセット
        setFlash();

        //オートフォーカス後、写真を撮る
        try {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mCamera.takePicture(mShutterListener, null, mPictureCallback);
                            } catch (RuntimeException e) {
                                mIsProcessing = false;
                                e.printStackTrace();

                                if (mCallbackListener != null) {
                                    mCallbackListener.unknownException();
                                }
                            }
                        }
                    }).start();
                }
            });
        } catch (RuntimeException e) {
            mIsProcessing = false;
            e.printStackTrace();

            if (mCallbackListener != null) {
                mCallbackListener.unknownException();
            }
        }
    }

    /**
     * プレビュー用のサイズを取得する
     *
     * @param sizes プレビュー可能サイズ一覧
     * @param w 幅
     * @param h 高さ
     * @return Size
     */
    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;

        if (sizes == null) {
            return null;
        }

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;

        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

    /**
     * フラッシュをセットする
     */
    private void setFlash() {
        try {
            if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                return;
            }

            Parameters cameraParam = mCamera.getParameters();
            cameraParam.setFlashMode(mFlashFlg);
            mCamera.setParameters(cameraParam);
        }
        catch (Exception e) {
            e.printStackTrace();

            if (mCallbackListener != null) {
                mCallbackListener.unknownException();
            }
        }
    }

    /**
     * カメラ切り替え
     */
    public void changeCamera() {
        try {
            // 今のカメラ解放
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }

            // バックカメラ、フロントカメラ切り替え
            if (mCurrentCameraId == mBackCameraId) {
                mCurrentCameraId = mFrontCameraId;
            } else if (mCurrentCameraId == mFrontCameraId) {
                mCurrentCameraId = mBackCameraId;
            } else {
                mCurrentCameraId = mFrontCameraId;
            }

            mCamera = Camera.open(mCurrentCameraId);
            mCamera.setPreviewDisplay(mHolder);

            //縦画面固定
            setPortrait();

            //プレビューサイズ設定
            setPreviewSize();

            //撮影サイズ設定
            setPictureSize();

            mCamera.startPreview();
        }
        catch (Exception e) {
            e.printStackTrace();

            mCamera.release();
            mCamera = null;

            if (mCallbackListener != null) {
                mCallbackListener.unknownException();
            }
        }
    }

    /**
     * フラッシュフラグを受け取る
     *
     * @param flashFlg フラッシュフラグ
     */
    public void setFlashFlg(String flashFlg) {
        mFlashFlg = flashFlg;
    }

    /**
     * 画像保存時の回転角度を算出する
     *
     * @param path 画像パス
     * @return int 角度
     */
    public int getOrientation(String path) throws IOException {
        int degree = 0;

        //exif情報から回転角度を算出
        degree += getOrientationFromExif(path);

        //画面回転を考慮（フロントカメラの場合は左右逆になるみたい）
        if (mCurrentCameraId == mFrontCameraId && (mPreOrientation == ORIENTATION_HORIZONTAL_LEFT ||  mPreOrientation == ORIENTATION_HORIZONTAL_RIGHT)) {
            degree += (ORIENTATION_HORIZONTAL_LEFT == mPreOrientation) ? Math.abs(mPreOrientation) : -(mPreOrientation);
        } else {
            degree += mPreOrientation;
        }

        //フロントカメラかどうかを考慮
        if (mCurrentCameraId == mFrontCameraId) {
            degree += DEGREE_UPSIDE_DOWN;
        }

        //360度を超える可能性を考慮
        while (360 < degree ) {
            degree -= 360;
        }

        LogUtils.i("degree", degree);

        return degree;
    }

    /**
     * exif情報から回転角度を取得する
     */
    public int getOrientationFromExif(String path) throws IOException {
        int degree = DEGREE_DEFAULT;

        ExifInterface exifInterface = new ExifInterface(path);
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            degree = DEGREE_HORIZONTAL_RIGHT;
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            degree = DEGREE_UPSIDE_DOWN;
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            degree = DEGREE_HORIZONTAL_LEFT;
        }
        if (degree != DEGREE_DEFAULT) {
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(DEGREE_DEFAULT));
            exifInterface.saveAttributes();
        }

        LogUtils.i("exif : " + degree);

        return degree;
    }

    /**
     * シャッターが押された際のコールバック
     */
    private Camera.ShutterCallback mShutterListener = new Camera.ShutterCallback() {
        public void onShutter() {
        }
    };

    /**
     * JPEG データ生成完了時のコールバック
     */
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data == null) {
                return;
            }

            try {
                // 画像保存パス
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis() + ".jpg");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                String path = MediaUtils.getPathFromUri(mContext, uri);

                //一旦保存
                FileOutputStream fos;
                fos = new FileOutputStream(MediaUtils.getPathFromUri(mContext, uri), true);
                fos.write(data);
                fos.close();

                //bitmap取得
                Bitmap original = BitmapFactory.decodeByteArray(data, 0, data.length);

                //回転を考慮
                Matrix m = new Matrix();
                m.setRotate(getOrientation(path));

                //回転
                Bitmap roted = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(), m, true);

                //保存し直す
                FileOutputStream out = new FileOutputStream(path, false);
                roted.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

                //ギャラリーに追加
                MediaScannerConnection.scanFile(mContext, new String[]{path}, new String[]{"image/jpeg"}, null);

                original.recycle();
                roted.recycle();
                original = null;
                roted = null;

                //takePicture するとプレビューが停止するので、再度プレビュースタート
                mCamera.startPreview();

                //連打対策解除
                mIsProcessing = false;

                if (mCallbackListener != null) {
                    mCallbackListener.onTakePicture(uri);
                }
            }
            catch (IOException e) {
                e.printStackTrace();

                if (mCallbackListener != null) {
                    mCallbackListener.onTakePictureFailed();
                }
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();

                if (mCallbackListener != null) {
                    mCallbackListener.outOfMemory();
                }
            }
            catch (Exception e) {
                e.printStackTrace();

                if (mCallbackListener != null) {
                    mCallbackListener.unknownException();
                }
            }
        }
    };

    /**
     * コールバックリスナーをセット
     *
     * @param listener コールバックリスナー
     */
    public void setOnTakePictureListener(CallbackListener listener) {
        mCallbackListener = listener;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagneticValues = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerValues = event.values.clone();
                break;
        }

        if (mMagneticValues != null && mAccelerometerValues != null) {
            SensorManager.getRotationMatrix(mInR, mI, mAccelerometerValues, mMagneticValues);
            SensorManager.remapCoordinateSystem(mInR, SensorManager.AXIS_X, SensorManager.AXIS_Z, mOutR);
            SensorManager.getOrientation(mOutR, mOrientationValues);

            int roll = (int) (mOrientationValues[2] * RAD2DEG);

            //初回向き判定
            if (mPreOrientation == -1) {
                if (-45 <= roll && roll <= 45) {
                    mPreOrientation = ORIENTATION_DEFAULT;
                } else if (-45 < roll && roll <= -135) {
                    mPreOrientation = ORIENTATION_HORIZONTAL_LEFT;
                } else if (45 < roll && roll <= 135) {
                    mPreOrientation = ORIENTATION_HORIZONTAL_RIGHT;
                } else {
                    mPreOrientation = ORIENTATION_UPSIDE_DOWN;
                }
            }
            //直前がデフォルトの向きだった場合の向き判定
            else if (mPreOrientation == ORIENTATION_DEFAULT) {
                if (roll < (ORIENTATION_DEFAULT - CHANGE_ORIENTATION_DEGREE)) {
                    mPreOrientation = ORIENTATION_HORIZONTAL_LEFT;
                } else if ((ORIENTATION_DEFAULT + CHANGE_ORIENTATION_DEGREE) < roll) {
                    mPreOrientation = ORIENTATION_HORIZONTAL_RIGHT;
                }
            }
            //直前が左向き横だった場合の向き判定
            else if (mPreOrientation == ORIENTATION_HORIZONTAL_LEFT) {
                if (roll < (ORIENTATION_HORIZONTAL_LEFT - CHANGE_ORIENTATION_DEGREE)) {
                    mPreOrientation = ORIENTATION_UPSIDE_DOWN;
                } else if ((ORIENTATION_HORIZONTAL_LEFT + CHANGE_ORIENTATION_DEGREE) < roll) {
                    mPreOrientation = ORIENTATION_DEFAULT;
                }
            }
            //直前が左向き横だった場合の向き判定
            else if (mPreOrientation == ORIENTATION_HORIZONTAL_RIGHT) {
                if (roll < (ORIENTATION_HORIZONTAL_RIGHT - CHANGE_ORIENTATION_DEGREE)) {
                    mPreOrientation = ORIENTATION_DEFAULT;
                } else if ((ORIENTATION_HORIZONTAL_RIGHT + CHANGE_ORIENTATION_DEGREE) < roll) {
                    mPreOrientation = ORIENTATION_UPSIDE_DOWN;
                }
            }
            //直前がさかさまだった場合の向き判定
            else {
                if (Math.abs(roll) < (ORIENTATION_UPSIDE_DOWN - CHANGE_ORIENTATION_DEGREE)) {
                    mPreOrientation = (roll < 0) ? ORIENTATION_HORIZONTAL_LEFT : ORIENTATION_HORIZONTAL_RIGHT ;
                }
            }
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * コールバックリスナー
     */
    public static interface CallbackListener {
        /**
         * 写真を撮影した
         *
         * @param uri 画像保存URI
         */
        public void onTakePicture(Uri uri);

        /**
         * 写真撮影に失敗した
         */
        public void onTakePictureFailed();

        /**
         * メモリ不足エラーが発生した
         */
        public void outOfMemory();

        /**
         * オートフォーカスに失敗した
         */
        public void autoFocusFailed();

        /**
         * 不明なエラーが発生した
         */
        public void unknownException();
    }
}
