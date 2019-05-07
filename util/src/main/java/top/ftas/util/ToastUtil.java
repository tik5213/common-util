package top.ftas.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 提示工具类
 */
public class ToastUtil {
    private static Context sApplicationContext = null;

    public static void setApplicationContext(Context applicationContext) {
        if (applicationContext instanceof Activity) {
            sApplicationContext = applicationContext.getApplicationContext();
        } else {
            sApplicationContext = applicationContext;
        }
    }

    public static void toast(int... args) {
        if (sApplicationContext == null || args == null || args.length == 0) {
            return;
        }
        String msg;
        if (args.length == 1) {
            msg = sApplicationContext.getString(args[0]);
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (int arg : args) {
                stringBuilder.append(sApplicationContext.getString(arg));
            }
            msg = stringBuilder.toString();
        }
        if (TextUtils.isEmpty(msg)) return;
        if (MainThreadUtil.isMainThread()) {
            Toast.makeText(sApplicationContext, msg, Toast.LENGTH_SHORT).show();
        } else {
            final String finalMsg = msg;
            MainThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(sApplicationContext, finalMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void toast(String... args) {
        if (sApplicationContext == null || args == null || args.length == 0) {
            return;
        }

        String msg;
        if (args.length == 1) {
            if (args[0] != null) {
                msg = args[0];
            } else {
                return;
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String arg : args) {
                if (arg != null) {
                    stringBuilder.append(arg);
                }
            }
            msg = stringBuilder.toString();
        }
        if (TextUtils.isEmpty(msg)) return;
        if (MainThreadUtil.isMainThread()) {
            Toast.makeText(sApplicationContext, msg, Toast.LENGTH_SHORT).show();
        } else {
            final String finalMsg = msg;
            MainThreadUtil.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(sApplicationContext, finalMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
