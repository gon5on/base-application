package jp.co.e2.baseapplication.validate;

/**
 * 少数バリデーションクラス
 */
public class ValidateDecimal {
    public static final String MATCH_DOUBLE = "^([0-9]\\d*|0)(\\.\\d+)?$";
    public static final String MATCH_DOUBLE_POINT = "^([0-9]\\d*|0)(\\.\\d{0,POINT})?$";

    /**
     * 少数形式チェック（String）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, String name, String value, String errorMsg) {
        doCheck(validate, name, value, errorMsg, MATCH_DOUBLE);
    }

    /**
     * 少数形式チェック（Int）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, String name, Integer value, String errorMsg) {
        doCheck(validate, name, value, errorMsg, MATCH_DOUBLE);
    }

    /**
     * 少数形式チェック（Float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, String name, Float value, String errorMsg) {
        doCheck(validate, name, value, errorMsg, MATCH_DOUBLE);
    }

    /**
     * 少数形式チェック（Double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, String name, Double value, String errorMsg) {
        doCheck(validate, name, value, errorMsg, MATCH_DOUBLE);
    }

    /**
     * 少数形式と桁数チェック（String）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param point 小数点以下MAX桁数
     * @param errorMsg エラーメッセージ
     */
    public static void checkPoint(ValidateHelper validate, String name, String value, int point, String errorMsg) {
        //正規表現の小数点部分を置換しておく
        String pattern = MATCH_DOUBLE_POINT.replaceAll("POINT", String.valueOf(point));

        doCheck(validate, name, value, errorMsg, pattern);
    }

    /**
     * 少数形式と桁数チェック（Int）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param point 小数点以下MAX桁数
     * @param errorMsg エラーメッセージ
     */
    public static void checkPoint(ValidateHelper validate, String name, Integer value, int point, String errorMsg) {
        //正規表現の小数点部分を置換しておく
        String pattern = MATCH_DOUBLE_POINT.replaceAll("POINT", String.valueOf(point));

        doCheck(validate, name, value, errorMsg, pattern);
    }

    /**
     * 少数形式と桁数チェック（Float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param point 小数点以下MAX桁数
     * @param errorMsg エラーメッセージ
     */
    public static void checkPoint(ValidateHelper validate, String name, Float value, int point, String errorMsg) {
        //正規表現の小数点部分を置換しておく
        String pattern = MATCH_DOUBLE_POINT.replaceAll("POINT", String.valueOf(point));

        doCheck(validate, name, value, errorMsg, pattern);
    }

    /**
     * 少数形式と桁数チェック（Double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param point 小数点以下MAX桁数
     * @param errorMsg エラーメッセージ
     */
    public static void checkPoint(ValidateHelper validate, String name, Double value, int point, String errorMsg) {
        //正規表現の小数点部分を置換しておく
        String pattern = MATCH_DOUBLE_POINT.replaceAll("POINT", String.valueOf(point));

        doCheck(validate, name, value, errorMsg, pattern);
    }

    /**
     * 少数形式チェック
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     * @param pattern エラーメッセージ
     */
    private static void doCheck(ValidateHelper validate, String name, Object value, String errorMsg, String pattern) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null) {
            return;
        }

        String valueStr = String.valueOf(value);

        if (valueStr.length() == 0) {
            return;
        }

        if (!valueStr.matches(pattern)) {
            validate.error(name, errorMsg);
        }
    }
}
