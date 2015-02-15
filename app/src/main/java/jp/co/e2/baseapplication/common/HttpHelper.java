package jp.co.e2.baseapplication.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import jp.co.e2.baseapplication.debug.DebugHelper;

/**
 * HTTP通信クラス
 *
 * GET/POST/DELETEを実装、ベーシック認証にも対応
 */
public class HttpHelper {
    // メソッド
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    //クッキー
    public static final String COOKIE = "Cookie";

    // ステータス
    public static final int STATUS_OK = 200;                            // 正常
    public static final int STATUS_ERROR_NO_NETWORK1 = 301;             // 通信可能環境下にない1
    public static final int STATUS_ERROR_NO_NETWORK2 = 302;             // 通信可能環境下にない2
    public static final int STATUS_ERROR_NO_NETWORK3 = 303;             // 通信可能環境下にない3
    public static final int STATUS_UNSUPPORTED_ENC_EX = 304;            // UnsupportedEncodingException
    public static final int STATUS_CLIENT_PROTOCOL_EX = 305;            // ClientProtocolException
    public static final int STATUS_ILLEGALS_STATE_EX = 306;             // IllegalStateException
    public static final int STATUS_IO_EX = 307;                         // IOException
    public static final int STATUS_SOCKET_TIMEOUT_EX = 308;             // SocketTimeoutException
    public static final int STATUS_CONN_TIMEOUT_EX = 309;               // ConnectTimeoutException
    public static final int STATUS_UNKNOWN_HOST_EX = 310;               // UnknownHostException
    public static final int STATUS_FILE_NOT_FOUND_EX = 311;             // FileNotFoundException
    public static final int STATUS_OTHER_EX = 399;                      // その他例外の場合
    public static final int STATUS_BLANK = 900;                         // ステータスが最後まで空だった場合

    //SSLのプロトコル
    public static final String PROTOCOL_SSL = "https";

    // ポート番号
    private static final int DEFAULT_PORT = 80;                         // デフォルトのポート番号
    private static final int SSL_PORT = 443;                            // SSLのポート番号

    //文字コード
    private static final String ENCODE = HTTP.UTF_8;                    // 文字コード

    // タイムアウト
    protected int CONNECTION_TIMEOUT = 5000;                            // 接続タイムアウト
    protected int SO_TIMEOUT = 10000;                                   // ソケットタイムアウト

    // 変数
    protected Context mContext;                                         // コンテキスト
    protected String mUrl;                                              // 接続URL
    protected String mMethod;                                           // メソッド
    protected DefaultHttpClient mDefaultHttpClient;                     // HTTPクライアント
    protected HttpResponse mHttpResponse;                               // HTTPレスポンス
    protected String mBody;                                             // レスポンスボディ
    protected Integer mStatus = 0;                                      // ステータス
    protected Integer mHttpStatus = 0;                                  // HTTPステータス

    protected HashMap<String, String> mHeadersMap;                      // リクエストヘッダのハッシュマップ
    protected HashMap<String, String> mParamsMap;                       // リクエストボディのハッシュマップ（name=valueの形）
    protected HashMap<String, ArrayList<String>> mArrayParamsMap;       // リクエストボディの配列のハッシュマップ（name=valueの形）
    protected ArrayList<String> mUrlParamsList;                         // URLにスラッシ区切りで付与するパラメータのリスト
    protected HashMap<String, String> mBinaryParamsList;                // バイナリのリクエストボディリスト、post・putのみ付与される
    protected String mParamsString;                                     // リクエストボディの文字列（valueのみの形）
    protected String mCookie;                                           // 追加するクッキー

    protected String mBasicAuthId;                                      // ベーシック認証のID
    protected String mBasicAuthPass;                                    // ベーシック認証のPASS

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public HttpHelper(Context context) {
        init();

        mContext = context;
    }

