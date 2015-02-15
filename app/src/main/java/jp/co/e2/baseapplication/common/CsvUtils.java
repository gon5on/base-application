package jp.co.e2.baseapplication.common;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV読み込み関連のクラス
 */
public class CsvUtils {
    public static final String DELIMITER = ",";                 //区切り文字

    /**
     * rawからファイルを読み込んでCSVを返す
     *
     * @param context コンテキスト
     * @param resId 読み込み元リソースID
     * @param headerFlg ヘッダの有無フラグ
     * @return List<String[]> 内容
     * @throws IOException
     */
    public static List<String[]> readFromRaw(Context context, int resId, boolean headerFlg) throws IOException {
        InputStream inputStream = context.getResources().openRawResource(resId);

        return readFromInputStream(inputStream, headerFlg);
    }

    /**
     * assetからファイルを読み込んでCSVを返す
     *
     * @param context コンテキスト
     * @param filename 読み込み元ファイル名
     * @param headerFlg ヘッダの生むフラグ
     * @return List<String[]> 内容
     * @throws IOException
     */
    public static List<String[]> readFromAssets(Context context, String filename, boolean headerFlg) throws IOException {
        AssetManager assetManager = context.getResources().getAssets();
        InputStream inputStream = assetManager.open(filename);

        return readFromInputStream(inputStream, headerFlg);
    }

    /**
     * InputStreamからCSVを読み込んで返す
     *
     * @param inputStream インプットストリーム
     * @param headerFlg ヘッダの生むフラグ
     * @return List<String[]> 内容
     */
    public static List<String[]> readFromInputStream(InputStream inputStream, boolean headerFlg) throws IOException {
        List<String[]> data = new ArrayList< String [] >();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            int i = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                i++;

                //ヘッダフラグがある場合、1行目は無視する
                if (headerFlg && i == 1) {
                    continue;
                }

                String[] row = line.split(DELIMITER);
                data.add(row);
            }
        } finally {
            inputStream.close();
        }

        return data;
    }
}