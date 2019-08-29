package top.ftas.util.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Dialog 设置帮助类
 *
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-29 16:38
 */
public class DialogSettingUtil {
    private static class BaseHolder {
        WindowManager.LayoutParams mLayoutParams;
        Dialog mDialog;
        Window mWindow;

        BaseHolder(Dialog dialog) {
            mDialog = dialog;
            mWindow = mDialog.getWindow();
        }

        @NonNull
        WindowManager.LayoutParams getLayoutParams() {
            if (mLayoutParams == null && mWindow != null) {
                mLayoutParams = mWindow.getAttributes();
            }
            if (mLayoutParams == null) {
                mLayoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            }
            return mLayoutParams;
        }

        /**
         * 获取 Dialog
         */
        public Dialog toDialog() {
            return mDialog;
        }

        void autoSetAttributes() {
            if (mWindow != null) {
                mWindow.setAttributes(getLayoutParams());
            }
            mLayoutParams = null;
        }
    }

    /**
     * 对于 onCreateDialog 方法
     */
    public static class OnCreateDialogHolder extends BaseHolder {

        private OnStartHolder mOnStartHolder;

        OnCreateDialogHolder(Dialog dialog) {
            super(dialog);
        }


        /**
         * 是否可点击空白处取消
         */
        public OnCreateDialogHolder setCanceledOnTouchOutside(boolean cancel) {
            mDialog.setCanceledOnTouchOutside(cancel);
            return this;
        }

        /**
         * 按返回键 dialog 是否可取消
         */
        public OnCreateDialogHolder setCancelable(boolean flag) {
            mDialog.setCancelable(flag);
            return this;
        }

        /**
         * Dialog 内容区域 背景支持圆角
         * 把 dialog 背景设置为透明，因为有些图片会设置为圆角，不设置的话，圆角图片就会显示为白色
         */
        public OnCreateDialogHolder setContentTransparent() {
            if (mWindow == null) return this;
            //把 dialog 背景设置为透明，因为有些图片会设置为圆角，不设置的话，圆角图片就会显示为白色
            mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            return this;
        }

        /**
         * 强制转换到 onStart 状态
         */
        public OnStartHolder forceOnStart() {
            if (mOnStartHolder == null) {
                mOnStartHolder = new OnStartHolder(mDialog);
            }
            return mOnStartHolder;
        }

        /**
         * 设置 Window 特性
         */
        public OnCreateDialogHolder setWindowFlags(int flags) {
            getLayoutParams().flags = flags;
            autoSetAttributes();
            return this;
        }

        /**
         * 设置 dialog 位置
         */
        public OnCreateDialogHolder setGravity(int gravity) {
            if (mWindow == null) return this;
            mWindow.setGravity(gravity);
            return this;
        }

        /**
         * 设置 dialog 进出动画
         *
         * <style name="animate_dialog">
         * <item name="android:windowEnterAnimation">@anim/dialog_enter</item>
         * <item name="android:windowExitAnimation">@anim/dialog_out</item>
         * </style>
         */
        public OnCreateDialogHolder setWindowAnimations(@StyleRes int resId) {
            if (mWindow == null) return this;
            mWindow.setWindowAnimations(resId);
            return this;
        }


        /**
         * 设置 dialog 底部居中展示
         */
        public OnCreateDialogHolder setBottomCenter() {
            if (mWindow == null) return this;
            mWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            return this;
        }

        /**
         * 设置 dialog 水平垂直居中展示
         */
        public OnCreateDialogHolder setCenterCenter() {
            if (mWindow == null) return this;
            mWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            return this;
        }

        /**
         * 支持点透
         */
        public OnCreateDialogHolder setNotFocusable() {
            getLayoutParams().flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            autoSetAttributes();
            return this;
        }
    }

    /**
     * 对应 onStart 方法
     */
    public static class OnStartHolder extends BaseHolder {
        private OnCreateDialogHolder mOnCreateDialogHolder;

        OnStartHolder(Dialog dialog) {
            super(dialog);
        }


        /**
         * Dialog 窗体透明（外框）
         */
        public OnStartHolder setWindowIsTransparent() {
            getLayoutParams().dimAmount = 0f;
            autoSetAttributes();
            return this;
        }


        /**
         * 设置 dimAmount
         */
        public OnStartHolder setDimAmount(float dimAmount) {
            getLayoutParams().dimAmount = dimAmount;
            autoSetAttributes();
            return this;
        }

        /**
         * 强制转换到 onCreateDialog 状态
         */
        public OnCreateDialogHolder forceOnCreateDialog() {
            if (mOnCreateDialogHolder == null) {
                mOnCreateDialogHolder = new OnCreateDialogHolder(mDialog);
            }
            return mOnCreateDialogHolder;
        }


    }


    public static OnCreateDialogHolder onCreateDialog(@NonNull Dialog dialog) {
        return new OnCreateDialogHolder(dialog);
    }

    public static OnStartHolder onStart(@NonNull Dialog dialog) {
        return new OnStartHolder(dialog);
    }
}
