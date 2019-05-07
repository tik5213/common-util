package top.ftas.util.size;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by tik on 17/8/11.
 * 获取屏幕宽度、以及状态栏高度
 */

public class CommonSizeUtil {
	// 屏幕宽度（像素）
	public static  int getWindowWidth(Context context){
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}


	public static int getStatusBarHeight(Context context){
		int statusBarHeight = -1;
		//获取status_bar_height资源的ID
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
		}
		return statusBarHeight;
	}

}
