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

import android.util.Log;

import the.one.net.TheNet;

/**
 * @author The one
 * @date 2019/3/30 0030
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class LogUtil {

    public static void showLog(String str) {
        if (TheNet.getBuilder().isDebug())
            Log.e("http", str);
    }

}
