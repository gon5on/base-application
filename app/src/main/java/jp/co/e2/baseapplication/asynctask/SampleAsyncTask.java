package jp.co.e2.baseapplication.asynctask;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * サンプル非同期処理
 */
public class SampleAsyncTask extends BaseAsyncTask<String, Integer, String> {
    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public SampleAsyncTask(Integer tag, Context context) {
        super(tag, context);
    }

    /**
     * ${inheritDoc}
     */
    @Override
    protected String doInBackground(String... params) {
        String result = null;

        //適当にスリープ
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //引数として渡されたURLにアクセスして、レスポンスボディを結果として返す
        //本来は例外をキャッチしたら何らかの処理が必要
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            result =  sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}