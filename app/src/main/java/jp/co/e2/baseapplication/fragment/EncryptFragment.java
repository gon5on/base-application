package jp.co.e2.baseapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.EncryptHelper;
import jp.co.e2.baseapplication.config.Config;
import jp.co.e2.baseapplication.config.Pref;

/**
 * 暗号化フラグメント
 *
 * 公開鍵認証を使用して、暗号化を行うサンプル
 * 4.3以上であれば、Android KeyStore Providerを使用して鍵の保管を行い、
 * 4.3より低いバージョンであれば、プライベートモードでプリファレンスに鍵を保管する
 */
public class EncryptFragment extends Fragment {
    private static final String BUNDLE_PLANE_TEXT = "bundle_plane_text";
    private static final String BUNDLE_ENCRYPT_TEXT = "bundle_encrypt_text";
    private static final String BUNDLE_DECRYPT_TEXT = "bundle_decrypt_text";

    private View mView;

    /**
     * ファクトリーメソッド
     *
     * @return fragment フラグメント
     */
    public static EncryptFragment newInstance() {
        Bundle args = new Bundle();

        EncryptFragment fragment = new EncryptFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_encrypt, container, false);

        //再生成が走ったら、保管していた値を取り出して画面にセットする
        if (savedInstanceState != null) {
            restoreData(savedInstanceState);
        }
        //初回表示であれば暗号化と復号化を行う
        else {
            try {
                encryptAndDecrypt();
            }
            catch (Exception e) {
                e.printStackTrace();
                AndroidUtils.showToastS(getActivity(), getString(R.string.errorMsgSomethingError));
            }
        }

        return mView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //再生成が走る前に値を保管しておく
        TextView textViewPlane = (TextView) mView.findViewById(R.id.textViewPlane);
        outState.putString(BUNDLE_PLANE_TEXT, textViewPlane.getText().toString());

        TextView textViewEncrypt = (TextView) mView.findViewById(R.id.textViewEncrypt);
        outState.putString(BUNDLE_ENCRYPT_TEXT, textViewEncrypt.getText().toString());

        TextView textViewDecrypt = (TextView) mView.findViewById(R.id.textViewDecrypt);
        outState.putString(BUNDLE_DECRYPT_TEXT, textViewDecrypt.getText().toString());
    }

    /**
     * 保管していた値を取り出す
     *
     * @param savedInstanceState バンドル
     */
    private void restoreData(Bundle savedInstanceState) {
        TextView textViewPlane = (TextView) mView.findViewById(R.id.textViewPlane);
        textViewPlane.setText(savedInstanceState.getString(BUNDLE_PLANE_TEXT));

        TextView textViewEncrypt = (TextView) mView.findViewById(R.id.textViewEncrypt);
        textViewEncrypt.setText(savedInstanceState.getString(BUNDLE_ENCRYPT_TEXT));

        TextView textViewDecrypt = (TextView) mView.findViewById(R.id.textViewDecrypt);
        textViewDecrypt.setText(savedInstanceState.getString(BUNDLE_DECRYPT_TEXT));
    }

    /**
     * 暗号化と復号化
     */
    private void encryptAndDecrypt() throws CertificateException, InvalidKeySpecException, NoSuchAlgorithmException,
            KeyStoreException, InvalidAlgorithmParameterException, NoSuchProviderException, UnrecoverableEntryException,
            IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {

        String planeText = "plane_text!";

        //暗号化
        EncryptHelper encryptHelper = new EncryptHelper(getActivity(), Pref.FILE_NAME);
        encryptHelper.loadKey(Pref.PRIVATE_KEY, Pref.PUBLIC_KEY, Config.KEY_ALIAS);
        String encryptStr = encryptHelper.encrypt(planeText);

        //復号化
        encryptHelper = new EncryptHelper(getActivity(), Pref.FILE_NAME);
        encryptHelper.loadKey(Pref.PRIVATE_KEY, Pref.PUBLIC_KEY, Config.KEY_ALIAS);
        String decryptStr = encryptHelper.decrypt(encryptStr);

        //画面に表示する
        TextView textViewPlane = (TextView) mView.findViewById(R.id.textViewPlane);
        textViewPlane.setText(planeText);

        TextView textViewEncrypt = (TextView) mView.findViewById(R.id.textViewEncrypt);
        textViewEncrypt.setText(encryptStr);

        TextView textViewDecrypt = (TextView) mView.findViewById(R.id.textViewDecrypt);
        textViewDecrypt.setText(decryptStr);
    }
}