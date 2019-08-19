package top.ftas.util.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * 将 Dialog 设置为透明，以便可以为 Dialog 背景设置圆角 shape
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-19 12:11
 */
public class DialogTransparentUtil {
    public static Dialog onCreateDialog(Dialog dialog) {
        return onCreateDialog(dialog, null);
    }

    public static Dialog onCreateDialog(Dialog dialog, @Nullable Boolean canceledOnTouchOutside) {
        if (canceledOnTouchOutside != null) {
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        }

        Window window = dialog.getWindow();
        if (window != null) {
            //把 dialog 背景设置为透明，因为有些图片会设置为圆角，不设置的话，圆角图片就会显示为白色
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams lp = window.getAttributes();
            window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            window.setAttributes(lp);

        }
        return dialog;
    }
}
