package the.one.net;

import android.text.TextUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import the.one.net.callback.Callback;
import the.one.net.callback.ListCallback;
import the.one.net.entity.PageInfoBean;
import the.one.net.util.FailUtil;
import the.one.net.util.LogUtil;

/**
 * 网络请求基类
 */
public abstract class BaseHttpRequest extends OkHttpHttpRequestCore {

    private static final String TAG = "BaseHttpRequest";
    private List<Call> mCalls = new ArrayList<>();
    static private Gson mGson;

    private Gson getGson() {
        if (mGson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    for (String exclude : mBuilder.getExcludes()) {
                        if (exclude.equals(f.getName())) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
            mGson = builder.create();
        }
        return mGson;
    }

    /**
     * 取消所有的网络请求
     */
    public void cancelAllRequest() {
        if (mCalls == null) {
            return;
        }
        for (int i = 0; i < mCalls.size(); i++) {
            mCalls.get(i).cancel();
        }
        mCalls.clear();
    }

    @Override
    public okhttp3.Callback getCallback(Call call, Callback callback) {
        mCalls.add(call);
        return new DefaultCallback(callback);
    }

    /**
     * 重写默认的回调
     */
    private class DefaultCallback implements okhttp3.Callback {
        private Callback callback;

        public DefaultCallback(Callback callBack) {
            this.callback = callBack;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mCalls.remove(call);
            if (e != null && e.getMessage() != null && e.getMessage().equalsIgnoreCase("Canceled")) {
                return;
            }
            if (this.callback != null) {
                sendFailure(callback, 0, FailUtil.getFailString(e));
            }
        }

        @Override
        public void onResponse(Call call, Response response) {
            mCalls.remove(call);
            if (callback == null) {
                return;
            }
            Type type = callback.getType();
            int code = response.code();
            String body = null;
            String result = null;
            String msg = "";
            PageInfoBean pageInfoBean = null;
            try {
                body = response.body().string();
                LogUtil.showLog("返回的数据-----" + code + "====" + body);
                if (code == 200) {
//                    LogUtil.showLog("返回的数据-----   0" );
                    JSONObject object;
                    try {
                        object = new JSONObject(body);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (type == String.class) {
                            sendSuccess(callback, body, msg, pageInfoBean);
                        } else {
                            sendFailure(callback, code, "解析JSONObject错误");
                        }
                        return;
                    }
//                    LogUtil.showLog("返回的数据-----   1" );
                    if (object.has(mBuilder.getMsg()))
                        msg = object.getString(mBuilder.getMsg());
//                    LogUtil.showLog("返回的数据-----   2" );
                    int status = -1;
                    if (object.has(mBuilder.getCode())) {
                        status = object.getInt(mBuilder.getCode());
//                        LogUtil.showLog("返回的数据-----   3" +status );
                    } else {
//                        LogUtil.showLog("返回的数据-----   4" );
                        if (type == String.class) {
                            sendSuccess(callback, body, msg, pageInfoBean);
                            return;
                        }
                    }
//                    LogUtil.showLog("返回的数据-----   5" +status );
                    if (status == mBuilder.getSuccessCode()) {
                        if (type == String.class) {
                            sendSuccess(callback, body, msg, pageInfoBean);
                            return;
                        }
                        result = object.getString(mBuilder.getData());
                        if (object.has(mBuilder.getPage())) {
                            String pageInfo = object.getString(mBuilder.getPage());
                            if (null != pageInfo && !pageInfo.isEmpty()) {
                                pageInfoBean = getGson().fromJson(pageInfo, new TypeToken<PageInfoBean>() {
                                }.getType());
                            }
                        }
                        Object t = getGson().fromJson(result, type);
                        if (callback instanceof ListCallback) {
                            ListCallback listCallback = (ListCallback) callback;
                            sendSuccess(listCallback, t, msg, pageInfoBean);
                        } else {
                            sendSuccess(callback, t, msg, pageInfoBean);
                        }
                    } else if (status == 511) {
                        sendFinish(callback);
                    } else if (status == 1) {
                        sendFailure(callback, status, msg);
                    } else {
                        sendFailure(callback, status, !TextUtils.isEmpty(msg) ? msg : "服务器内部错误");
                    }
                } else {
                    try {
                        JSONObject object = new JSONObject(body);
                        msg = object.getString(mBuilder.getMsg());
                    } catch (Exception e) {

                    }
                    sendFailure(callback, code, !TextUtils.isEmpty(msg) ? msg : "服务器内部错误");
                }
            } catch (IOException e) {
                e.printStackTrace();
                sendFailure(callback, 0, "网络错误");
            } catch (JSONException e) {
                e.printStackTrace();
                sendFailure(callback, 0, "数据解析错误");
            } catch (Exception e) {
                e.printStackTrace();
                sendFailure(callback, 0, "数据解析错误");
            }
        }
    }

    private void sendSuccess(final Callback callback, final Object t, final String msg, final PageInfoBean pageInfoBean) {
        getHander().post(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.onSuccess(t, msg, pageInfoBean);
                    LogUtil.showLog("返回的数据-----成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.showLog("返回的数据-----失败");
                    callback.onFailure(0, "返回失败");
                }
            }
        });
        sendFinish(callback);
    }

    private void sendFailure(final Callback callback, final int httpCode, final String errorText) {
        getHander().post(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.onFailure(httpCode, errorText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        sendFinish(callback);
    }

    private void sendFinish(final Callback callback) {
        getHander().post(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.onFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 以json方式进行提交
     *
     * @param url
     * @param params
     * @param callback
     */
    protected void postJson(String url, Map<String, Object> params, Callback callback) {
        if (callback != null && callback instanceof ListCallback) {
            if (params == null) {
                params = new HashMap<>();
            }
        }
        String json = params == null ? "{}" : getGson().toJson(params, Map.class);
        LogUtil.showLog(url);
        LogUtil.showLog(json);
        post(url, params, callback);
    }

    /**
     * 以json方式进行提交
     *
     * @param url
     * @param callback
     */
    protected void postJson(String url, Callback callback) {
        postJson(url, null, callback);
    }

    @Override
    protected Call doPost(String url, RequestBody body, Callback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return doExecute(request, callback);
    }

    /**
     * 以json的方式
     *
     * @param url
     * @param callback
     */
    protected void getJson(String url, Callback callback) {
        getJson(url, null, callback);
    }


    /**
     * 以json的方式提交
     *
     * @param url
     * @param callback
     */
    protected void getJson(String url, Map<String, Object> params, Callback callback) {
        if (callback != null && callback instanceof ListCallback) {
            if (params == null) {
                params = new HashMap<>();
            }
        }
        if (params != null && params.size() != 0) {
            url += "?";
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                url += entry.getKey() + "=" + entry.getValue() + "&";
            }
            url = url.substring(0, url.length() - 1);
        }
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        doExecute(request, callback);
    }

    @Override
    protected void get(String url, Callback callback) {
        if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
            sendFailure(callback, 0, "请求错误");
        } else
            super.get(url, callback);
    }

    @Override
    protected void get(String url, Map<String, Object> params, Callback data) {
        super.get(url, params, data);
    }

    @Override
    protected void post(String url, String mediaType, String text, Callback callback) {
        super.post(url, mediaType, text, callback);
    }

    @Override
    protected void post(String url, Map<String, Object> params, Callback callback) {
        super.post(url, params, callback);
    }

}
