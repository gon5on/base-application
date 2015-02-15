package jp.co.e2.baseapplication.entity;

import java.io.Serializable;

/**
 * サンプルエンティティクラス
 */
public class SampleEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer mId;                                        //ID
    private String mSample1;                                    //サンプル1
    private String mSample2;                                    //サンプル2
    private String mSample3;                                    //サンプル3
    private String mCreated;                                    //作成日時
    private String mModified;                                   //更新日時

    /**
     * IDをセット
     *
     * @param value 値
     */
    public void setId(Integer value) {
        mId = value;
    }

    /**
     * IDを返す
     *
     * @return Integer mId
     */
    public Integer getId() {
        return mId;
    }

    /**
     * サンプル1をセット
     *
     * @param value 値
     */
    public void setSample1(String value) {
        mSample1 = value;
    }

    /**
     * サンプル1を返す
     *
     * @return String mSample1
     */
    public String getSample1() {
        return mSample1;
    }

    /**
     * サンプル2をセット
     *
     * @param value 値
     */
    public void setSample2(String value) {
        mSample2 = value;
    }

    /**
     * サンプル1を返す
     *
     * @return String mSample2
     */
    public String getSample2() {
        return mSample2;
    }

    /**
     * サンプル1をセット
     *
     * @param value 値
     */
    public void setSample3(String value) {
        mSample3 = value;
    }

    /**
     * サンプル3を返す
     *
     * @return String mSample3
     */
    public String getSample3() {
        return mSample3;
    }

    /**
     * 作成日時をセット
     *
     * @param value 値
     */
    public void setCreated(String value) {
        mCreated = value;
    }

    /**
     * 作成日時を返す
     *
     * @return String mCreated
     */
    public String getCreated() {
        return mCreated;
    }

    /**
     * 更新日時をセット
     *
     * @param value 値
     */
    public void setModified(String value) {
        mModified = value;
    }

    /**
     * 更新日時を返す
     *
     * @return String mModified
     */
    public String Modified() {
        return mModified;
    }
}
