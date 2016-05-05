package jp.co.e2.baseapplication.validate;

/**
 * 必須バリデーションクラス
 */
public class ValidateRequire {
    /**
     * 必須チェック（String）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, String name, String value, String errorMsg) {
        doCheck(validate, name, value, errorMsg);
    }

    /**
     * 必須チェック（Integer）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, Integer value, String name, String errorMsg) {
        doCheck(validate, name, value, errorMsg);
    }

    /**
     * 必須チェック（セレクトボックス用）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, String name, boolean[] value, String errorMsg) {
        if (!validate.getResult(name)) {
            return;
        }

        Boolean flg = false;

        for (boolean tmp : value) {
            if (tmp) {
                flg = true;
                break;
            }
        }

        if (!flg) {
            validate.error(name, errorMsg);
        }
    }

    /**
     * 必須チェック
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
            validate.error(name, errorMsg);
        }

        String valueStr = String.valueOf(value);

        if (valueStr.length() == 0) {
            validate.error(name, errorMsg);
        }
    }
}
