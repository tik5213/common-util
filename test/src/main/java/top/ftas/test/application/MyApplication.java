package top.ftas.test.application;

import android.app.Application;

import top.ftas.util.ToastUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-09-17 11:50
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.setApplicationContext(this);
    }
}
