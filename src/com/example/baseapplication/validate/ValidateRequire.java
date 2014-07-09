package com.example.baseapplication.validate;

/**
 * 必須チェッククラス
 * 
 * validate … バリデートクラス
 * value … バリデート対象の値
 * name … 値の名前（誕生日、性別とか）
 * msgFull … デフォルトではないエラーメッセージを使用したい場合に指定
 */
public class ValidateRequire
{
    public static final String ERROR_MSG_REQUIRE = "%sを入力してください。";
    public static final String ERROR_MSG_REQUIRE_SELECT = "%sを選択してください。";

    /**
     * String型必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param String value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void check(Validate validate, String value, String name, String msgFull)
    {
        if (validate.getResult(value) == false) {
            return;
        }
        if (value == null || value.length() == 0) {
            setErrorMsg(validate, name, msgFull);
        }
    }

    /**
     * String型必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param String value 値
     * @param String name 変数名
     * @return void
     * @access public
     */
    public void check(Validate validate, String value, String name)
    {
        check(validate, value, name, null);
    }

    /**
     * Int型必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param Int value 値
     * @param String name 変数名
     * @param String msg_full エラーメッセージ全文
     * @return void
     * @access public
     */
    public void check(Validate validate, Integer value, String name, String msgFull)
    {
        if (validate.getResult(name) == false) {
            return;
        }
        if (value == null) {
            setErrorMsg(validate, name, msgFull);
        }
    }

    /**
     * Int型必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param Int value 値
     * @param String name 変数名
     * @return void
     * @access public
     */
    public void check(Validate validate, Integer value, String name)
    {
        check(validate, value, name, null);
    }

    /**
     * Double型必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param Double value 値
     * @param String name 変数名
     * @param String msg_full エラーメッセージ全文
     * @return void
     * @access public
     */
    public void check(Validate validate, Double value, String name, String msgFull)
    {
        if (validate.getResult(name) == false) {
            return;
        }
        if (value == null) {
            setErrorMsg(validate, name, msgFull);
        }
    }

    /**
     * Double型必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param Double value 値
     * @param String name 変数名
     * @return void
     * @access public
     */
    public void check(Validate validate, Double value, String name)
    {
        check(validate, value, name, null);
    }

    /**
     * Float型必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param Float value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void check(Validate validate, Float value, String name, String msgFull)
    {
        if (validate.getResult(name) == false) {
            return;
        }
        if (value == null) {
            setErrorMsg(validate, name, msgFull);
        }
    }

    /**
     * Float型必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param Float value 値
     * @param String name 変数名
     * @return void
     * @access public
     */
    public void check(Validate validate, Float value, String name)
    {
        check(validate, value, name, null);
    }

    /**
     * セレクトボックス必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param boolean[] value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void check(Validate validate, boolean[] value, String name, String msgFull)
    {
        if (validate.getResult(name) == false) {
            return;
        }

        selectboxCheck(validate, value, name, msgFull);
    }

    /**
     * セレクトボックス必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param boolean[] value 値
     * @param String name 変数名
     * @return void
     * @access public
     */
    public void check(Validate validate, boolean[] value, String name)
    {
        selectboxCheck(validate, value, name, null);
    }

    /**
     * セレクトボックス必須チェック
     * 
     * @param Validate validate バリデートクラス
     * @param boolean[] value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access private
     */
    private void selectboxCheck(Validate validate, boolean[] value, String name, String msgFull)
    {
        Boolean flg = false;

        for (int i = 0; i < value.length; i++) {
            if (value[i] == true) {
                flg = true;
                break;
            }
        }

        if (flg == false) {
            if (msgFull != null) {
                validate.error(name, msgFull);
            } else {
                validate.error(name, String.format(ERROR_MSG_REQUIRE, name));
            }
        }
    }

    /**
     * エラーメッセージセット
     * 
     * @param Validate validate バリデートクラス
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access private
     */
    private void setErrorMsg(Validate validate, String name, String msgFull)
    {
        if (msgFull != null) {
            validate.error(name, msgFull);
        } else {
            validate.error(name, String.format(ERROR_MSG_REQUIRE, name));
        }
    }
}
