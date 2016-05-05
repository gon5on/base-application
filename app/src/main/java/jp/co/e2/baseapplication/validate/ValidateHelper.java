package jp.co.e2.baseapplication.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * バリデートクラス
 *
 * 同じ値に複数のバリデートをかける場合、エラーがあったらその後のバリデートは実行されません。
 * 各バリデートへの引数は、各バリデートクラス参照。
 *
 * Validate v = new Validate();
 * ValidateRequire.check(v, "name", value, "名前は必須入力項目です。");
 * ValidateLength.max(v, "name", value, 30, "名前は30文字以内で入力してください。");
 *
 * if(!v.getResult()){
 *     .....
 * }
 */
public class ValidateHelper {
    private boolean mResult = true;                         //バリデート結果
    private LinkedHashMap<String, String> mErrorMsg;        //エラーメッセージ

    /**
     * コンストラクタ
     */
    public ValidateHelper() {
        mResult = true;
        mErrorMsg = new LinkedHashMap<>();
    }

    /**
     * バリデート結果がエラーのため、エラーメッセージを追加する
     *
     * @param name 変数名
     * @param msg エラーメッセージ
     */
    public void error(String name, String msg) {
        mResult = false;

        mErrorMsg.put(name, msg);
    }

    /**
     * 渡された変数名のデータに関するエラーが既に存在しているか
     *
     * @param name 変数名
     * @return boolean エラーの有無
     */
    public Boolean getResult(String name) {
        return !mErrorMsg.containsKey(name);
    }

    /**
     * バリデート結果を返す
     *
     * @return result バリデート結果
     */
    public Boolean getResult() {
        return mResult;
    }

    /**
     * エラ―文言を返す（ハッシュマップ形式）
     *
     * @return mErrorMsg
     */
    public HashMap<String, String> getErrorMsgMap() {
        return mErrorMsg;
    }

    /**
     * エラ―文言を返す（リスト形式）
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getErrorMsgList() {
        return new ArrayList<>(mErrorMsg.values());
    }
}
