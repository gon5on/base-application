package jp.co.e2.baseapplication.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.IOException;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.ImgHelper;

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
     *
     * @return fragment フラグメント
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
            boolean newMethod = (requestCode == REQUEST_CODE_GALLERY);
            String orgPath = AndroidUtils.getPathFromUri(getActivity(), mPhotoUri, newMethod);

            //パスが取れなかった
            if (orgPath == null) {
                throw new NullPointerException();
            }

            //回転を考慮して画像を保存し直す
            String tmpPath = getSaveTmpPath();
            ImgHelper imgHelper = new ImgHelper(orgPath);
            imgHelper.getRotatedResizedImage(500, 500);
            imgHelper.saveImg(getActivity(), tmpPath);

            //トリミングアプリ呼び出し
            String savePath = getSavePath();
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setData(AndroidUtils.path2contentUri(getActivity(), tmpPath));
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