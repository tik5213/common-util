package cn.dxy.util.softinput;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.text.method.KeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import cn.dxy.util.touch.TouchEventUtil;

public class SoftInputUtil {

	public static void showSoftInput(Activity activity, EditText edit) {
		showSoftInput(activity, edit, true);
	}

	public static void showSoftInput(Activity activity, EditText edit, boolean toTail) {

		Object obj = edit.getTag();
		if (obj instanceof KeyListener) {
			edit.setKeyListener((KeyListener) obj);
		}
		if (toTail) {
			String txtString = edit.getText().toString();
			if (txtString != null) {
				edit.setSelection(txtString.length());
			}
		}

		edit.requestFocus();

		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edit, InputMethodManager.SHOW_FORCED);
	}


	public static void hideSoftInputIfNeed(Context context,View view,MotionEvent ev) {
		if (needHideInput(view, ev)) {
			hideDirectly(context);
//			hideSoftInput(context,view.getWindowToken());
		}
	}

	// 判定是否超出EditText范围，需要隐藏键盘
	public static boolean needHideInput(View v, MotionEvent ev) {
		return !TouchEventUtil.isTouchPointInView(v,(int)ev.getRawX(),(int)ev.getRawY());
		//只计算水平方向，忽略竖直方向
		/*
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (ev.getY() > top
					&& ev.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
		*/
		//原始算法
		/*
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (ev.getX() > left && ev.getX() < right && ev.getY() > top
					&& ev.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
		*/
	}


	public static void hideSoftInput(Activity activity) {
		try {
			InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			IBinder binder = activity.getCurrentFocus().getWindowToken();
			if (binder != null) {
				manager.hideSoftInputFromWindow(binder, 0);
			}
		} catch (Exception e) {
		}
	}
	public static void hideDirectly(Context context){
		if (context instanceof  Activity){
			hideSoftInput((Activity) context);
		}else {
			hideWithContext(context);
		}
	}

	public static void hideWithContext(Context context){
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 判断软键盘是否弹出，注意，此方法判断键盘是否弹出的同时，也把键盘隐藏了。
	 */
	public static boolean isShowSoftInput(Context context,View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //软键盘已弹出
//软键盘未弹出
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
