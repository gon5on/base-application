package jp.co.e2.baseapplication.http;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.greenrobot.eventbus.EventBus;

import java.net.HttpURLConnection;

import jp.co.e2.baseapplication.entity.SampleApiEntity;

/**
 * サンプル非同期HTTP通信処理
 */
public class SampleHttp {
    /**
     * 非同期HTTP通信を実行する
     */
    public void execute(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callApi(url);
            }
        }).start();
    }

    /**
     * APIにアクセスする
     *
     * @param url URL
     */
    private void callApi(String url) {
        SampleEvent event = new SampleEvent();

        try {
            //リクエスト生成
            Request request = new Request.Builder().url(url).get().build();

            //HTTPクライアント生成
            OkHttpClient okHttpClient = new OkHttpClient();

            //リクエストとレスポンスをログに吐く
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.interceptors().add(logging);

            //通信を行う
            Response response = okHttpClient.newCall(request).execute();
            String body = response.body().string();

            //200OK
            if (response.isSuccessful()) {
                //JSONパースを行う
                Gson gson = new Gson();
                SampleApiEntity sampleApiEntity = gson.fromJson(body, SampleApiEntity.class);

                event.setSampleApiEntity(sampleApiEntity);
            }
            //200以外
            else {
                event.setHttpStatus(response.code());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            event.setException(e);
        }

        //メインスレッドに処理を戻す
        EventBus.getDefault().post(event);
    }

    /**
     * EventBus用クラス
     */
    public class SampleEvent {
        private boolean mResult = false;
        private int mHttpStatus;
        private SampleApiEntity mSampleApiEntity;
        private Exception mException;

        /**
         * サンプルエンティティをセットする
         *
         * @param sampleApiEntity サンプルエンティティ
         */
        public void setSampleApiEntity(@NonNull SampleApiEntity sampleApiEntity) {
            if (sampleApiEntity.getCode() == HttpURLConnection.HTTP_OK) {
                mResult = true;
            }

            mHttpStatus = HttpURLConnection.HTTP_OK;
            mSampleApiEntity = sampleApiEntity;
        }

        /**
         * HTTPステータスをセットする
         *
         * @param httpStatus HTTPステータス
         */
        public void setHttpStatus (int httpStatus) {
            mHttpStatus = httpStatus;
        }

        /**
         * 例外をセットする
         *
         * @param exception 例外
         */
        public void setException(@NonNull Exception exception) {
            mException = exception;
        }

        /**
         * 成功かどうか
         *
         * @return mResult
         */
        public boolean isSuccessful() {
            return mResult;
        }

        /**
         * HTTPステータスを返す
         *
         * @return mHttpStatus
         */
        public int getHttpStatus() {
            return mHttpStatus;
        }

        /**
         * サンプルエンティティを返す
         *
         * @return mSampleApiEntity
         */
        public SampleApiEntity getSampleApiEntity() {
            return mSampleApiEntity;
        }

        /**
         * 例外を返す
         *
         * @return mException
         */
        public Exception getException() {
            return mException;
        }
    }
}