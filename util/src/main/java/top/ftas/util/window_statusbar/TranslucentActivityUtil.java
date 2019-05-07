package top.ftas.util.window_statusbar;

import android.app.Activity;

import top.ftas.util.R;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-08-27 23:28
 * 设置透明的Activity
 * 需要Activity默认设置的 theme 上添加了 windowIsTranslucent 属性为 true 才有效
 */
public class TranslucentActivityUtil {

    /**
     * 直接将 Activity 的 theme 设置为 Support.Translucent 即可实现透明Activity
     * @param activity
     */
    public static void setActivityTranslucent(Activity activity){

        activity.setTheme(R.style.Support_Translucent);
    }
}
