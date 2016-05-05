package jp.co.e2.baseapplication.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.KeyChain;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.security.auth.x500.X500Principal;

/**
 * 暗号化ヘルパークラス
 *
 * 4.3以上であれば、Android KeyStore Providerを使用して鍵の保管を行い、
 * 4.3より低いバージョンであれば、プライベートモードでプリファレンスに鍵を保管する
 */
public class EncryptHelper {
    public static final String DEFAULT_KEY_ALGORITHM = "RSA";
    public static final String DEFAULT_ENCRYPT_ALGORITHM = "RSA/ECB/PKCS1PADDING";
    public static final String CHAR_ENCODING = "UTF-8";
    public static final int KEY_LIFE_YEAR = 25;

    private String mAlgorithm = DEFAULT_ENCRYPT_ALGORITHM;
    private Context mContext;
    private SharedPreferences mPref;
    private String mKeyAlias;
    private String mPrefPrivateKeyName;
    private String mPrefPublicKeyName;
    private RSAPrivateKey mPrivateKey;
    private RSAPublicKey mPublicKey;
    private KeyStore.PrivateKeyEntry mKeyEntry;     //6.0からか、AndroidKeyStoreから取り出した鍵をRSAPrivateKeyにキャストできなくなったので、
                                                    //キャスト前の型を保持して、使うときに鍵を直接chiperに渡す

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param prefFileName プリファレンスファイル名
     */
    public EncryptHelper(Context context, String prefFileName) {
        mContext = context;
        mPref = mContext.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    /**
     * 鍵を読み込む
     *
     * @param prefPrivateKeyName 秘密鍵のプリファレンスキー名
     * @param prefPublicKeyName 公開鍵のプリファレンスキー名
     * @param keyAlias Android KeyStore Providerに保存するキーのエイリアス
     */
    public void loadKey(String prefPrivateKeyName, String prefPublicKeyName, String keyAlias) throws NoSuchAlgorithmException,
            CertificateException, UnrecoverableEntryException, KeyStoreException, IOException, InvalidKeySpecException, NoSuchProviderException, InvalidAlgorithmParameterException {

        mPrefPrivateKeyName = prefPrivateKeyName;
        mPrefPublicKeyName = prefPublicKeyName;
        mKeyAlias = keyAlias;

        //Android KeyStore Providerが使用できる場合
        if (isCredentialStorage()) {
            if (!loadKeyFromCredentialStorage()) {
                createKeyForCredentialStorage();
            }
        }
        //Android KeyStore Providerが使用できない場合
        else {
            if (!loadKeyFromPref()) {
                createKey();
            }
        }
    }

    /**
     * 暗号化する
     *
     * @param planeStr 平文
     * @return 暗号化した文字列
     */
    public String encrypt(String planeStr) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        //鍵か平文が空の場合は例外を投げる
        if (planeStr == null || (mPublicKey == null && mKeyEntry == null)){
            throw new NullPointerException();
        }

        //暗号化アルゴリズム、動作モード、パディングを指定してCipherオブジェクトの取得
        Cipher cipher = Cipher.getInstance(mAlgorithm);

        //暗号化モード、秘密鍵を設定
        if (mPublicKey != null) {
            cipher.init(Cipher.ENCRYPT_MODE, mPublicKey);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, mKeyEntry.getCertificate().getPublicKey());
        }

        //暗号化
        byte[] encryptData = cipher.doFinal(planeStr.getBytes(CHAR_ENCODING));

