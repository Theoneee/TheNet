package the.one.net;

import android.os.Handler;
import android.os.Looper;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import the.one.net.callback.Callback;
import the.one.net.entity.Builder;
import the.one.net.util.AddCookiesInterceptor;
import the.one.net.util.LogUtil;
import the.one.net.util.SaveCookiesInterceptor;

/**
 * 网络请求类
 * okhttp的相关介绍 https://github.com/square/okhttp/wiki/Recipes
 */
public abstract class OkHttpHttpRequestCore extends HttpRequest {

    public static final String MEDIA_TYPE_JSON = "application/json; charset=utf-8";
    protected static OkHttpClient mOkHttpClient;
    private Handler mHander = null;
    static Builder mBuilder = null;

    public OkHttpHttpRequestCore() {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            mBuilder = TheNet.getBuilder();
            if (mBuilder.isNeedCookie()) {
                builder.addInterceptor(new AddCookiesInterceptor());
                builder.addInterceptor(new SaveCookiesInterceptor());
            }
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);
            mOkHttpClient = builder.build();
        }
        mHander = new Handler(Looper.getMainLooper());
    }

    public Handler getHander() {
        return mHander;
    }

    @Override
    protected void post(String url, Map<String, Object> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        String testUrl = url;
        if (params != null && params.size() != 0) {
            testUrl += "?";
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    builder.add(entry.getKey(), entry.getValue().toString());
                }
                testUrl += entry.getKey() + "=" + entry.getValue() + "&";
            }
            testUrl = testUrl.substring(0, testUrl.length() - 1);
        }
        LogUtil.showLog("请求的地址  " + testUrl);
        doPost(url, builder.build(), callback);
    }

    @Override
    protected void post(String url, String mediaType, String text, Callback callback) {
        RequestBody body = RequestBody.create(MediaType.parse(mediaType), text);
        doPost(url, body, callback);
    }

    @Override
    protected void get(String url, Callback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        doExecute(request, callback);
    }

    @Override
    protected void get(String url, Map<String, Object> params, Callback data) {
        if (params != null && params.size() != 0) {
            url += "?";
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
            url = url.substring(0, url.length() - 1);
        }
        get(url, data);
    }


    protected Call doPost(String url, RequestBody body, Callback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return doExecute(request, callback);
    }

    protected Call doExecute(Request request, Callback callback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(getCallback(call, callback));
        return call;
    }

    /**
     * 添加回调
     *
     * @param call
     * @return
     */
    public abstract okhttp3.Callback getCallback(Call call, Callback callback);


}
