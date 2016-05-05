package jp.co.e2.baseapplication.validate;

/**
 * 最大・最小系バリデーションクラス
 *
 * 小数に変換して大小を確認するので、
 * 事前に整数か小数であるかのバリデーションを掛けておくこと！
 */
public class ValidateSize {
    /**
     * 最大チェック（値→String、MAX→Integer）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, String value, Integer max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→Integer、MAX→Integer）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, Integer value, Integer max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→Float、MAX→Integer）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, Float value, Integer max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→Double、MAX→Integer）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, Double value, Integer max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→String、MAX→float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, String value, float max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→Integer、MAX→float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, Integer value, float max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→Float、MAX→float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, Float value, float max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→Double、MAX→float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, Double value, float max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→String、MAX→double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, String value, double max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→Integer、MAX→double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ全文
     */
    public static void maxCheck(ValidateHelper validate, String name, Integer value, double max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→Float、MAX→double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, Float value, double max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }

    /**
     * 最大チェック（値→Double、MAX→double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    public static void maxCheck(ValidateHelper validate, String name, Double value, double max, String errorMsg) {
        doMaxCheck(validate, name, value, max, errorMsg);
    }




    /**
     * 最小チェック（値→String、MAX→Integer）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, String value, Integer min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→Integer、MAX→Integer）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Integer value, Integer min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→Float、MAX→Integer）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Float value, Integer min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→Double、MAX→Integer）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Double value, Integer min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→String、MAX→float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, String value, float min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→Integer、MAX→float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Integer value, float min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→Float、MAX→float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Float value, float min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→Double、MAX→float）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Double value, float min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→String、MAX→double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, String value, double min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→Integer、MAX→double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Integer value, double min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→Float、MAX→double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Float value, double min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }

    /**
     * 最小チェック（値→Double、MAX→double）
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    public static void minCheck(ValidateHelper validate, String name, Double value, double min, String errorMsg) {
        doMinCheck(validate, name, value, min, errorMsg);
    }




    /**
     * 最大チェック
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param max 最大値
     * @param errorMsg エラーメッセージ
     */
    private static void doMaxCheck(ValidateHelper validate, String name, Object value, Object max, String errorMsg) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null) {
            return;
        }

        String valueStr = String.valueOf(value);
        String maxStr = String.valueOf(max);

        if (Double.valueOf(maxStr) < Double.valueOf(valueStr)) {
            validate.error(name, errorMsg);
        }
    }

    /**
     * 最小チェック
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param min 最小値
     * @param errorMsg エラーメッセージ
     */
    private static void doMinCheck(ValidateHelper validate, String name, Object value, Object min, String errorMsg) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null) {
            return;
        }

        String valueStr = String.valueOf(value);
        String minStr = String.valueOf(min);

        if (Double.valueOf(valueStr) < Double.valueOf(minStr)) {
            validate.error(name, errorMsg);
        }
    }
}