        return Base64.encodeToString(encryptData, Base64.NO_WRAP);
    }

    /**
     * 復号化する
     *
     * @param encryptStr 暗号化した文字列
     * @return 平文
     */
    public String decrypt(String encryptStr) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        //鍵か暗号化文字列が空の場合は例外を投げる
        if (encryptStr == null || (mPrivateKey == null && mKeyEntry == null)) {
            throw new NullPointerException();
        }

        //暗号化アルゴリズム、動作モード、パディングを指定してCipherオブジェクトの取得
        Cipher cipher = Cipher.getInstance(mAlgorithm);

        //復号モードで秘密鍵を設定
        if (mPrivateKey != null) {
            cipher.init(Cipher.DECRYPT_MODE, mPrivateKey);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, mKeyEntry.getPrivateKey());
        }

        //復号
        byte[] encryptByte = Base64.decode(encryptStr.getBytes(CHAR_ENCODING), Base64.NO_WRAP);
        byte[] decryptedData = cipher.doFinal(encryptByte);

        return new String(decryptedData);
    }






    /**
     * Android KeyStore Providerが使用できるかどうか
     */
    private boolean isCredentialStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (KeyChain.isBoundKeyAlgorithm(KeyProperties.KEY_ALGORITHM_RSA)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Android KeyStore Providerから鍵を取り出す
     *
     * @return boolean 鍵を取り出せたかどうか
     */
    private boolean loadKeyFromCredentialStorage()
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableEntryException {

        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        mKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(mKeyAlias, null);

        return (mKeyEntry != null);
    }

    /**
     * Android KeyStore Providerに鍵を生成する
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void createKeyForCredentialStorage() throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidAlgorithmParameterException, CertificateException, UnrecoverableEntryException, KeyStoreException, IOException {

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.YEAR, KEY_LIFE_YEAR);
        Date end = cal.getTime();

        //鍵を生成する
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(DEFAULT_KEY_ALGORITHM, "AndroidKeyStore");
        keygen.initialize(new KeyPairGeneratorSpec.Builder(mContext)
                .setAlias(mKeyAlias)
                .setStartDate(now)
                .setEndDate(end)
                .setSerialNumber(BigInteger.valueOf(1))
                .setSubject(new X500Principal("CN=test1"))
                .build());
        keygen.generateKeyPair();

        //鍵を取り出す
        loadKeyFromCredentialStorage();
    }

    /**
     * プリファレンスから鍵を取り出す
     *
     * @return boolean 鍵を取り出せたかどうか
     */
    private boolean loadKeyFromPref() throws NoSuchAlgorithmException, InvalidKeySpecException {

        if (!mPref.contains(mPrefPublicKeyName) || !mPref.contains(mPrefPrivateKeyName)) {
            return false;
        }

        //秘密鍵取り出し
        String privateKeyStr = mPref.getString(mPrefPrivateKeyName, null);
        byte[] privateKeyByte = Base64.decode(privateKeyStr, Base64.NO_WRAP);

        KeyFactory keyFactory = KeyFactory.getInstance(DEFAULT_KEY_ALGORITHM);
        PKCS8EncodedKeySpec keySpecPub = new PKCS8EncodedKeySpec(privateKeyByte);
        mPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpecPub);

        //公開鍵取り出し
        String publicKeyStr = mPref.getString(mPrefPublicKeyName, null);
        byte[] publicKeyByte = Base64.decode(publicKeyStr, Base64.NO_WRAP);

        EncodedKeySpec keySpecPri = new X509EncodedKeySpec(publicKeyByte);
        mPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpecPri);

        return true;
    }

    /**
     * 鍵を生成する
     */
    private void createKey() throws NoSuchAlgorithmException {
        //鍵を生成する
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(DEFAULT_KEY_ALGORITHM);
        keygen.initialize(1024);
        KeyPair keyPair = keygen.generateKeyPair();

        mPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        mPublicKey = (RSAPublicKey) keyPair.getPublic();

        //鍵を保存する
        saveBase64Str(mPrivateKey.getEncoded(), mPrefPrivateKeyName);
        saveBase64Str(mPublicKey.getEncoded(), mPrefPublicKeyName);
    }

    /**
     * Base64したデータをプリファレンスに保存する
     *
     * @param dataByte バイトデータ
     * @param prefName プリファレンスへの保存名
     */
    private void saveBase64Str(byte[] dataByte, String prefName) {
        String keyStr = Base64.encodeToString(dataByte, Base64.NO_WRAP);

        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(prefName, keyStr);
        editor.apply();
    }
}
