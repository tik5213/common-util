package top.ftas.util.window_statusbar;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class WindowUtil {

    /**
     * Activity 全屏
     * 设置黑色状态栏文案的沉浸式状态栏
     * @param activity
     *
     *
     * android7.0 沉浸式状态栏蒙灰问题
     * https://blog.csdn.net/zi_zhe/article/details/76557249
     */
    public static void beforeSetContentViewSetToDark(Activity activity){
        try {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //去除半透明状态栏
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); //全屏显示
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decorView = window.getDecorView();
                //黑色样式
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }



            //隐藏 ActionBar
            if (activity instanceof AppCompatActivity) {
                ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.hide();
                }
            } else {
                android.app.ActionBar actionBar = activity.getActionBar();
                if (actionBar != null) {
                    actionBar.hide();
                }
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    public static void setActivityToFullScreen(Activity activity) {
        setActivityToFullScreen(activity, false, false);
    }

    /**
     * 全屏状态栏，并且完全隐藏StatusBar
     *
     * @param activity
     */
    public static void setActivityToFullScreenWithInvisibleStatusBar(Activity activity) {
        setActivityToFullScreen(activity, true,true);
    }

    public static void showActivityStatusBar(Activity activity) {
        if (activity == null) return;
        Window window = activity.getWindow();
        if (window == null) return;
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        ViewGroup contentLayout = activity.findViewById(android.R.id.content);
        if (contentLayout == null || contentLayout.getChildCount() <= 0) return;
            contentLayout.getChildAt(0).setFitsSystemWindows(true);
    }

    public static void showActionBar(Activity activity) {
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        } else {
            android.app.ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    }

    /**
     * 将 Activity 设置为全屏展示
     *
     * @param activity
     */
    public static void setActivityToFullScreen(Activity activity, boolean hideActionBar, boolean statusBarIsInvisible) {
        if (activity == null) {
            return;
        }
        if (hideActionBar) {
            if (activity instanceof AppCompatActivity) {
                ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.hide();
                }
            } else {
                android.app.ActionBar actionBar = activity.getActionBar();
                if (actionBar != null) {
                    actionBar.hide();
                }
            }
        }
        Window window = activity.getWindow();
        if (window == null) return;
        if (statusBarIsInvisible) {
            window.getDecorView().setSystemUiVisibility(View.INVISIBLE);
        } else {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        ViewGroup contentLayout = activity.findViewById(android.R.id.content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        if (contentLayout == null || contentLayout.getChildCount() <= 0) return;
        contentLayout.getChildAt(0).setFitsSystemWindows(false);
    }

    /**
     * 将 DialogFragment 设置为透明背景
     *
     * @param dialogFragment
     */
    public static void setSupportDialogFragmentToTransparent(android.support.v4.app.DialogFragment dialogFragment) {
        Dialog dialog = dialogFragment.getDialog();
        if (dialog == null) return;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        window.setAttributes(windowParams);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
