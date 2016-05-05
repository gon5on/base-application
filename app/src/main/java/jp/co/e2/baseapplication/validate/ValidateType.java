package jp.co.e2.baseapplication.validate;

/**
 * 形式バリデーションクラス
 */
public class ValidateType {
    public static final String MATCH_NUMBER = "^[0-9]+$";
    public static final String MATCH_ALPHABET = "^[a-zA-Z]+$";
    public static final String MATCH_ALPHANUMERIC = "^[a-zA-Z0-9]+$";
    public static final String MATCH_HIRAGANA = "^[ぁ-ゞー～ 　]+$";
    public static final String MATCH_KATAKANA = "^[ァ-ヶー～ 　]+$";

    public static final String MATCH_EMAIL = "([a-zA-Z0-9][a-zA-Z0-9_.+\\-]*)@(([a-zA-Z0-9][a-zA-Z0-9_\\-]*\\.)+[a-zA-Z]{2,6})";
    public static final String MATCH_URL = "^(https?|ftp)(:\\/\\/[-_.!~*\\'()a-zA-Z0-9;\\/?:\\@&=+\\$,%#]+)$";

    /**
     * すべて半角数字かどうか
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void isHalfWeightNumeric(ValidateHelper validate, String name, String value, String errorMsg) {
        match(validate, value, name, errorMsg, MATCH_NUMBER);
    }

    /**
     * すべて半角英字かどうか
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void isHalfWeightAlphabet(ValidateHelper validate, String name, String value, String errorMsg) {
        match(validate, value, name, errorMsg, MATCH_ALPHABET);
    }

    /**
     * すべて半角英数字かどうか
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void isHalfWeightAlphanumeric(ValidateHelper validate, String name, String value, String errorMsg) {
        match(validate, value, name, errorMsg, MATCH_ALPHANUMERIC);
    }

    /**
     * すべてひらがなかどうか
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void isHiragana(ValidateHelper validate, String name, String value, String errorMsg) {
        match(validate, value, name, errorMsg, MATCH_HIRAGANA);
    }

    /**
     * すべてカタカナかどうか
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void isKatakana(ValidateHelper validate, String name, String value, String errorMsg) {
        match(validate, value, name, errorMsg, MATCH_KATAKANA);
    }

    /**
     * メールの形式かどうか
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void isMail(ValidateHelper validate, String name, String value, String errorMsg) {
        match(validate, value, name, errorMsg, MATCH_EMAIL);
    }

    /**
     * メールの形式かどうか
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void isUrl(ValidateHelper validate, String name, String value, String errorMsg) {
        match(validate, value, name, errorMsg, MATCH_URL);
    }

    /**
     * すべて半角文字かどうか
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void isHalfWeightChar(ValidateHelper validate, String name, String value, String errorMsg) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null || value.length() == 0) {
            return;
        }

        char[] chars = value.toCharArray();

        for (char c : chars) {
            //半角
            if ((c <= '\u007e') ||                      // 英数字
                    (c == '\u00a5') ||                  // \記号
                    (c == '\u203e') ||                  // ~記号
                    (c >= '\uff61' && c <= '\uff9f')    // 半角カナ
                    ) {
                //何もしない
            }
            //全角
            else {
                validate.error(name, errorMsg);
            }
        }
    }

    /**
     * マッチするかどうか
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     * @param pattern 正規表現パターン
     */
    private static void match(ValidateHelper validate, String name, String value, String errorMsg, String pattern) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null || value.length() == 0) {
            return;
        }

        if (!value.matches(pattern)) {
            validate.error(name, errorMsg);
        }
    }
}
