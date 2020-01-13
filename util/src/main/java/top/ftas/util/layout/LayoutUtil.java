package top.ftas.util.layout;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import top.ftas.util.size.DisplayUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-09-05 12:32
 */
public class LayoutUtil {
    public static View init(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int resId) {
        View view = inflater.inflate(resId, parent, false);
        return init(view);
    }

    public static View init(View view) {
        if (view == null) return null;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        view.setLayoutParams(layoutParams);
        return view;
    }

    /**
     * 根据屏幕宽 等比计算出目标图片的高
     */
    public static void setImageViewLayoutVisible(View view, int width, int height) {
        setImageViewLayoutVisible(view, width, height, 0);
    }

    /**
     * 根据屏幕宽（如果目标 ImageView 并不是充满宽度的，可以传递一个差值过来） 等比计算出目标图片的高
     */
    public static void setImageViewLayoutVisible(View view, int width, int height, int dp_reduceWidth) {
        if (view == null) return;
        int reduceWidth = 0;
        if (dp_reduceWidth != 0) {
            reduceWidth = DisplayUtil.dip2px(view.getContext(), dp_reduceWidth);
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        layoutParams.height = (DisplayUtil.getScreenWidth(view.getContext()) - reduceWidth) * height / width;
        view.setLayoutParams(layoutParams);
        view.setVisibility(View.VISIBLE);
    }
}
