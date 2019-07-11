package top.ftas.util.layout;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-09-05 12:32
 */
public class LayoutUtil {
    public static View init(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent,int resId) {
        View view = inflater.inflate(resId,parent,false);
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
}
