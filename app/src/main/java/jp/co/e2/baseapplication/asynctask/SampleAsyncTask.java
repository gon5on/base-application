package jp.co.e2.baseapplication.asynctask;

import android.content.Context;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import jp.co.e2.baseapplication.common.LogUtils;
import jp.co.e2.baseapplication.entity.SampleApiEntity;

/**
 * サンプル非同期HTTP通信処理
 */
public class SampleAsyncTask extends BaseAsyncTask<String, Integer, SampleApiEntity> {
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
    protected SampleApiEntity doInBackground(String... params) {
        SampleApiEntity result = null;

        try {
            LogUtils.d("URL : " + params[0]);

            //リクエスト生成
            Request request = new Request.Builder().url(params[0]).get().build();

            //通信を行う
            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();
            int httpStatus = response.code();
            String body = response.body().string();

            LogUtils.d("HttpStatus : " + httpStatus);
            LogUtils.d("body : " + body);

            //JSONパースを行う
            Gson gson = new Gson();
            result = gson.fromJson(body, SampleApiEntity.class);
        }
        catch (Exception e) {
            //SampleApiEntityの形じゃないとonPostExecuteに処理を返せないので、
            //例外内容を返したい場合、セッターを設けるなどしてSampleApiEntityに例外を入れて返す必要がある
            //もしくはAsyncTaskを拡張して例外を返せるコールバックを作るとか…
            e.printStackTrace();
        }

        return result;
    }
}