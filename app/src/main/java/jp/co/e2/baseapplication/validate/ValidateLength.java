package jp.co.e2.baseapplication.validate;

/**
 * 文字長バリデーションクラス
 */
public class ValidateLength {
    /**
     * 文字長MAXチェック（String）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max MAX文字数
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, String value, Integer max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 文字長MAXチェック（Int）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max MAX文字数
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, Integer value, Integer max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 文字長MINチェック（String）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min MIN文字数
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, String value, Integer min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 文字長MINチェック（Int）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min MIN文字数
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Integer value, Integer min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 文字長MAXチェック
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max MAX文字数
     * @param errorMsg エラーメッセージ
     */
    private static void doMaxCheck(ValidateHelper validate, String name, Object value, Integer max, String errorMsg) {
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

        if (max < valueStr.length()) {
            validate.error(name, errorMsg);
        }
    }

    /**
     * 文字長MINチェック
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min MIN文字数
     * @param errorMsg エラーメッセージ
     */
    private static void doMinCheck(ValidateHelper validate, String name, Object value, Integer min, String errorMsg) {
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

        if (valueStr.length() < min) {
            validate.error(name, errorMsg);
        }
    }
}
