package jp.co.e2.baseapplication.config;

/**
 * 設定情報クラス
 */
public class Config {
    public static final String KEY_ALIAS = "key_alias";                                     //秘密鍵認証のキーエイリアス

    public static final int IMG_TMP_SIZE = 1000;                                            //画像一時保存サイズ
    public static final int IMG_SAVE_SIZE = 500;                                            //画像保存サイズ
    public static final String IMG_DIR = "img";                                             //画像ディレクトリ名
    public static final String IMG_SAVE_FILE_NAME = "img.jpg";                              //画像一時保存ファイル名
    public static final String IMG_TMP_FILE_NAME = "tmp.jpg";                               //画像保存ファイル名

    public static final String URL_SUCCESS = "http://5050.tmrnk.com/dev/success.php";       //成功が返却されるURL
    public static final String URL_ERROR = "http://5050.tmrnk.com/dev/error.php";           //エラーステータスが返却されるURL
    public static final String URL_DISCONNECT = "http://5050.tmrnk.com/dev/notFound.php";   //404が返却される存在しないURL
}
