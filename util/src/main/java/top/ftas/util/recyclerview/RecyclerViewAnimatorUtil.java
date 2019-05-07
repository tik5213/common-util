package top.ftas.util.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

/**
 * Created by tik on 17/12/12.
 * 开启或者关闭RecyclerView的默认动画效果
 */

public class RecyclerViewAnimatorUtil {

	/**
	 * 关闭默认局部刷新动画
	 */
	public static void closeDefaultAnimator(RecyclerView recyclerView) {
		recyclerView.getItemAnimator().setAddDuration(0);
		recyclerView.getItemAnimator().setChangeDuration(0);
		recyclerView.getItemAnimator().setMoveDuration(0);
		recyclerView.getItemAnimator().setRemoveDuration(0);
		((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
	}
}
