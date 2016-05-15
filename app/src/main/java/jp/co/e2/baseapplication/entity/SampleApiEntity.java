package jp.co.e2.baseapplication.entity;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * サンプルAPIのレスポンスを格納するエンティティクラス
 */
public class SampleApiEntity implements Serializable {
    @SerializedName("code")
    private Integer mCode;                              //コード
    @SerializedName("data")
    private ArrayList<SampleEntity> mData;              //データ

    /**
     * コード返す
     *
     * @return コード
     */
    public int getCode() {
        if (mCode == null) {
            mCode = HttpURLConnection.HTTP_NOT_FOUND;
        }

        return mCode;
    }

    /**
     * コードをセットする
     *
     * @param code コード
     */
    public void setCode(int code) {
        this.mCode = code;
    }

    /**
     * データを返す
     *
     * @return データ
     */
    public ArrayList<SampleEntity> getData() {
        return mData;
    }

    /**
     * データをセットする
     *
     * @param data データ
     */
    public void setData(ArrayList<SampleEntity> data) {
        this.mData = data;
    }
}
