package com.sjcq.app;

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

import android.app.Application;

import the.one.net.entity.Builder;
import the.one.net.TheNet;

/**
 * @author The one
 * @date 2019/8/26 0026
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TheNet.init(this,new Builder().setNeedCookie(true));
    }
}
