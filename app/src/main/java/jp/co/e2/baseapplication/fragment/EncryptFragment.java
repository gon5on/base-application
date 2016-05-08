package jp.co.e2.baseapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.common.AndroidUtils;
import jp.co.e2.baseapplication.common.EncryptHelper;
import jp.co.e2.baseapplication.config.Config;

/**
 * 暗号化フラグメント
 *
 * 公開鍵認証を使用して、暗号化を行うサンプル
 * 4.3以上であれば、Android KeyStore Providerを使用して鍵の保管を行い、
 * 4.3より低いバージョンであれば、プライベートモードでプリファレンスに鍵を保管する
 */
public class EncryptFragment extends Fragment {
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

        try {
            String planeText = "plane_text!";

            //暗号化
            EncryptHelper encryptHelper = new EncryptHelper(getActivity(), Config.PREF_FILE_NAME);
            encryptHelper.loadKey(Config.PREF_PRIVATE_KEY, Config.PREF_PUBLIC_KEY, Config.KEY_ALIAS);
            String encryptStr = encryptHelper.encrypt(planeText);

            //復号化
            encryptHelper = new EncryptHelper(getActivity(), Config.PREF_FILE_NAME);
            encryptHelper.loadKey(Config.PREF_PRIVATE_KEY, Config.PREF_PUBLIC_KEY, Config.KEY_ALIAS);
            String decryptStr = encryptHelper.decrypt(encryptStr);

            //画面に表示する
            TextView textViewPlane = (TextView) mView.findViewById(R.id.textViewPlane);
            textViewPlane.setText(planeText);

            TextView textViewEncrypt = (TextView) mView.findViewById(R.id.textViewEncrypt);
            textViewEncrypt.setText(encryptStr);

            TextView textViewDecrypt = (TextView) mView.findViewById(R.id.textViewDecrypt);
            textViewDecrypt.setText(decryptStr);
        }
        catch (Exception e) {
            e.printStackTrace();
            AndroidUtils.showToastS(getActivity(), getString(R.string.errorMsgSomethingError));
        }

        return mView;
    }
}