package jp.co.e2.baseapplication.validate;

/**
 * 整数バリデーションクラス
 *
 * 整数なのでプラスマイナスが付いていてもOK
 */
public class ValidateInt {
    public static final String MATCH_INT = "^[+-]?[0-9]+$";

    /**
     * 整数チェック（String）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, String name, String value, String errorMsg) {
        doCheck(validate, value, name, errorMsg);
    }

    /**
     * 整数チェック（Int）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, String name, Integer value, String errorMsg) {
        doCheck(validate, name, value, errorMsg);
    }

    /**
     * 整数チェック
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    private static void doCheck(ValidateHelper validate, String name, Object value, String errorMsg) {
        if (!validate.getResult()) {
            return;
        }

        if (value == null) {
            return;
        }

        String valueStr = String.valueOf(value);

        if (valueStr.length() == 0) {
            return;
        }

        if (!valueStr.matches(MATCH_INT)) {
            validate.error(name, errorMsg);
        }
    }
}
