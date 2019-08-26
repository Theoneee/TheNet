package the.one.net.util;

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @author The one
 * @date 2019/8/26 0026
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class CookieUtil {

    private static final String COOKIE_PREF = "cookies_prefs";
    private static final String COOKIE_KEY = "cookie_key";
    private static SharedPreferences mSp;
    private static CookieUtil cookieUtil;

    public static CookieUtil getInstance(){
        if(null == cookieUtil){
            cookieUtil = new CookieUtil();
        }
        if(null == mSp){
            if(null == TheNetUtil.getContext()){
                new RuntimeException("TheNetUtil 需要初始化");
            }
            mSp = TheNetUtil.getContext().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        }
        return cookieUtil;
    }

    //保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
    public void saveCookie(String url,String cookies) {
        if(url.contains("login")){
            SharedPreferences.Editor editor = mSp.edit();
            if (!TextUtils.isEmpty(COOKIE_KEY)) {
                editor.putString(COOKIE_KEY, cookies);
            }
            editor.apply();
        }
    }

    public String getCookie() {
        if (mSp.contains(COOKIE_KEY) && !TextUtils.isEmpty(mSp.getString(COOKIE_KEY, ""))) {
            return mSp.getString(COOKIE_KEY, "");
        }
        return null;
    }
}
