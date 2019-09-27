package top.ftas.util;


import android.os.Handler;
import android.os.Looper;

/**
 * Created by tik on 2018/1/24.
 */

public class MainThreadUtil {
    private static Handler sMainHandler = null;

    public static Handler getMainHandler() {
        if (sMainHandler == null) {
            synchronized (MainThreadUtil.class) {
                if (sMainHandler == null) {
                    sMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sMainHandler;
    }

    /**
     * 如果想要清除 Handler ，或者共享某个 Handler，可以使用此方法
     */
    public static void setMainHandler(Handler mainHandler) {
        sMainHandler = mainHandler;
    }

    public static void post(Runnable runnable) {
        getMainHandler().post(runnable);
    }

    public static void postDelayed(Runnable runnable, int delayMillis) {
        getMainHandler().postDelayed(runnable, delayMillis);
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
