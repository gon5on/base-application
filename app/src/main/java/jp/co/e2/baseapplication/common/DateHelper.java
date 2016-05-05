package jp.co.e2.baseapplication.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日時関連の便利なものをまとめたクラス
 */
public class DateHelper {
    //フォーマット
    public static final String FMT_DATE = "yyyy-MM-dd";
    public static final String FMT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FMT_DATE_SLASH = "yyyy/MM/dd";
    public static final String FMT_DATETIME_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final String FMT_DATE_DOT = "yyyy.MM.dd";
    public static final String FMT_DATETIME_DOT = "yyyy.MM.dd HH:mm:ss";
    public static final String FMT_DATE_NO_UNIT = "yyyyMMdd";

    private Calendar mCal = null;

    /**
     * コンストラクタ
     */
    public DateHelper() {
        mCal = Calendar.getInstance();
    }

    /**
     * コンストラクタ
     *
     * @param cal カレンダークラス
     */
    public DateHelper(Calendar cal) {
        mCal = cal;
    }

    /**
     * コンストラクタ
     *
     * @param time ミリ秒
     */
    public DateHelper(Long time) {
        mCal = Calendar.getInstance();
        mCal.clear();
        mCal.setTimeInMillis(time);
    }

    /**
     * コンストラクタ
     *
     * @param date Dateクラス
     */
    public DateHelper(Date date) {
        mCal = Calendar.getInstance();
        mCal.clear();
        mCal.setTime(date);
    }

    /**
     * コンストラクタ
     *
     * @param year 年
     * @param month 月
     * @param day 日
     */
    public DateHelper(int year, int month, int day) {
        mCal = Calendar.getInstance();
        mCal.clear();

        setYear(year);
        setMonth(month);
        setDay(day);
    }

    /**
     * コンストラクタ
     *
     * @param strDate 日付文字列
     * @param format 日付文字列の形式
     * @throws ParseException
     */
    public DateHelper(String strDate, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date date = sdf.parse(strDate);

        mCal = Calendar.getInstance();
        mCal.clear();
        mCal.setTime(date);
    }

    /**
     * 年をセットする
     *
     * @param value 年
     */
    public void setYear(int value) {
        mCal.set(Calendar.YEAR, value);
    }

    /**
     * 月をセットする
     *
     * @param value 月
     */
    public void setMonth(int value) {
        mCal.set(Calendar.MONTH, (value -1));
    }

    /**
     * 日をセットする
     *
     * @param value 日
     */
    public void setDay(int value) {
        mCal.set(Calendar.DATE, value);
    }

    /**
     * 時をセットする
     *
     * @param value 時
     */
    public void setHour(int value) {
        mCal.set(Calendar.HOUR_OF_DAY, value);
    }

    /**
     * 分をセットする
     *
     * @param value 分
     */
    public void setMin(int value) {
        mCal.set(Calendar.MINUTE, value);
    }

    /**
     * 秒をセットする
     *
     * @param value 秒
     */
    public void setSec(int value) {
        mCal.set(Calendar.SECOND, value);
    }

    /**
     * ミリ秒をセットする
     *
     * @param value ミリ秒
     */
    public void setMills(int value) {
        mCal.set(Calendar.MILLISECOND, value);
    }

    /**
     * 時間以降をクリアする（日付だけを使いたい場合に使用）
     */
    public void clearHour() {
        mCal.set(Calendar.HOUR_OF_DAY, 0);
        mCal.set(Calendar.MINUTE, 0);
        mCal.set(Calendar.SECOND, 0);
        mCal.set(Calendar.MILLISECOND, 0);
    }

    /**
     * 年の加減算
     *
     * マイナスを指定すれば過去が計算可能
     * 足さない部分には0を指定する
     *
     * @param add 何年後
     */
    public void addYear(Integer add) {
        add(add, 0, 0, 0, 0, 0);
    }

