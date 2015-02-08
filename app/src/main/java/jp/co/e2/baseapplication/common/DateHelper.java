package jp.co.e2.baseapplication.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日時関連の便利なものをまとめたクラス
 *
 * @access public
 */
public class DateHelper {
    //フォーマット
    public static final String FMT_DATE = "yyyy-MM-dd";
    public static final String FMT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FMT_DATE_SLASH = "yyyy/MM/dd";
    public static final String FMT_DATETIME_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final String FMT_DATE_DOT = "yyyy.MM.dd";
    public static final String FMT_DATETIME_DOT = "yyyy.MM.dd HH:mm:ss";
    public static final String FMT_DATE_JP = "yyyy年MM月dd日";
    public static final String FMT_DATETIME_JP = "yyyy年MM月dd日 HH時mm分";
    public static final String FMT_DATETIME_JP_FULL = "yyyy年MM月dd日 HH時mm分ss秒";
    public static final String FMT_DATE_NO_UNIT = "yyyyMMdd";

    private Calendar mCal = null;

    /**
     * コンストラクタ
     *
     * @access public
     */
    public DateHelper() {
        mCal = Calendar.getInstance();
    }

    /**
     * コンストラクタ
     *
     * @param cal
     * @access public
     */
    public DateHelper(Calendar cal) {
        mCal = cal;
    }

    /**
     * コンストラクタ
     *
     * @param time
     * @access public
     */
    public DateHelper(Long time) {
        mCal = Calendar.getInstance();
        mCal.clear();
        mCal.setTimeInMillis(time);
    }

    /**
     * コンストラクタ
     *
     * @param date
     * @access public
     */
    public DateHelper(Date date) {
        mCal = Calendar.getInstance();
        mCal.clear();
        mCal.setTime(date);
    }

    /**
     * コンストラクタ
     *
     * @param strDate
     * @param format
     * @throws ParseException
     * @access public
     */
    public DateHelper(String strDate, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(strDate);

        mCal = Calendar.getInstance();
        mCal.clear();
        mCal.setTime(date);
    }

    /**
     * 時間以降をクリアする（日付だけを使いたい場合に使用）
     *
     * @return void
     * @access public
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
     * @access public
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
     * @access public
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
     * @access public
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
     * @access public
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
     * @access public
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
     * @access public
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
     * @param addYear  何年後
     * @param addMonth 何か月後
     * @param addDay   何日後
     * @param addHour  何時間後
     * @param addMin   何分後
     * @param addSec   何秒後
     * @access public
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
     * @param format
     * @return String
     * @access public
     */
    public String format(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(mCal.getTime());
    }

    /**
     * 今日までの年齢を計算する
     *
     * @return int
     * @access public
     */
    public int getAge() {
        int today = Integer.parseInt(new DateHelper().format(FMT_DATE_NO_UNIT));
        int born = Integer.parseInt(format(FMT_DATE_NO_UNIT));

        return (int)((today - born) / 10000);
    }

    /**
     * カレンダークラスを返す
     *
     * @return Calendar mCal
     * @access public
     */
    public Calendar get() {
        return mCal;
    }
}
