package jp.co.e2.baseapplication.config;

/**
 * 設定情報クラス
 * 
 * @access public
 */
public class Config
{
    public final static Integer ENV_PRODUCTION = 1;                             //本番環境
    public final static Integer ENV_STAGING = 2;                                //ステージング環境
    public final static Integer ENV_DEV = 3;                                    //開発環境

    public final static Integer ENV = ENV_DEV;                                  //環境変数

    public final static Integer SAMPLE1 = 1;                                    //サンプル1
    public final static Integer SAMPLE2 = 2;                                    //サンプル2
    public final static Integer SAMPLE3 = 3;                                    //サンプル3

    /**
     * 環境に応じてSAMPLE4を返す
     * 
     * @return Integer
     * @access public
     */
    public static Integer getSample4()
    {
        Integer value = null;

        if (ENV == ENV_PRODUCTION) {
            value = ConfigProduction.SAMPLE4;
        } else if (ENV == ENV_STAGING) {
            value = ConfigStaging.SAMPLE4;
        } else if (ENV == ENV_DEV) {
            value = ConfigDev.SAMPLE4;
        }

        return value;
    }
}
