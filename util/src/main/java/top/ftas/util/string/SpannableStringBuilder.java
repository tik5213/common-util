package top.ftas.util.string;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.View;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-04-22 18:36
 */
public class SpannableStringBuilder {
    public final String TAG = "SpannableStringBuilder";

    String mOriginalStr;
    SpannableString mSpannableString;
    CurrentSubStrProperty mCurrentSubStrProperty = new CurrentSubStrProperty();
    private Context mContext;

    private class CurrentSubStrProperty{
        int start;
        int end;
        int length;
        String subStr;

        private void reset(String subStr){
            this.subStr = subStr;
            this.length = subStr.length();
            this.start = mOriginalStr.indexOf(subStr);
            this.end = start + this.length;
        }
    }

    private SpannableStringBuilder() {
    }

    public static SpannableStringBuilder builder(Context context, String originalStr){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.mOriginalStr = originalStr;
        spannableStringBuilder.mSpannableString = new SpannableString(originalStr);
        spannableStringBuilder.mContext = context instanceof Activity ? context.getApplicationContext() : context;
        return spannableStringBuilder;
    }

    public SpannableStringBuilder setSubStr(String subStr){
        mCurrentSubStrProperty.reset(subStr);
        return this;
    }

    /**
     * 设置高亮颜色
     * @param color
     * @return
     */
    public SpannableStringBuilder setColorRes(@ColorRes int color){
        return setColorInt(ContextCompat.getColor(mContext,color));
    }

    /**
     * 设置高亮颜色
     * @param color
     * @return
     */
    public SpannableStringBuilder setColorInt(@ColorInt int color){
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        mSpannableString.setSpan(foregroundColorSpan, mCurrentSubStrProperty.start, mCurrentSubStrProperty.end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        return this;
    }

    /**
     * 设置字段加粗
     * @return
     */
    public SpannableStringBuilder setIsBold(){
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        mSpannableString.setSpan(styleSpan,mCurrentSubStrProperty.start,mCurrentSubStrProperty.end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return this;
    }

    /**
     * 设置字段大小
     * @param dp
     * @return
     */
    public SpannableStringBuilder setTextSize(int dp){
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(dp,true);
        mSpannableString.setSpan(absoluteSizeSpan,mCurrentSubStrProperty.start,mCurrentSubStrProperty.end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return this;
    }

    /**
     * 设置点击事件
     * @param onClickListener
     * @return
     */
    public SpannableStringBuilder setOnClickListener(final View.OnClickListener onClickListener){
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                if (onClickListener != null) {
                    onClickListener.onClick(widget);
                }
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                //去掉可点击区域的文字的下划线
                ds.setUnderlineText(false);
            }
        };

        mSpannableString.setSpan(clickableSpan,mCurrentSubStrProperty.start,mCurrentSubStrProperty.end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return this;
    }


    /**
     * 设置删除线
     * @return
     */
    public SpannableStringBuilder setStrikethrough(){
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        mSpannableString.setSpan(strikethroughSpan,mCurrentSubStrProperty.start,mCurrentSubStrProperty.end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return this;
    }

    /**
     * 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍
     * @param proportion
     * @return
     */
    public SpannableStringBuilder setRelativeSize(@FloatRange(from = 0) float proportion){
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(proportion);
        mSpannableString.setSpan(relativeSizeSpan,mCurrentSubStrProperty.start,mCurrentSubStrProperty.end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return this;
    }

    public SpannableString build(){
        return mSpannableString;
    }

}