    /**
     * 通信を行う
     *
     * @param method メソッド（POST/GET/PUT/DELETE）
     */
    public void connect(String method, String url) {
        // ネットワーク接続確認
        if (!checkNetworkStatus()) {
            return;
        }

        try {
            mUrl = createUrl(url);
            mMethod = method;

            LogUtils.d("HttpHelper", "method : " + method);
            LogUtils.d("HttpHelper", "URL : " + mUrl);

            mDefaultHttpClient = new DefaultHttpClient();

            //タイムアウト設定
            HttpParams params = mDefaultHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);

            // ベーシック認証
            doBasicAuth();

            // GETの場合
            if (mMethod.equals(GET)) {
                mHttpResponse = doConnectGet();
            }
            // POSTの場合
            else if (mMethod.equals(POST)) {
                mHttpResponse = doConnectPost();
            }
            // PUTの場合
            else if (mMethod.equals(PUT)) {
                mHttpResponse = doConnectPut();
            }
            // DELETEの場合
            else if (mMethod.equals(DELETE)) {
                mHttpResponse = doConnectDelete();
            }

            // レスポンスが返ってきたらステータスを正常にする
            mStatus = STATUS_OK;

            // レスポンス解析
            analyzeResponse();

        } catch (FileNotFoundException e) {
            mStatus = STATUS_FILE_NOT_FOUND_EX;
            saveException(e);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            mStatus = STATUS_SOCKET_TIMEOUT_EX;
            saveException(e);
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            mStatus = STATUS_CONN_TIMEOUT_EX;
            saveException(e);
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            mStatus = STATUS_UNSUPPORTED_ENC_EX;
            saveException(e);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            mStatus = STATUS_CLIENT_PROTOCOL_EX;
            saveException(e);
            e.printStackTrace();
        } catch (IllegalStateException e) {
            mStatus = STATUS_ILLEGALS_STATE_EX;
            saveException(e);
            e.printStackTrace();
        } catch (UnknownHostException e) {
            mStatus = STATUS_UNKNOWN_HOST_EX;
            saveException(e);
            e.printStackTrace();
        } catch (IOException e) {
            mStatus = STATUS_IO_EX;
            saveException(e);
            e.printStackTrace();
        } catch (Exception e) {
            mStatus = STATUS_OTHER_EX;
            saveException(e);
            e.printStackTrace();
        } finally {
            mDefaultHttpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * GET通信を行う
     *
     * @return HttpResponse
     * @throws IOException
     */
    private HttpResponse doConnectGet() throws IOException {
        if (mParamsMap != null) {
            mUrl += ("?" + createGetParams());
        }
        else if (mParamsString != null) {
            mUrl += ("?" + mParamsString);
        }

        HttpGet http = new HttpGet(mUrl);
        http = addHeader(http);

        return mDefaultHttpClient.execute(http);
    }

    /**
     * POST通信を行う
     *
     * @return HttpResponse
     * @throws IOException
     */
    private HttpResponse doConnectPost() throws IOException {
        HttpPost http = new HttpPost(mUrl);

        if (mBinaryParamsList != null) {
            http.setEntity(createBinaryPostParams());
        }
        else if (mParamsMap != null || mArrayParamsMap != null) {
            ArrayList<NameValuePair> postParams = createPostParams();
            http.setEntity(new UrlEncodedFormEntity(postParams, ENCODE));
        }
        else if (mParamsString != null) {
            http.setEntity(new StringEntity(mParamsString, ENCODE));
        }
        http = addHeader(http);

        return mDefaultHttpClient.execute(http);
    }

    /**
     * PUT通信を行う
     *
     * @return HttpResponse
     * @throws IOException
     */
    private HttpResponse doConnectPut() throws IOException {
        HttpPut http = new HttpPut(mUrl);

        if (mBinaryParamsList != null) {
            http.setEntity(createBinaryPostParams());
        }
        else if (mParamsMap != null || mArrayParamsMap != null) {
            ArrayList<NameValuePair> postParams = createPostParams();
            http.setEntity(new UrlEncodedFormEntity(postParams, ENCODE));
        }
        else if (mParamsString != null) {
            http.setEntity(new StringEntity(mParamsString, ENCODE));
        }
        http = addHeader(http);

        return mDefaultHttpClient.execute(http);
    }

    /**
     * DELETE通信を行う
     *
     * @return HttpResponse
     * @throws IOException
     */
    private HttpResponse doConnectDelete() throws IOException {
        HttpDeleteWithBody http = new HttpDeleteWithBody(mUrl);

        if (mParamsMap != null || mArrayParamsMap != null) {
            ArrayList<NameValuePair> postParams = createPostParams();
            http.setEntity(new UrlEncodedFormEntity(postParams, ENCODE));
        }
        else if (mParamsString != null) {
            http.setEntity(new StringEntity(mParamsString, ENCODE));
        }

        http = addHeader(http);

        return mDefaultHttpClient.execute(http);
    }

    /**
     * パラメータをつなげてGET用の文字列にする
     *
     * @return String paramsStr
     * @throws UnsupportedEncodingException
     */
    private String createGetParams() throws UnsupportedEncodingException {
        String paramsStr = "";

        if (mParamsMap != null && mParamsMap.size() != 0) {
            for (String key : mParamsMap.keySet()) {
                if (paramsStr.length() != 0) {
                    paramsStr += "&";
                }
                paramsStr += (key + "=" + URLEncoder.encode(mParamsMap.get(key), ENCODE));
            }
        }
        LogUtils.d("HttpHelper", "get params : " + paramsStr);

        return paramsStr;
    }

    /**
     * URLにスラッシュ区切りでパラメータを付与する
     *
     * @param url URL
     * @return String paramsStr
     * @throws UnsupportedEncodingException
     */
    private String createUrl(String url) throws UnsupportedEncodingException {
        if (mUrlParamsList != null && mUrlParamsList.size() != 0) {
            for (String param : mUrlParamsList) {
                url += ("/" + URLEncoder.encode(param, ENCODE));
            }
        }

        return url;
    }

    /**
     * パラメータをポスト用にセットする
     *
     * @return ArrayList<NameValuePair>
     */
    private ArrayList<NameValuePair> createPostParams() {
        ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();

        if (mParamsMap != null && mParamsMap.size() != 0) {
            for (String key : mParamsMap.keySet()) {
                LogUtils.d("HttpHelper", "post body : " + key + " - " + mParamsMap.get(key));
                postParams.add(new BasicNameValuePair(key, mParamsMap.get(key)));
            }
        }

        if (mArrayParamsMap != null && mArrayParamsMap.size() != 0) {
            for (String key : mArrayParamsMap.keySet()) {
                ArrayList<String> tmp = mArrayParamsMap.get(key);

                for (String value : tmp) {
                    LogUtils.d("HttpHelper", "post body : " + key + " - " + value);
                    postParams.add(new BasicNameValuePair(key, value));
                }
            }
        }

        return postParams;
    }

    /**
     * バイナリのパラメータをポスト用にセットする
     *
     * @return ArrayList<NameValuePair>
     */
    private HttpEntity createBinaryPostParams() throws FileNotFoundException {
        HttpEntity httpEntity = null;

        if (mBinaryParamsList != null) {
            MultipartEntityBuilder postParams = MultipartEntityBuilder.create();
            postParams.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            postParams.setCharset(Charset.forName(ENCODE));

            for (String key : mBinaryParamsList.keySet()) {
                LogUtils.d("HttpHelper", "post body : " + key + " - binary");

                File file = MediaUtils.getBinaryFile(mBinaryParamsList.get(key));
                String mimeType = MediaUtils.getMimeType(mBinaryParamsList.get(key));
                String fileName = MediaUtils.geFileName(mBinaryParamsList.get(key));
                postParams.addBinaryBody(key, file, ContentType.create(mimeType), fileName);
            }

            if (mParamsMap != null && mParamsMap.size() != 0) {
                for (String key : mParamsMap.keySet()) {
                    LogUtils.d("HttpHelper", "post body : " + key + " - " + mParamsMap.get(key));

                    if (mParamsMap.get(key) != null) {
                        postParams.addTextBody(key, mParamsMap.get(key), ContentType.create("plain/text", ENCODE));
                    }
                }
            }

            if (mArrayParamsMap != null && mArrayParamsMap.size() != 0) {
                for (String key : mArrayParamsMap.keySet()) {
                    ArrayList<String> tmp = mArrayParamsMap.get(key);

                    for (String value : tmp) {
                        postParams.addTextBody(key, value, ContentType.create("plain/text", ENCODE));
                        LogUtils.d("HttpHelper", "post body : " + key + " - " + value);
                    }
                }
            }

            httpEntity = postParams.build();
        }

        return httpEntity;
    }

    /**
     * GETにヘッダを付与する
     *
     * @param http HTTPGetクラス
     * @return HttpGet http
     */
    public HttpGet addHeader(HttpGet http) {
        if (mHeadersMap != null && mHeadersMap.size() != 0) {
            for (String key : mHeadersMap.keySet()) {
                LogUtils.d("HttpHelper", "header : " + key + " - " + mHeadersMap.get(key));
                http.addHeader(key, mHeadersMap.get(key));
            }
        }

        //cookieが指定してあればcookie付与
        if (mCookie != null) {
            http.addHeader(COOKIE, mCookie);
        }

        return http;
    }

    /**
     * POSTにヘッダを付与する
     *
     * @param http HttpPostクラス
     * @return HttpPost http
     */
    public HttpPost addHeader(HttpPost http) {
        if (mHeadersMap != null && mHeadersMap.size() != 0) {
            for (String key : mHeadersMap.keySet()) {
                LogUtils.d("HttpHelper", "header : " + key + " - " + mHeadersMap.get(key));
                http.addHeader(key, mHeadersMap.get(key));
            }
        }

        //cookieが指定してあればcookie付与
        if (mCookie != null) {
            http.addHeader(COOKIE, mCookie);
        }

        return http;
    }

    /**
     * PUTにヘッダを付与する
     *
     * @param http HttpPutクラス
     * @return HttpPut http
     */
    public HttpPut addHeader(HttpPut http) {
        if (mHeadersMap != null && mHeadersMap.size() != 0) {
            for (String key : mHeadersMap.keySet()) {
                LogUtils.d("HttpHelper", "header : " + key + " - " + mHeadersMap.get(key));
                http.addHeader(key, mHeadersMap.get(key));
            }
        }

        //cookieが指定してあればcookie付与
        if (mCookie != null) {
            http.addHeader(COOKIE, mCookie);
        }

        return http;
    }

    /**
     * DELETEにヘッダを付与する
     *
     * @param http HttpDeleteWithBodyクラス
     * @return HttpDeleteWithBody http
     */
    public HttpDeleteWithBody addHeader(HttpDeleteWithBody http) {
        if (mHeadersMap != null && mHeadersMap.size() != 0) {
            for (String key : mHeadersMap.keySet()) {
                LogUtils.d("HttpHelper", "header : " + key + " - " + mHeadersMap.get(key));
                http.addHeader(key, mHeadersMap.get(key));
            }
        }

        //cookieが指定してあればcookie付与
        if (mCookie != null) {
            http.addHeader(COOKIE, mCookie);
        }

        return http;
    }

    /**
     * ネットワーク接続確認
     *
     * @return Boolean
     */
    private Boolean checkNetworkStatus() {
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
     * @throws IOException
     * @throws IllegalStateException
     */
    private void analyzeResponse() throws IllegalStateException, IOException {
        // HTTPステータス取得解析
        mHttpStatus = mHttpResponse.getStatusLine().getStatusCode();
        LogUtils.d("HttpHelper", "http status : " + mHttpStatus);

        // ボディ取得
        if (mHttpResponse.getEntity() != null) {
            InputStream objStream = mHttpResponse.getEntity().getContent();
            InputStreamReader objReader = new InputStreamReader(objStream);
            BufferedReader objBuf = new BufferedReader(objReader);
            StringBuilder objJson = new StringBuilder();
            String sLine;

            while ((sLine = objBuf.readLine()) != null) {
                objJson.append(sLine);
            }

            mBody = objJson.toString();
            objStream.close();

            LogUtils.d("HttpHelper", "response body : " + mBody);
        }

        // ヘッダトレース
        if (mHttpResponse.getAllHeaders() != null) {
            Header[] headers = mHttpResponse.getAllHeaders();

            for(Header header : headers){
                LogUtils.d("HttpHelper", "response header : " + header);
            }
        }
    }

    /**
     * ベーシック認証
     */
    private void doBasicAuth() {
        if (mBasicAuthId == null || mBasicAuthPass == null) {
            return;
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
        mDefaultHttpClient.getCredentialsProvider().setCredentials(scope, credentials);
    }

    /**
     * 例外内容とその他デバッグ用の通信情報を作成
     *
     * @param e 例外
     */
    protected void saveException(Exception e) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put("url", mUrl);
        map.put("method", mMethod);
        map.put("status", String.valueOf(mStatus));
        map.put("http status", String.valueOf(mHttpStatus));

        if (mBasicAuthId != null || mBasicAuthPass != null) {
            map.put("basic auth", String.valueOf(mBasicAuthId) + " / " + String.valueOf(mBasicAuthPass));
        }

        if (mHeadersMap != null) {
            String text = "\n";

            for (String key : mHeadersMap.keySet()) {
                text += (key + " ： " + mHeadersMap.get(key) + "\n");
            }
            map.put("header params", text);
        }

        if (mParamsMap != null) {
            String text = "\n";

            for (String key : mParamsMap.keySet()) {
                text += (key + " ： " + mParamsMap.get(key) + "\n");
            }
            map.put("request params", text);
        }

        if (mArrayParamsMap != null) {
            String text = "\n";

            for (String key : mArrayParamsMap.keySet()) {
                ArrayList<String> tmp = mArrayParamsMap.get(key);

                if (tmp != null && tmp.size() != 0) {
                    for (String value : tmp) {
                        text += (key + " ： " + value + "\n");
                    }
                }
            }
            map.put("request array params", text);
        }

        if (mBinaryParamsList != null) {
            String text = "\n";

            for (String key : mBinaryParamsList.keySet()) {
                text += (key + " ： binary" + "\n");
            }
            map.put("request binary params", text);
        }

        if (mParamsString != null) {
            map.put("request params", mParamsString);
        }

        if (mCookie != null) {
            map.put("cookie", mCookie);
        }

        if (mBody != null) {
            map.put("response body", mBody);
        } else {
            map.put("response body", "null");
        }

        DebugHelper.save(mContext, e, map);
    }

    /**
     * 初期化を行う
     */
    private void init() {
        mContext = null;
        mUrl = null;
        mDefaultHttpClient = null;
        mHttpResponse = null;
        mBody = null;
        mStatus = 0;
        mHttpStatus = 0;

        mParamsMap = null;
        mArrayParamsMap = null;
        mUrlParamsList = null;
        mBinaryParamsList = null;
        mHeadersMap = null;
        mParamsString = null;
        mCookie =null;

        mBasicAuthId = null;
        mBasicAuthPass = null;
    }

    /**
     * 接続タイムアウトの時間をセット
     *
     * @param value 接続タイムアウトまでの時間
     */
    public void setConnectionTimeout(Integer value) {
        CONNECTION_TIMEOUT = value;
    }

    /**
     * ソケットイムアウトの時間をセット
     *
     * @param value 接続タイムアウトまでの時間
     */
    public void setSoTimeout(Integer value) {
        SO_TIMEOUT = value;
    }

    /**
     * 追加するcookieのパラメータを受け取る
     *
     * @param cookie クッキー
     */
    public void setCookie(String cookie) {
        mCookie = cookie;
    }

    /**
     * ヘッダのパラメータを受け取る
     *
     * @param headersMap ヘッダのパラメータ
     */
    public void setHeaders(HashMap<String, String> headersMap) {
        mHeadersMap = headersMap;
    }

    /**
     * URLにフラッシュ区切りで付与するパラメータ
     *
     * @param urlParamsList パラメータ
     */
    public void setUrlParams(ArrayList<String> urlParamsList) {
        mUrlParamsList = urlParamsList;
    }

    /**
     * name=valueの形のパラメータを受け取る
     *
     * @param paramsMap パラメータ
     */
    public void setParams(HashMap<String, String> paramsMap) {
        mParamsMap = paramsMap;
    }

    /**
     * name=valueの形の配列のパラメータを受け取る
     *
     * @param arrayParamsMap パラメータ
     */
    public void setArrayParams(HashMap<String, ArrayList<String>> arrayParamsMap) {
        mArrayParamsMap = arrayParamsMap;
    }

    /**
     * バリナリのパラメータを受け取る
     * ※post・putの場合のみ付与される
     *
     * @param binaryParamsList パラメータ
     */
    public void setBinaryParams(HashMap<String, String> binaryParamsList) {
        mBinaryParamsList = binaryParamsList;
    }

    /**
     * name=valueの形でない、valueだけのパラメータを受け取る
     *
     * @param paramsString パラメータ
     */
    public void setSingleStringParams(String paramsString) {
        mParamsString = paramsString;

        LogUtils.d("HttpHelper", "params string : " + paramsString);
    }

    /**
     * ベーシック認証のIDを受け取る
     *
     * @param basicAuthId ベーシック認証ID
     */
    public void setBasicAuthId(String basicAuthId) {
        mBasicAuthId = basicAuthId;

        LogUtils.d("HttpHelper", "basic auth id : " + basicAuthId);
    }

    /**
     * ベーシック認証のPASSを受け取る
     *
     * @param basicAuthPass ベーシック認証PASS
     */
    public void setBasicAuthPass(String basicAuthPass) {
        mBasicAuthPass = basicAuthPass;

        LogUtils.d("HttpHelper", "basic auth pass : " + basicAuthPass);
    }

    /**
     * ステータスを返す
     *
     * @return Integer mStatus テータス
     */
    public Integer getStatus() {
        //仮にステータスが入ってなかった場合、不明なエラーステータスを入れる
        if (mStatus == 0) {
            mStatus = STATUS_BLANK;
        }

        return mStatus;
    }

    /**
     * HTTPテータスを返す
     *
     * @return Integer mHttpStatus HTTPテータス
     */
    public Integer getHttpStatus() {
        return mHttpStatus;
    }

    /**
     * レスポンスボディを返す
     *
     * @return String mBody レスポンス
     */
    public String getBody() {
        return mBody;
    }

    /**
     * レスポンスヘッダを返す
     *
     * @return String header ヘッダ
     */
    public String getHeader(String name) {
        String header = null;

        Header[] headers = mHttpResponse.getHeaders(name);

        if (headers.length != 0) {
            header = headers[0].getValue();
        }

        return header;
    }

    /**
     * DELETEメソッドでbodyを付けられるように拡張
     * （通常のHttpDeleteはbodyを付けられない）
     */
    @NotThreadSafe
    class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public String getMethod() {
            return DELETE;
        }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpDeleteWithBody() {
            super();
        }
    }
}
