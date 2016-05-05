package jp.co.e2.baseapplication.validate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.co.e2.baseapplication.common.DateHelper;

/**
 * 日付系バリデーションクラス
 */
public class ValidateDate {
    /**
     * 正しい日付かどうかチェック
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param format フォーマット
     * @param errorMsg エラーメッセージ
     */
    public static void check(ValidateHelper validate, String name, String value, String format, String errorMsg) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null || value.length() == 0) {
            return;
        }

        try {
            //日付形式に変換できるかどうか
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            sdf.setLenient(false);
            Date tmp = sdf.parse(value);

            SimpleDateFormat sdf2 = new SimpleDateFormat(DateHelper.FMT_DATE_NO_UNIT, Locale.getDefault());
            Integer date = Integer.parseInt(sdf2.format(tmp.getTime()));
        }
        catch (ParseException e) {
            //日付形式に変換失敗
            validate.error(name, errorMsg);
        }
    }

    /**
     * 未来日かどうかチェック
     *
     * ※今日を指定されたらエラー
     * ※必ず日付形式かどうかをチェックしてから使用すること
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param format フォーマット
     * @param errorMsg エラーメッセージ
     */
    public static void isFuture(ValidateHelper validate, String name, String value, String format, String errorMsg) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null || value.length() == 0) {
            return;
        }

        if (0 <= compareTo(value, format)) {
            validate.error(name, errorMsg);
        }
    }

    /**
     * 未来日かどうかチェック
     *
     * ※今日を指定されてもエラーにしない
     * ※必ず日付形式かどうかをチェックしてから使用すること
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param format フォーマット
     * @param errorMsg エラーメッセージ
     */
    public static void isFutureAllowToday(ValidateHelper validate, String name, String value, String format, String errorMsg) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null || value.length() == 0) {
            return;
        }

        if (0 <= compareTo(value, format)) {
            validate.error(name, errorMsg);
        }
    }

    /**
     * 過去日かどうかチェック
     *
     * ※今日を指定されたらエラー
     * ※必ず日付形式かどうかをチェックしてから使用すること
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param format フォーマット
     * @param errorMsg エラーメッセージ
     */
    public static void isPast(ValidateHelper validate, String name, String value, String format, String errorMsg) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null || value.length() == 0) {
            return;
        }

        if (compareTo(value, format) <= 0) {
            validate.error(name, errorMsg);
        }
    }

    /**
     * 過去日かどうかチェック
     *
     * ※今日を指定されてもエラーではない
     * ※必ず日付形式かどうかをチェックしてから使用すること
     *
     * @param validate バリデートクラス
     * @param name 変数名
     * @param value 値
     * @param format フォーマット
     * @param errorMsg エラーメッセージ
     */
    public static void isPastAllowToday(ValidateHelper validate, String name, String value, String format, String errorMsg) {
        if (!validate.getResult(name)) {
            return;
        }

        if (value == null || value.length() == 0) {
            return;
        }

        if (compareTo(value, format) < 0) {
            validate.error(name, errorMsg);
        }
    }

    /**
     * 今日と指定日を比べる
     *
     * @param value  値
     * @param format フォーマット
     * @return Integer compareTo()の結果
     */
    private static Integer compareTo(String value, String format) {
        Integer ret = null;

        try {
            //今日のミリ秒を取得
            DateHelper dateUtils = new DateHelper();
            dateUtils.clearHour();
            Long border = dateUtils.get().getTimeInMillis();

            //指定日のミリ秒を取得
            Long date = new DateHelper(value, format).get().getTimeInMillis();

            //比較
            ret = border.compareTo(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
