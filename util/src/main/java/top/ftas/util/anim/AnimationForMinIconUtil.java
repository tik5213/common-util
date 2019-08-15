package top.ftas.util.anim;

import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * 小图标常用动画
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-07-24 12:00
 */
public class AnimationForMinIconUtil {


    /**
     * 箭头旋转动画
     * @param arrow 箭头 ImageView
     * @param toUpState 是否切换到展开状态（箭头向上的状态） true - 切换到展开状态，箭头向上 false - 切换到收起状态，箭头向下
     */
    public static void rotateArrow(ImageView arrow, boolean toUpState) {
        rotateArrow(arrow,toUpState,100);
    }

    /**
     * 箭头旋转动画
     * @param arrow 箭头 ImageView
     * @param toUpState 是否切换到展开状态（箭头向上的状态） true - 切换到展开状态，箭头向上 false - 切换到收起状态，箭头向下
     * @param durationMillis 动画持续的时间
     */
    public static void rotateArrow(ImageView arrow, boolean toUpState,long durationMillis) {
        float pivotX;
        float pivotY;
        float fromDegrees;
        float toDegrees;

        pivotX = arrow.getWidth() / 2f;
        pivotY = arrow.getHeight() / 2f;
        if (toUpState) {
            fromDegrees = 0f;
            toDegrees = 180f;
        } else {
            fromDegrees = 180f;
            toDegrees = 0f;
        }

        //旋转动画话晒 参数值 旋转的开始角度 旋转的结束角度 pivotX x轴伸缩值
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
        //该方法用于设置动画的持续时间，以毫秒为单位
        animation.setDuration(durationMillis);
        //设置重复次数
//        animation.setRepeatCount(int repeatCount);
        //动画终止时仪在最后一帧
        animation.setFillAfter(true);
        //启动动画
        arrow.startAnimation(animation);
    }
}