    /**
     * 月の加減算
     *
     * マイナスを指定すれば過去が計算可能
     * 足さない部分には0を指定する
     *
     * @param add 何か月後
     */
    public void addMonth(Integer add) {
        add(0, add, 0, 0, 0, 0);
    }

    /**
     * 日の加減算
     *
     * マイナスを指定すれば過去が計算可能
     * 足さない部分には0を指定する
     *
     * @param add 何日後
     */
    public void addDay(Integer add) {
        add(0, 0, add, 0, 0, 0);
    }

    /**
     * 時の加減算
     *
     * マイナスを指定すれば過去が計算可能
     * 足さない部分には0を指定する
     *
     * @param add 何時間後
     */
    public void addHour(Integer add) {
        add(0, 0, 0, add, 0, 0);
    }

    /**
     * 分の加減算
     *
     * マイナスを指定すれば過去が計算可能
     * 足さない部分には0を指定する
     *
     * @param add 何分後
     */
    public void addMin(Integer add) {
        add(0, 0, 0, 0, add, 0);
    }

    /**
     * 秒の加減算
     *
     * マイナスを指定すれば過去が計算可能
     * 足さない部分には0を指定する
     *
     * @param add 何秒後
     */
    public void addSec(Integer add) {
        add(0, 0, 0, 0, 0, add);
    }

    /**
     * 日時の加減算
     *
     * マイナスを指定すれば何日前が計算可能
     * 足さない部分には0を指定する
     *
     * @param addYear 何年後
     * @param addMonth 何か月後
     * @param addDay 何日後
     * @param addHour 何時間後
     * @param addMin 何分後
     * @param addSec 何秒後
     */
    public void add(Integer addYear, Integer addMonth, Integer addDay, Integer addHour, Integer addMin, Integer addSec) {
        mCal.add(Calendar.YEAR, addYear);
        mCal.add(Calendar.MONTH, addMonth);
        mCal.add(Calendar.DATE, addDay);
        mCal.add(Calendar.HOUR_OF_DAY, addHour);
        mCal.add(Calendar.MINUTE, addMin);
        mCal.add(Calendar.SECOND, addSec);
    }

    /**
     * フォーマットした日時を返す
     *
     * @param format フォーマット
     * @return String フォーマットした日付文字列
     */
    public String format(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

        return sdf.format(mCal.getTime());
    }

    /**
     * 今日までの年齢を計算する
     *
     * @return int 年齢
     */
    public int getAge() {
        int today = Integer.parseInt(new DateHelper().format(FMT_DATE_NO_UNIT));
        int born = Integer.parseInt(format(FMT_DATE_NO_UNIT));

        return ((today - born) / 10000);
    }

    /**
     * カレンダークラスを返す
     *
     * @return Calendar mCal
     */
    public Calendar get() {
        return mCal;
    }

    /**
     * 年を取得
     *
     * @return int 年
     */
    public int getYear() {
        return mCal.get(Calendar.YEAR);
    }

    /**
     * 月を取得
     *
     * そのまま使えるように+1した数字を返すので注意！
     *
     * @return int 月
     */
    public int getMonth() {
        return mCal.get(Calendar.MONTH) + 1;
    }

    /**
     * 日を取得
     *
     * @return int 日
     */
    public int getDay() {
        return mCal.get(Calendar.DATE);
    }

    /**
     * 時を取得
     *
     * @return int 時
     */
    public int getHour() {
        return mCal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 分を取得
     *
     * @return int 分
     */
    public int getMin() {
        return mCal.get(Calendar.MINUTE);
    }

    /**
     * 秒を取得
     *
     * @return int 秒
     */
    public int getSec() {
        return mCal.get(Calendar.SECOND);
    }

    /**
     * ミリ秒を取得
     *
     * @return long ミリ秒
     */
    public long getMillis() {
        return mCal.getTimeInMillis();
    }

    /**
     * 曜日を取得
     *
     * @return int 曜日（1=日曜日 … 7=土曜日）
     */
    public long getWeek() {
        return mCal.get(Calendar.DAY_OF_WEEK);
    }
}