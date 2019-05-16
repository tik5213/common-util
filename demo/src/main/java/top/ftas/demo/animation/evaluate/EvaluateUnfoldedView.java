package top.ftas.demo.animation.evaluate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.ftas.demo.R;


/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-05-15 15:47
 */
public class EvaluateUnfoldedView extends FrameLayout {
    //评价条 - 未折叠
    View evaluate_unfolded_layout;
    //评价条 - 已折叠
    View evaluate_folded_layout;

    //评价条 - 动态长度
    View evaluate_dynamic_layout;

    ImageView btn_close_layout;

    //禁止点击
    View v_not_click_layout;

    OnClickListener mOnClickListener;
    private int ANIMATION_DURATION = 300;

    ValueAnimator mValueAnimator;


    int mOriginalFullWidth;
    int mFoldedLayoutWidth;

    public void setAnimationDuration(int animationDuration) {
        ANIMATION_DURATION = animationDuration;
    }

    public EvaluateUnfoldedView(@NonNull Context context) {
        this(context, null);
    }

    public EvaluateUnfoldedView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EvaluateUnfoldedView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_evaluate_unfolded_layout, this);

        bindViewById();
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private void setWidthValue(ValueAnimator animation){
        //获取当前宽度
        int width = (int) animation.getAnimatedValue();
        ViewGroup.LayoutParams layoutParams = evaluate_dynamic_layout.getLayoutParams();
        layoutParams.width = width;
        evaluate_dynamic_layout.setLayoutParams(layoutParams);
        evaluate_dynamic_layout.requestLayout();
    }

    private void resetValueAnimator(){
        if (mValueAnimator != null){
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    private void bindViewById() {
        evaluate_unfolded_layout = findViewById(R.id.evaluate_unfolded_layout);
        evaluate_folded_layout = findViewById(R.id.evaluate_folded_layout);
        evaluate_dynamic_layout = findViewById(R.id.evaluate_dynamic_layout);
        btn_close_layout = findViewById(R.id.btn_close_layout);
        v_not_click_layout = findViewById(R.id.v_not_click_layout);

        btn_close_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*重置动画*/
                resetValueAnimator();

                /* 属性动画 */
                ValueAnimator valueAnimator = ValueAnimator.ofInt(mOriginalFullWidth,mFoldedLayoutWidth);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setWidthValue(animation);
                    }
                });
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        btn_close_layout.setVisibility(GONE);
                    }
                });

                valueAnimator.setDuration(ANIMATION_DURATION);
                mValueAnimator = valueAnimator;

                /* Alpha 动画 */
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
                alphaAnimation.setDuration(ANIMATION_DURATION);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        evaluate_folded_layout.setVisibility(VISIBLE);
                        v_not_click_layout.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        evaluate_unfolded_layout.setVisibility(GONE);
                        v_not_click_layout.setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                /* Alpha 动画 */
                AlphaAnimation foldedAlphaAnimation = new AlphaAnimation(0f, 1.0f);
                foldedAlphaAnimation.setDuration(ANIMATION_DURATION);

                /* 启动 */
                evaluate_folded_layout.startAnimation(foldedAlphaAnimation);
                evaluate_unfolded_layout.startAnimation(alphaAnimation);
                valueAnimator.start();
            }
        });

        evaluate_folded_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*重置动画*/
                resetValueAnimator();

                /* 属性动画 */
                ValueAnimator valueAnimator = ValueAnimator.ofInt(mFoldedLayoutWidth,mOriginalFullWidth);

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        setWidthValue(animation);
                    }
                });

                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        btn_close_layout.setVisibility(View.VISIBLE);
                    }
                });

                valueAnimator.setDuration(ANIMATION_DURATION);
                mValueAnimator = valueAnimator;

                /* Alpha 动画 */
                AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
                alphaAnimation.setDuration(ANIMATION_DURATION);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        evaluate_unfolded_layout.setVisibility(VISIBLE);
                        v_not_click_layout.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        evaluate_folded_layout.setVisibility(INVISIBLE);
                        v_not_click_layout.setVisibility(GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                /* Alpha 动画 */
                AlphaAnimation foldedAlphaAnimation = new AlphaAnimation(1.0f, 0f);
                foldedAlphaAnimation.setDuration(ANIMATION_DURATION);

                /* 启动 */
                evaluate_folded_layout.startAnimation(foldedAlphaAnimation);
                evaluate_unfolded_layout.startAnimation(alphaAnimation);
                valueAnimator.start();

            }
        });

        evaluate_unfolded_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            }
        });
    }

/*    private ScaleAnimation createScaleAnimation(float fromX, float toX) {
        //缩放动画，以左侧从原始缩小到0.1倍
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, 1.0f, 1.0f,
                ScaleAnimation.RELATIVE_TO_SELF, 0f,
                ScaleAnimation.RELATIVE_TO_SELF, 0f);
        return scaleAnimation;

    }*/

/*    private AnimationSet createAnimationSet() {
        AnimationSet animationSet = new AnimationSet(true);
        //动画时长
        animationSet.setDuration(ANIMATION_DURATION);
        return animationSet;

    }*/

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mOriginalFullWidth = w;
        mFoldedLayoutWidth = evaluate_folded_layout.getMeasuredWidth();
    }

    @Override
    protected void onDetachedFromWindow() {
        resetValueAnimator();
        super.onDetachedFromWindow();
    }
}
