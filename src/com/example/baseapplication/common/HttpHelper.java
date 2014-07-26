package com.example.baseapplication.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.baseapplication.debug.Debug;

/**
 * HTTP通信クラス
 * 
 * GET/POST/DELETEを実装、ベーシック認証にも対応
 * 
 * @access public
 */
public class HttpHelper
{
    // メソッド
    public static final String GET = "get";
    public static final String POST = "post";
    public static final String PUT = "put";
    public static final String DELETE = "delete";

    // ステータス
    public static final Integer STATUS_OK = 200;                        // 正常
    public static final Integer STATUS_ERROR_NO_NETWORK1 = 301;         // 通信可能環境下にない1
    public static final Integer STATUS_ERROR_NO_NETWORK2 = 302;         // 通信可能環境下にない2
    public static final Integer STATUS_ERROR_NO_NETWORK3 = 303;         // 通信可能環境下にない3
    public static final Integer STATUS_UNSUPPORTED_ENC_EX = 304;        // UnsupportedEncodingException
    public static final Integer STATUS_CLIENT_PROTOCOL_EX = 305;        // ClientProtocolException
    public static final Integer STATUS_ILLIGAL_STATE_EX = 306;          // IllegalStateException
    public static final Integer STATUS_IO_EX = 307;                     // IOException
    public static final Integer STATUS_HTTP_EX = 308;                   // HttpException
    public static final Integer STATUS_SOCKET_TIMEOUT_EX = 309;         // SocketTimeoutException
    public static final Integer STATUS_CONN_TIMEOUT_EX = 310;           // ConnectTimeoutException
    public static final Integer STATUS_UNKNOWN_HOST_EX = 311;           // UnknownHostException
    public static final Integer STATUS_OTHER_EX = 399;                  // その他例外の場合
    public static final Integer STATUS_BLANC = 900;                     // ステータスが最後まで空だった場合

    //SSLのプロトコル
    public static final String PROTOCOL_SSL = "https";

    // ポート番号
    private static final Integer DEFAULT_PORT = 80;                     // デフォルトのポート番号
    private static final Integer SSL_PORT = 443;                        // SSLのポート番号

    //文字コード
    private static final String ENCODE = "UTF-8";                       // 文字コード

    // タイムアウト
    private Integer CONNECTION_TIMEOUT = 10000;                         // 接続タイムアウト
    private Integer SO_TIMEOUT = 10000;                                 // ソケットタイムアウト

    // 変数
    private Context mContext;                                           // コンテキスト
    private String mUrl;                                                // 接続URL
    private String mMethod;                                             // メソッド
    private String mBody;                                               // レスポンスボディ
    private Integer mStatus = 0;                                        // レスポンスステータス
    private Integer mHttpStatus = 0;                                    // HTTPレスポンス

    private HashMap<String, String> mHeadersMap;                        // リクエストヘッダのハッシュマップ
    private HashMap<String, String> mParamsMap;                         // リクエストボディのハッシュマップ（name=valueの形）
    private String mParamsString;                                       // リクエストボディの文字列（valueのみの形）

    private String mBasicAuthId;                                        // ベーシック認証のID
    private String mBasicAuthPass;                                      // ベーシック認証のPASS

    /**
     * コンストラクタ
     * 
     * @param Context applicationContext
     * @param String baseUrl
     * @access public
     */
    public HttpHelper(Context applicationContext)
    {
        setNull();

        mContext = applicationContext;
    }

