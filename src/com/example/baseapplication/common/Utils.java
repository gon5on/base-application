package com.example.baseapplication.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.res.Resources;

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
     * Implode
     * 
     * @param ArrayList<String> list 文字列配列
     * @param String delimiter デリミタ
     * @return String 連結文字列
     * @access public
     */
    public static String implode(ArrayList<String> list, String delimiter)
    {
        StringBuilder sb = new StringBuilder();

        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                if (sb.length() != 0) {
                    sb.append(delimiter);
                }

                sb.append(list.get(i));
            }
        }

        return sb.toString();
    }

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

    /**
     * ファイルからテキストを読みだす
     * 
     * @param Resources res リソース
     * @param Integer resId ファイルのリソースID
     * @return Integer
     * @throws IOException
     * @access public
     */
    public static String readTextFile(Resources res, Integer resId) throws IOException
    {
        InputStream is = null;
        BufferedReader br = null;

        StringBuilder sb = new StringBuilder();

        try {
            is = res.openRawResource(resId);
            br = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str + "\n");
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return sb.toString();
    }
}
