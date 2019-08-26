package the.one.net;

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

import android.annotation.SuppressLint;
import android.content.Context;

import the.one.net.entity.Builder;

/**
 * @author The one
 * @date 2019/8/26 0026
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class TheNet {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Builder mBuilder;

    public static void init(Context context){
        mContext = context;
    }

    public static void init(Context context,Builder builder){
        mContext = context;
        mBuilder = builder;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Builder getBuilder() {
        if(null == mBuilder){
            mBuilder = new Builder();
        }
        return mBuilder;
    }
}
