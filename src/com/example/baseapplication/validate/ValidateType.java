package com.example.baseapplication.validate;

/**
 * 形式計バリデーションクラス
 * 
 * value … バリデート対象の値
 * name … 値の名前（誕生日、性別とか）
 * msgFull … デフォルトではないエラーメッセージを使用したい場合に指定
 */
public class ValidateType
{
    public static final String ERROR_MSG_HALF_WEIGHT_NUMERIC = "%sは半角数字で入力してください。";
    public static final String ERROR_MSG_HALF_WEIGHT_ALPHABET = "%sは半角英字で入力してください。";
    public static final String ERROR_MSG_HALF_WEIGHT_ALPHANUMERIC = "%sは半角英数字で入力してください。";
    public static final String ERROR_MSG_HIRAGANA = "%sは全角ひらがなで入力してください。";
    public static final String ERROR_MSG_KATAKANA = "%sは全角カタカナで入力してください。";

    public static final String ERROR_MSG_EMAIL = "%sは正しいメールアドレスの形式ではありません。";
    public static final String ERROR_MSG_URL = "%sは正しいURLの形式ではありません。";

    public static final String MATCH_NUMBER = "^[0-9]+$";
    public static final String MATCH_ALPHABET = "^[a-zA-Z]+$";
    public static final String MATCH_ASCII = "^[\\u0020-\\u007E]+$";
    public static final String MATCH_HIRAGANA = "^[\\u3040-\\u309F]+$";
    public static final String MATCH_KATAKANA = "^[\\u30A0-\\u30FF]+$";

    public static final String MATCH_EMAIL = "([a-zA-Z0-9][a-zA-Z0-9_.+\\-]*)@(([a-zA-Z0-9][a-zA-Z0-9_\\-]+\\.)+[a-zA-Z]{2,6})";
    public static final String MATCH_URL = "^(https?|ftp)(:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+)$";

    private Validate mValidate;              //バリデーションクラス

    /**
     * コンストラクタ
     * 
     * @param Validate validate バリデーションクラス
     */
    public ValidateType(Validate validate)
    {
        mValidate = validate;
    }

    /**
     * すべて半角数字かどうか
     * 
     * @param String value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void isHalfWeightNumeric(String value, String name, String msgFull)
    {
        if (mValidate.getValueResult() == false) {
            return;
        }
        if (value == null || value.length() == 0) {
            return;
        }

        if (value.matches(MATCH_NUMBER) == false) {
            if (msgFull != null) {
                mValidate.error(msgFull);
            } else {
                mValidate.error(String.format(ERROR_MSG_HALF_WEIGHT_NUMERIC, name));
            }
        }
    }

    /**
     * すべて半角英字かどうか
     * 
     * @param String value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void isHalfWeightAlphabet(String value, String name, String msgFull)
    {
        if (mValidate.getValueResult() == false) {
            return;
        }
        if (value == null || value.length() == 0) {
            return;
        }

        if (value.matches(MATCH_ALPHABET) == false) {
            if (msgFull != null) {
                mValidate.error(msgFull);
            } else {
                mValidate.error(String.format(ERROR_MSG_HALF_WEIGHT_ALPHABET, name));
            }
        }
    }

    /**
     * すべて半角英数字かどうか
     * 
     * @param String value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void isHalfWeightAlphanumeric(String value, String name, String msgFull)
    {
        if (mValidate.getValueResult() == false) {
            return;
        }
        if (value == null || value.length() == 0) {
            return;
        }

        if (value.matches(MATCH_ASCII) == false) {
            if (msgFull != null) {
                mValidate.error(msgFull);
            } else {
                mValidate.error(String.format(ERROR_MSG_HALF_WEIGHT_ALPHANUMERIC, name));
            }
        }
    }

    /**
     * すべてひらがなかどうか
     * 
     * @param String value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void isHiragana(String value, String name, String msgFull)
    {
        if (mValidate.getValueResult() == false) {
            return;
        }
        if (value == null || value.length() == 0) {
            return;
        }

        if (value.matches(MATCH_HIRAGANA) == false) {
            if (msgFull != null) {
                mValidate.error(msgFull);
            } else {
                mValidate.error(String.format(ERROR_MSG_HIRAGANA, name));
            }
        }
    }

    /**
     * すべてカタカナかどうか
     * 
     * @param String value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void isKatakana(String value, String name, String msgFull)
    {
        if (mValidate.getValueResult() == false) {
            return;
        }
        if (value == null || value.length() == 0) {
            return;
        }

        if (value.matches(MATCH_KATAKANA) == false) {
            if (msgFull != null) {
                mValidate.error(msgFull);
            } else {
                mValidate.error(String.format(ERROR_MSG_KATAKANA, name));
            }
        }
    }

    /**
     * メールの形式かどうか
     * 
     * @param String value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void isEmail(String value, String name, String msgFull)
    {
        if (mValidate.getValueResult() == false) {
            return;
        }
        if (value == null || value.length() == 0) {
            return;
        }

        if (value.matches(MATCH_EMAIL) == false) {
            if (msgFull != null) {
                mValidate.error(msgFull);
            } else {
                mValidate.error(String.format(ERROR_MSG_EMAIL, name));
            }
        }
    }

    /**
     * URLの形式かどうか
     * 
     * @param String value 値
     * @param String name 変数名
     * @param String msgFull エラーメッセージ全文
     * @return void
     * @access public
     */
    public void isUrl(String value, String name, String msgFull)
    {
        if (mValidate.getValueResult() == false) {
            return;
        }
        if (value == null || value.length() == 0) {
            return;
        }

        if (value.matches(MATCH_URL) == false) {
            if (msgFull != null) {
                mValidate.error(msgFull);
            } else {
                mValidate.error(String.format(ERROR_MSG_URL, name));
            }
        }
    }
}