    /**
     * 通信を行う
     * 
     * @param String method メソッド（POST/GET/PUT/DELETE）
     * @param String action ベースURLの後ろにつくもの
     * @return void
     * @access public
     */
    public void connect(String method, String url)
    {
        // ネットワーク接続確認
        if (checkNetworkStatus() == false) {
            return;
        }

        mUrl = url;
        mMethod = method;
        LogUtils.v("Http", "URL : " + url);

        DefaultHttpClient client = new DefaultHttpClient();

        try {
            HttpResponse response = null;

            //タイムアウト設定
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);

            // ベーシック認証
            client = doBasicAuth(client);

            // GETの場合
            if (mMethod.equals(GET)) {
                response = doConnectGet(client);
            }
            // POSTの場合
            else if (mMethod.equals(POST)) {
                response = doConnectPost(client);
            }
            // PUTの場合
            else if (mMethod.equals(PUT)) {
                response = doConnectPut(client);
            }
            // DELETEの場合
            else if (mMethod.equals(DELETE)) {
                response = doConnectDelete(client);
            }

            // レスポンスが返ってきたらステータスを正常にする
            mStatus = STATUS_OK;

            // レスポンス解析
            analyzeResponce(response);

        } catch (SocketTimeoutException e) {
            mStatus = STATUS_SOCKET_TIMEOUT_EX;
            saveException(e);
            e.printStackTrace();
            return;
        } catch (ConnectTimeoutException e) {
            mStatus = STATUS_CONN_TIMEOUT_EX;
            saveException(e);
            e.printStackTrace();
            return;
        } catch (UnsupportedEncodingException e) {
            mStatus = STATUS_UNSUPPORTED_ENC_EX;
            saveException(e);
            e.printStackTrace();
            return;
        } catch (ClientProtocolException e) {
            mStatus = STATUS_CLIENT_PROTOCOL_EX;
            saveException(e);
            e.printStackTrace();
            return;
        } catch (IllegalStateException e) {
            mStatus = STATUS_ILLIGAL_STATE_EX;
            saveException(e);
            e.printStackTrace();
            return;
        } catch (UnknownHostException e) {
            mStatus = STATUS_UNKNOWN_HOST_EX;
            saveException(e);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            mStatus = STATUS_IO_EX;
            saveException(e);
            e.printStackTrace();
            return;
        } catch (Exception e) {
            mStatus = STATUS_OTHER_EX;
            saveException(e);
            e.printStackTrace();
            return;
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    /**
     * GET通信を行う
     * 
     * @param DefaultHttpClient client
     * @return HttpResponse
     * @throws IOException
     * @throws ClientProtocolException
     * @access private
     */
    private HttpResponse doConnectGet(DefaultHttpClient client) throws ClientProtocolException, IOException
    {
        if (mParamsMap != null) {
            mUrl += "?" + createGetParams(mParamsMap);
        }
        else if (mParamsString != null) {
            mUrl += "?" + mParamsString;
        }
        HttpGet http = new HttpGet(mUrl);
        http = addHeader(http);

        return client.execute(http);
    }

    /**
     * POST通信を行う
     * 
     * @param DefaultHttpClient client
     * @return HttpResponse
     * @throws IOException
     * @throws ClientProtocolException
     * @access private
     */
    private HttpResponse doConnectPost(DefaultHttpClient client) throws ClientProtocolException, IOException
    {
        HttpPost http = new HttpPost(mUrl);

        if (mParamsMap != null) {
            ArrayList<NameValuePair> postParams = createPostParams(mParamsMap);
            http.setEntity(new UrlEncodedFormEntity(postParams, HTTP.UTF_8));
        }
        else if (mParamsString != null) {
            http.setEntity(new StringEntity(mParamsString, HTTP.UTF_8));
        }
        http = addHeader(http);

        return client.execute(http);
    }

    /**
     * PUT通信を行う
     * 
     * @param DefaultHttpClient client
     * @return HttpResponse
     * @throws IOException
     * @throws ClientProtocolException
     * @access private
     */
    private HttpResponse doConnectPut(DefaultHttpClient client) throws ClientProtocolException, IOException
    {
        HttpPut http = new HttpPut(mUrl);

        if (mParamsMap != null) {
            ArrayList<NameValuePair> postParams = createPostParams(mParamsMap);
            http.setEntity(new UrlEncodedFormEntity(postParams, HTTP.UTF_8));
        }
        else if (mParamsString != null) {
            http.setEntity(new StringEntity(mParamsString, HTTP.UTF_8));
        }
        http = addHeader(http);

        return client.execute(http);
    }

    /**
     * DELETE通信を行う
     * 
     * @param DefaultHttpClient client
     * @return HttpResponse
     * @throws IOException
     * @throws ClientProtocolException
     * @access private
     */
    private HttpResponse doConnectDelete(DefaultHttpClient client) throws ClientProtocolException, IOException
    {
        if (mParamsMap != null) {
            mUrl += "?" + createGetParams(mParamsMap);
        }
        else if (mParamsString != null) {
            mUrl += "?" + mParamsString;
        }
        HttpDelete http = new HttpDelete(mUrl);
        http = addHeader(http);

        return client.execute(http);
    }

    /**
     * パラメータをつなげてGET用の文字列にする
     * 
     * @param HashMap<String, String> paramsMap
     * @return String paramsStr
     * @throws UnsupportedEncodingException
     * @access private
     */
    private String createGetParams(HashMap<String, String> paramsMap) throws UnsupportedEncodingException
    {
        String paramsStr = "";

        if (paramsMap != null && paramsMap.size() != 0) {
            for (String key : paramsMap.keySet()) {
                if (paramsStr.length() != 0) {
                    paramsStr += "&";
                }
                paramsStr += (key + "=" + URLEncoder.encode(paramsMap.get(key), ENCODE));
            }
        }
        LogUtils.v("Http", "get params : " + paramsStr);

        return paramsStr;
    }

    /**
     * パラメータをポスト用にセットする
     * 
     * @param HashMap<String, String> paramsMap
     * @return ArrayList<NameValuePair>
     * @access private
     */
    private ArrayList<NameValuePair> createPostParams(HashMap<String, String> paramsMap)
    {
        ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();

        if (paramsMap != null && paramsMap.size() != 0) {
            for (String key : paramsMap.keySet()) {
                LogUtils.v("Http", "post body : " + key + " - " + paramsMap.get(key));
                postParams.add(new BasicNameValuePair(key, paramsMap.get(key)));
            }
        }

        return postParams;
    }

    /**
     * GETにヘッダを付与する
     * 
     * @param HttpGet http
     * @return HttpGet http
     * @access private
     */
    public HttpGet addHeader(HttpGet http)
    {
        if (mHeadersMap != null && mHeadersMap.size() != 0) {
            for (String key : mHeadersMap.keySet()) {
                LogUtils.v("Http", "header : " + key + " - " + mHeadersMap.get(key));
                http.addHeader(key, mHeadersMap.get(key));
            }
        }

        return http;
    }

    /**
     * POSTにヘッダを付与する
     * 
     * @param HttpPost http
     * @return HttpPost http
     * @access private
     */
    public HttpPost addHeader(HttpPost http)
    {
        if (mHeadersMap != null && mHeadersMap.size() != 0) {
            for (String key : mHeadersMap.keySet()) {
                LogUtils.v("Http", "header : " + key + " - " + mHeadersMap.get(key));
                http.addHeader(key, mHeadersMap.get(key));
            }
        }

        return http;
    }

    /**
     * PUTにヘッダを付与する
     * 
     * @param HttpPut http
     * @return HttpPut http
     * @access private
     */
    public HttpPut addHeader(HttpPut http)
    {
        if (mHeadersMap != null && mHeadersMap.size() != 0) {
            for (String key : mHeadersMap.keySet()) {
                LogUtils.v("Http", "header : " + key + " - " + mHeadersMap.get(key));
                http.addHeader(key, mHeadersMap.get(key));
            }
        }

        return http;
    }

    /**
     * DELETEにヘッダを付与する
     * 
     * @param HttpDelete http
     * @return HttpDelete http
     * @access private
     */
    public HttpDelete addHeader(HttpDelete http)
    {
        if (mHeadersMap != null && mHeadersMap.size() != 0) {
            for (String key : mHeadersMap.keySet()) {
                LogUtils.v("Http", "header : " + key + " - " + mHeadersMap.get(key));
                http.addHeader(key, mHeadersMap.get(key));
            }
        }

        return http;
    }

    /**
     * ネットワーク接続確認
     * 
     * @return Boolean
     * @access private
     */
    private Boolean checkNetworkStatus()
    {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivity.getActiveNetworkInfo();

        if (network == null) {
            mStatus = STATUS_ERROR_NO_NETWORK1;
            return false;
        } else {
            if (!network.isAvailable()) {
                mStatus = STATUS_ERROR_NO_NETWORK2;
                return false;
            } else if (!network.isConnectedOrConnecting()) {
                mStatus = STATUS_ERROR_NO_NETWORK3;
                return false;
            }
        }

        return true;
    }

    /**
     * レスポンスを解析する
     * 
     * @param HttpResponse response
     * @return void
     * @throws IOException
     * @throws IllegalStateException
     * @access public
     */
    public void analyzeResponce(HttpResponse response) throws IllegalStateException, IOException
    {
        // HTTPステータス取得解析
        mHttpStatus = response.getStatusLine().getStatusCode();
        LogUtils.v("Http", "http status : " + mHttpStatus);

        // ボディ取得
        if (response.getEntity() != null) {
            InputStream objStream = response.getEntity().getContent();
            InputStreamReader objReader = new InputStreamReader(objStream);
            BufferedReader objBuf = new BufferedReader(objReader);
            StringBuilder objJson = new StringBuilder();
            String sLine;

            while ((sLine = objBuf.readLine()) != null) {
                objJson.append(sLine);
            }

            mBody = objJson.toString();
            objStream.close();
        }
    }

    /**
     * ベーシック認証
     * 
     * @param DefaultHttpClient client
     * @return String url
     * @access private
     */
    private DefaultHttpClient doBasicAuth(DefaultHttpClient client)
    {
        if (mBasicAuthId == null || mBasicAuthPass == null) {
            return client;
        }

        Integer port = DEFAULT_PORT;

        //接続先URLがSSLだったらポート番号を変更する
        String[] urlArray = mUrl.split("://");
        if (urlArray[0].equals(PROTOCOL_SSL)) {
            port = SSL_PORT;
        }

        //ドメイン抽出のために/で切り分ける
        String[] urlArray2 = urlArray[1].split("/");

        Credentials credentials = new UsernamePasswordCredentials(mBasicAuthId, mBasicAuthPass);
        AuthScope scope = new AuthScope(urlArray2[0], port);
        client.getCredentialsProvider().setCredentials(scope, credentials);

        return client;
    }

    /**
     * 例外内容とその他デバッグ用の通信情報を作成
     * 
     * @param Exception e
     * @return void
     * @access private
     */
    private void saveException(Exception e)
    {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("url", mUrl);
        map.put("method", mMethod);
        map.put("status", String.valueOf(mStatus));
        map.put("http status", String.valueOf(mHttpStatus));

        if (mBasicAuthId != null || mBasicAuthPass != null) {
            map.put("basic auth", String.valueOf(mBasicAuthId) + " / " + String.valueOf(mBasicAuthPass));
        }

        if (mHeadersMap != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");

            for (String key : mHeadersMap.keySet()) {
                sb.append(key + " ： " + mHeadersMap.get(key) + "\n");
            }
            map.put("header params", sb.toString());
        }

        if (mParamsMap != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n");

            for (String key : mParamsMap.keySet()) {
                sb.append(key + " ： " + mParamsMap.get(key) + "\n");
            }
            map.put("request params", sb.toString());
        }

        if (mParamsString != null) {
            map.put("request params", mParamsString);
        }

        if (mBody != null) {
            map.put("responce body", mBody);
        } else {
            map.put("responce body", "null");
        }

        Debug.save(mContext, e, map);
    }

    /**
     * 初期化を行う
     * 
     * @return void
     * @access private
     */
    private void setNull()
    {
        mContext = null;
        mUrl = null;
        mBody = null;
        mStatus = null;
        mHttpStatus = null;

        mParamsMap = null;
        mHeadersMap = null;
        mParamsString = null;

        mBasicAuthId = null;
        mBasicAuthPass = null;
    }

    /**
     * 接続タイムアウトの時間をセット
     * 
     * @param Integer value 接続タイムアウトまでの時間
     * @return void
     * @access public
     */
    public void setConnectionTimeout(Integer value)
    {
        CONNECTION_TIMEOUT = value;
    }

    /**
     * ソケットイムアウトの時間をセット
     * 
     * @param Integer value 接続タイムアウトまでの時間
     * @return void
     * @access public
     */
    public void setSoTimeout(Integer value)
    {
        SO_TIMEOUT = value;
    }

    /**
     * ヘッダのパラメータを受け取る
     * 
     * @param HashMap<String, String> heddersMap ヘッダのパラメータ
     * @return void
     * @access public
     */
    public void setHeaders(HashMap<String, String> heddersMap)
    {
        mHeadersMap = heddersMap;
    }

    /**
     * name=valueの形のパラメータを受け取る
     * 
     * @param HashMap<String, String> paramsMap パラメータ
     * @return void
     * @access public
     */
    public void setParams(HashMap<String, String> paramsMap)
    {
        mParamsMap = paramsMap;
    }

    /**
     * name=valueの形でない、valueだけのパラメータを受け取る
     * 
     * @param String paramsString パラメータ
     * @return void
     * @access public
     */
    public void setSingleStringParams(String paramsString)
    {
        mParamsString = paramsString;
        LogUtils.v("Http", "params string : " + paramsString);
    }

    /**
     * ベーシック認証のIDを受け取る
     * 
     * @param String basicAuthId
     * @return void
     * @access public
     */
    public void setBasicAuthId(String basicAuthId)
    {
        mBasicAuthId = basicAuthId;
        LogUtils.v("Http", "basic auth id : " + basicAuthId);
    }

    /**
     * ベーシック認証のPASSを受け取る
     * 
     * @param String basicAuthPass
     * @return void
     * @access public
     */
    public void setBasicAuthPass(String basicAuthPass)
    {
        mBasicAuthPass = basicAuthPass;
        LogUtils.v("Http", "basic auth pass : " + basicAuthPass);
    }

    /**
     * ステータスを返す
     * 
     * @return Integer mStatus テータス
     * @access public
     */
    public Integer getStatus()
    {
        //仮にステータスが入ってなかった場合、不明なエラーステータスを入れる
        if (mStatus == 0) {
            mStatus = STATUS_BLANC;
        }

        return mStatus;
    }

    /**
     * HTTPテータスを返す
     * 
     * @return Integer mHttpStatus HTTPテータス
     * @access public
     */
    public Integer getHttpStatus()
    {
        return mHttpStatus;
    }

    /**
     * レスポンスを返す
     * 
     * @return String mBody レスポンス
     * @access public
     */
    public String getBody()
    {
        return mBody;
    }
}
