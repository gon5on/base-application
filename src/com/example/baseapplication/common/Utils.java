package com.example.baseapplication.common;

/**
 * 便利なものまとめたクラス
 * 
 * newしなくても使える
 * 
 * @access public
 */
public class Utils
{
    /**
     * オブジェクトをString型に変換する
     * 
     * @param Object value
     * @return String
     * @access public
     */
    public static String objToString(Object value)
    {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    /**
     * オブジェクトをInteger型に変換する
     * 
     * @param Object value
     * @return Integer
     * @access public
     */
    public static Integer objToInteger(Object value)
    {
        if (value == null) {
            return null;
        }
        return Integer.parseInt(String.valueOf(value));
    }

    /**
     * オブジェクトをDouble型に変換する
     * 
     * @param Object value
     * @return Integer
     * @access public
     */
    public static Double objToDouble(Object value)
    {
        if (value == null) {
            return null;
        }
        return Double.parseDouble(String.valueOf(value));
    }

    /**
     * オブジェクトをFloat型に変換する
     * 
     * @param Object value
     * @return Integer
     * @access public
     */
    public static Float objToFloat(Object value)
    {
        if (value == null) {
            return null;
        }
        return Float.parseFloat(String.valueOf(value));
    }

    /**
     * オブジェクトをLong型に変換する
     * 
     * @param Object value
     * @return Integer
     * @access public
     */
    public static Long objToLong(Object value)
    {
        if (value == null) {
            return null;
        }
        return Long.parseLong(String.valueOf(value));
    }
}
