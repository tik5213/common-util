package cn.dxy.util.touch;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by tik on 17/9/13.
 * 判断一个坐标是否在指定的View范围内(一般是处理touch事件时使用)
 */

public class TouchEventUtil {
	public static int h_reviseSpace = 80;
	public static int v_reviseSpace = 80;
	public static boolean isTouchPointInView(View view, int x, int y) {
		if (view == null) {
			return false;
		}
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int left = location[0] - h_reviseSpace;
		int top = location[1] - v_reviseSpace;
		int right = left + view.getMeasuredWidth() + h_reviseSpace;
		int bottom = top + view.getMeasuredHeight() + v_reviseSpace;
		//view.isClickable() &&
        return y >= top && y <= bottom && x >= left
                && x <= right;
    }

	public static View getTouchTarget(View view, int x, int y) {
		View targetView = null;
		// 判断view是否可以聚焦
		ArrayList<View> TouchableViews = view.getTouchables();
		for (View child : TouchableViews) {
			if (isTouchPointInView(child, x, y)) {
				targetView = child;
				break;
			}
		}
		return targetView;
	}
}
