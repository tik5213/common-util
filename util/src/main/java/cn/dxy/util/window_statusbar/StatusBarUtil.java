package cn.dxy.util.window_statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 统一设置状态栏颜色 或 透明
 * Created by larry on 2017/7/5.
 *
 */
public class StatusBarUtil {

    /**
     * 根据颜色Id 设置状态栏颜色
     * @param activity
     * @param resColor
     * @param isDarkText
     */
    public static void setStatusBarColor(Activity activity, int resColor, boolean isDarkText) {
        if(activity == null || resColor <= 0) {
            return;
        }

        int colorValue = ContextCompat.getColor(activity,resColor);
        setStatusBarColorWithColorValue(activity,colorValue,isDarkText);
    }

    /**
     * 根据颜色值 设置状态栏颜色
     * @param activity
     * @param colorValue
     * @param isDarkText
     */
    public static void setStatusBarColorWithColorValue(Activity activity, int colorValue, boolean isDarkText) {

        if(activity == null) {
            return;
        }

        if(FlymeUtils.isFlyme() || MIUIUtils.isMIUI()) {
            if(FlymeUtils.isFlyme()) {
                StatusBarUtil.FlymeSetStatusBarLightMode(activity.getWindow(), isDarkText);
            } else {
                StatusBarUtil.MIUISetStatusBarLightMode(activity.getWindow(), isDarkText);
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorValue);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View view = activity.findViewById(android.R.id.content);
            if(view != null) {
                view.setSystemUiVisibility(colorValue == Color.WHITE ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : 0);
            }
        }
    }



    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     *
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if(window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit   = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if(dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     *
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if(window != null) {
            Class clazz = window.getClass();
            try {
                int   darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field        = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }



    /**
     *
     * 检测 系统型号工具类
     *
     * Created by chenlinwei on 2017/7/10.
     */
    public static class MIUIUtils {
        private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

        public static boolean isMIUI() {
            //1.经测试 Android 8.0及以上不需要为 MIUI 单独设置状态栏颜色
            //2. Android8.0及以上，下面代码会报build.prop (Permission denied)错误
            //3. 参考网址 https://blog.csdn.net/zwww_/article/details/80340335
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                try {
                    final BuildProperties prop = BuildProperties.newInstance();
                    return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                            || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                            || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
                } catch (final IOException e) {
                    return false;
                }
            }
            return false;
        }
    }

    /**
     * TODO 此工具类年久未测，可能已废弃
     */
    public static class FlymeUtils {
        public static boolean isFlyme() {
            try {
                // Invoke Build.hasSmartBar()
                final Method method = Build.class.getMethod("hasSmartBar");
                return method != null;
            } catch (final Exception e) {
                return false;
            }
        }
    }







    public static class BuildProperties {

        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }

    }

}
