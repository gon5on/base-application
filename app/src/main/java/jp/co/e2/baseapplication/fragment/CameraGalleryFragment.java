package jp.co.e2.baseapplication.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;

/**
 * カメラ・ギャラリーフラグメント
 *
 * カメラ、もしくはギャラリーにインテントを投げて、画像を取得するサンプル
 * 回転を考慮してトリミングも行う
 * 4.4以降とそれ以前両方のギャラリーからの選択に対応
 * 6.0以降のパーミッションにも対応
 */
public class CameraGalleryFragment extends Fragment {
    private static final int REQUEST_CODE_CAMERA = 101;
    private static final int REQUEST_CODE_TRIMMING = 102;
    private static final int REQUEST_CODE_GALLERY = 103;
    private static final int REQUEST_CODE_GALLERY_UNDER_KITKAT = 104;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 105;

    private View mView;
    private Uri mPhotoUri;

    /**
     * ファクトリーメソッド
     */
    public static CameraGalleryFragment newInstance() {
        Bundle args = new Bundle();

        CameraGalleryFragment fragment = new CameraGalleryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_camera_gallery, container, false);

        //画像選択
        Button buttonSelect = (Button) mView.findViewById(R.id.buttonSelect);
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //パーミッションチェック
                if (checkPermission()) {
                    showPopupMenu();
                }
            }
        });

        return mView;
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            //カメラの場合、事前にコンテンツプロバイダに登録してしまっているので削除する
            if (requestCode == REQUEST_CODE_CAMERA && mPhotoUri != null) {
                getActivity().getContentResolver().delete(mPhotoUri, null, null);
            }
            return;
        }

        //トリミング戻り
        if (requestCode == REQUEST_CODE_TRIMMING) {
            showPhoto();
        }
        //カメラ・キャラリー戻り
        else {
            goTrimmingFromAlbumOrCamera(requestCode, data);
        }
    }

    /**
     * ${inheritDoc}
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (REQUEST_CODE_WRITE_EXTERNAL_STORAGE == requestCode) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPopupMenu();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("使えないよ");
                alert.setMessage("許可してくれなかったから写真を選べないよ～");
                alert.setPositiveButton("OK", null);
                alert.show();
            }
        }
    }

    /**
     * ポップアップメニューを表示
     */
    private void showPopupMenu() {
        Button buttonSelect = (Button) mView.findViewById(R.id.buttonSelect);

        PopupMenu popup = new PopupMenu(getActivity(), buttonSelect);
        popup.getMenuInflater().inflate(R.menu.menu_select_image, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_gallery) {
                    callGallery();
                } else if (item.getItemId() == R.id.menu_camera) {
                    callCamera();
                }
                return false;
            }
        });
    }

    /**
     * カメラを起動する
     */
    private void callCamera() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String fileName = System.currentTimeMillis() + ".jpg";
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.TITLE, fileName);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeMillis);
            contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, Math.round(currentTimeMillis / 1000));

            mPhotoUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);

            startActivityForResult(intentCamera, REQUEST_CODE_CAMERA);
        }
        catch(Exception e) {
            e.printStackTrace();
            AndroidUtils.showToastS(getActivity(), "エラーが発生しました。");
        }
    }

    /**
     * アルバムを起動する
     */
    private void callGallery() {
        try {
            Intent intent;

            //キットカットより前
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY_UNDER_KITKAT);
            }
            //キットカット以降
            else {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/jpeg");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            AndroidUtils.showToastS(getActivity(), "エラーが発生しました。");
        }
    }

    /**
     * トリミングアプリを起動する
     *
     * @param requestCode リクエストコード
     * @param data インテント
     */
    private void goTrimmingFromAlbumOrCamera(int requestCode, Intent data) {
        try {
            mPhotoUri = (data != null && data.getData() != null) ? data.getData() : mPhotoUri;

            //URIが取れなかった
            if (mPhotoUri == null) {
                throw new NullPointerException();
            }

            //カメラの場合はギャラリーをスキャン
            if (requestCode == REQUEST_CODE_CAMERA) {
                String path[] = new String[]{ mPhotoUri.getPath() };
                String type[] = new String[]{ "image/jpeg" };
                MediaScannerConnection.scanFile(getActivity(), path, type, null);
            }

            //URIをパスに変換
            String orgPath = getPathFromUri(requestCode);

            //パスが取れなかった
            if (orgPath == null) {
                throw new NullPointerException();
            }

            //回転を考慮して画像を保存し直す
            String tmpPath = getSaveTmpPath();
            saveRotatedResizedImage(orgPath, tmpPath);

            //トリミングアプリ呼び出し
            String savePath = getSavePath();
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setData(path2contentUri(tmpPath));
            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 500);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(savePath)));
            startActivityForResult(intent, REQUEST_CODE_TRIMMING);
        }
        catch (Exception e) {
            e.printStackTrace();
            AndroidUtils.showToastS(getActivity(), "エラーが発生しました。");
        }
    }

    /**
     * URIからパスを取得する
     *
     * @param requestCode リクエストコード
     * @return path パス
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPathFromUri(int requestCode) {
        String path = null;

        //カメラ or キットカットより前
        if (requestCode == REQUEST_CODE_CAMERA || requestCode == REQUEST_CODE_GALLERY_UNDER_KITKAT) {
            String[] strColumns = { MediaStore.Images.Media.DATA };
            Cursor crsCursor = getActivity().getContentResolver().query(mPhotoUri, strColumns, null, null, null);
            if(crsCursor != null && crsCursor.moveToFirst()) {
                path = crsCursor.getString(0);
            }
            if (crsCursor != null) {
                crsCursor.close();
            }
        }
        //キットカット以降
        else {
            String strDocId = DocumentsContract.getDocumentId(mPhotoUri);
            String[] strSplitDocId = strDocId.split(":");
            String strId = strSplitDocId[strSplitDocId.length - 1];

            Cursor crsCursor = getActivity().getContentResolver().query(
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

        return path;
    }

    /**
     * 回転を考慮して、リサイズした画像を保存し直す
     *
     * @param orgPath 元画像パス
     * @param savePath 保存画像パス
     * @throws IOException
     */
    private void saveRotatedResizedImage(String orgPath, String savePath) throws IOException {
        File file = new File(orgPath);

        //ファイルが存在しない
        if (!file.exists()) {
            throw new NullPointerException();
        }

        //回転と読み込みサイズを計算
        Matrix matrix = new Matrix();
        matrix = getResizedMatrix(file, matrix);
        matrix = getRotatedMatrix(file, matrix);

        // 元画像の取得
        Bitmap originalPicture = BitmapFactory.decodeFile(orgPath);
        int height = originalPicture.getHeight();
        int width = originalPicture.getWidth();

        // マトリクスをつけることで縮小、向きを反映した画像を生成
        Bitmap resizedPicture = Bitmap.createBitmap(originalPicture, 0, 0, width, height, matrix, true);

        // 一時ファイルの保存
        File destination = new File(savePath);
        FileOutputStream outputStream = new FileOutputStream(destination);
        resizedPicture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();

        //ギャラリーのスキャン
        String path[] = new String[]{ savePath };
        String type[] = new String[]{ "image/jpeg" };
        MediaScannerConnection.scanFile(getActivity(), path, type, null);

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
     * パスをコンテンツURIに変換
     *
     * @param path パス
     * @return コンテンツURI
     * @throws NullPointerException
     */
    private Uri path2contentUri(String path) throws NullPointerException {
        Uri uri;

        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] project = { BaseColumns._ID };
        String sel = MediaStore.Images.ImageColumns.DATA + " LIKE ?";
        String[] selArgs = new String[] { path };
        ContentResolver cr = getActivity().getContentResolver();

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

    /**
     * 外部ストレージにアクセスするパーミッションがあるか確認して取得する
     *
     * @return boolean
     */
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission() {
        //パーミッションがない場合はリクエスト
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA)) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("おねがい");
                alert.setMessage("次に出てくるダイアログで許可を押してね～");
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                    }
                });
                alert.show();
            } else {
                requestPermissions(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }

            return false;
        }

        return true;
    }

    /**
     * 画像の保存パスを取得
     */
    public static String getSavePath() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/";
        path += "tmp.jpg";

        return path;
    }

    /**
     * 画像の一時保存パスを取得
     */
    public static String getSaveTmpPath() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/";
        path += "image.jpg";

        return path;
    }

    /**
     * 選択した画像を表示する
     */
    private void showPhoto() {
        try {
            FileInputStream fis = new FileInputStream(new File(getSavePath()));
            BufferedInputStream bis = new BufferedInputStream(fis);
            Bitmap bitmap = BitmapFactory.decodeStream(bis);

            ImageView imageView = (ImageView) mView.findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }
        catch (IOException e){
            e.printStackTrace();
            AndroidUtils.showToastS(getActivity(), "エラーが発生しました。");
        }
    }
}