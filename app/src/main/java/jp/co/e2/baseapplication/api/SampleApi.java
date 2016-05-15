package jp.co.e2.baseapplication.api;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;

import jp.co.e2.baseapplication.common.LogUtils;
import jp.co.e2.baseapplication.entity.SampleApiEntity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * サンプル非同期HTTP通信処理
 */
public class SampleApi {
    /**
     *
     *
     * @return サンプルAPIエンティティ
     */
    public static Observable<SampleApiEntity> request(final String url) {
        Observable observable = Observable.create(new Observable.OnSubscribe<SampleApiEntity>() {
            @Override
            public void call(final Subscriber<? super SampleApiEntity> subscriber) {
                try {
                    LogUtils.d("URL : " + url);

                    //リクエスト生成
                    Request request = new Request.Builder().url(url).get().build();

                    //通信を行う
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Response response = okHttpClient.newCall(request).execute();
                    int httpStatus = response.code();
                    String body = response.body().string();

                    LogUtils.d("HttpStatus : " + httpStatus);
                    LogUtils.d("body : " + body);

                    //HTTPステータスが200以外
                    if (httpStatus != HttpURLConnection.HTTP_OK || body.length() == 0) {
                        subscriber.onError(new Exception());
                        return;
                    }

                    //JSONパースを行う
                    Gson gson = new Gson();
                    SampleApiEntity result = gson.fromJson(body, SampleApiEntity.class);

                    //レスポンス内のコードが200以外
                    if (result.getCode() != HttpURLConnection.HTTP_OK) {
                        subscriber.onError(new Exception());
                        return;
                    }

                    subscriber.onNext(result);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
        observable.subscribeOn(Schedulers.newThread());
        observable.observeOn(AndroidSchedulers.mainThread());

        return  observable;
    }
}
