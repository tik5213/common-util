package cn.dxy.util.size;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

import cn.dxy.util.softinput.SoftInputUtil;

/**
 * Created by tik on 17/8/11.
 */

public class ViewPreparedUtil {
	public static abstract class OnViewPreparedListener implements ViewTreeObserver.OnGlobalLayoutListener{
		private int layoutCount;
		private boolean requiredSoftInputHidden;
		private WeakReference<View> mViewWeakReference;

		public View getView(){
			return mViewWeakReference.get();
		}

		public void setView(View view){
			mViewWeakReference = new WeakReference<>(view);
		}

		public OnViewPreparedListener() {
		}

		public OnViewPreparedListener(boolean requiredSoftInputHidden) {
			this.requiredSoftInputHidden = requiredSoftInputHidden;
		}

		public int getLayoutCount() {
			return layoutCount;
		}

		public void setLayoutCount(int layoutCount) {
			this.layoutCount = layoutCount;
		}

		public boolean isRequiredSoftInputHidden() {
			return requiredSoftInputHidden;
		}

		public void setRequiredSoftInputHidden(boolean requiredSoftInputHidden) {
			this.requiredSoftInputHidden = requiredSoftInputHidden;
		}

		@Override
		public void onGlobalLayout() {
			View view = mViewWeakReference.get();
			if (view == null) return;
			layoutCount++;
			onPrepared(view,layoutCount);
		}

		public abstract void onFirstPrepared(View view);

		public void onPrepared(View view,int layoutCount){
			if (isRequiredSoftInputHidden()){
				Context context = view.getContext();
				if (SoftInputUtil.isShowSoftInput(context,view)){
					SoftInputUtil.hideDirectly(context);
					setLayoutCount(0);
					return;
				}else {
					removeSelf(view,layoutCount);
				}
			}else {
				removeSelf(view,layoutCount);
			}
			if (layoutCount == 1){
				onFirstPrepared(view);
			}
		}

		public void removeSelf(View view,int layoutCount){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}else {
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		}

	}

	public static void addViewPreparedListener(View view,OnViewPreparedListener onViewPreparedListener){
		onViewPreparedListener.setView(view);
		view.getViewTreeObserver().addOnGlobalLayoutListener(onViewPreparedListener);
	}

	/**
	 * 界面还没有展示时，就希望获取某些控件的宽高
	 * @param view
	 */
	public static void measureView(View view){
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(width,height);

	}
}
