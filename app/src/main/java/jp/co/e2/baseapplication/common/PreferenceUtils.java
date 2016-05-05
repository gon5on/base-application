package jp.co.e2.baseapplication.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * プリファレンスについて便利なものをまとめたクラス
 *
 * newしなくても使える
 */
public class PreferenceUtils {
    /**
     * プリファレンスからInt型の値を取得
     *
     * @param context コンテキスト
     * @param name 名前
     * @param def デフォルト値
     * @return Integer
     */
    public static Integer get(Context context, String name, Integer def) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(name, def);
    }

    /**
     * プリファレンスからString型の値を取得
     *
     * @param context コンテキスト
     * @param name 名前
     * @param def デフォルト値
     * @return String
     */
    public static String get(Context context, String name, String def) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(name, def);
    }

    /**
     * プリファレンスからFloat型の値を取得
     *
     * @param context コンテキスト
     * @param name 名前
     * @param def デフォルト値
     * @return Long
     */
    public static Float get(Context context, String name, Float def) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getFloat(name, def);
    }

    /**
     * プリファレンスからLong型の値を取得
     *
     * @param context コンテキスト
     * @param name 名前
     * @param def デフォルト値
     * @return Long
     */
    public static Long get(Context context, String name, Long def) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(name, def);
    }

    /**
     * プリファレンスからBoolean型の値を取得
     *
     * @param context コンテキスト
     * @param name 名前
     * @param def デフォルト値
     * @return Boolean
     */
    public static Boolean get(Context context, String name, boolean def) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(name, def);
    }

    /**
     * プリファレンスにInt型の値を保存
     *
     * @param context コンテキスト
     * @param name 名前
     * @param value 保存する値
     */
    public static void save(Context context, String name, Integer value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(name, value).apply();
    }

    /**
     * プリファレンスにString型の値を保存
     *
     * @param context コンテキスト
     * @param name 名前
     * @param value 保存する値
     */
    public static void save(Context context, String name, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(name, value).apply();
    }

    /**
     * プリファレンスにFloat型の値を保存
     *
     * @param context コンテキスト
     * @param name    名前
     * @param value   保存する値
     */
    public static void save(Context context, String name, Float value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putFloat(name, value).apply();
    }

    /**
     * プリファレンスにLong型の値を保存
     *
     * @param context コンテキスト
     * @param name    名前
     * @param value   保存する値
     */
    public static void save(Context context, String name, Long value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putLong(name, value).apply();
    }

    /**
     * プリファレンスにBoolean型の値を保存
     *
     * @param context コンテキスト
     * @param name 名前
     * @param value 保存する値
     */
    public static void save(Context context, String name, Boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(name, value).apply();
    }

    /**
     * プリファレンスの値を消す
     *
     * @param context コンテキスト
     * @param name 名前
     */
    public static void delete(Context context, String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().remove(name).apply();
    }

    /**
     * プリファレンスの値を全て消す
     *
     * @param context コンテキスト
     */
    public static void deleteAll(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().clear().apply();
    }
}
