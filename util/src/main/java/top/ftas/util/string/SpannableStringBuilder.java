package top.ftas.util.string;

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
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-04-22 18:36
 */
public class SpannableStringBuilder {
    public final String TAG = "SpannableStringBuilder";

    private Context mContext;
    SpannableString mSpannableString;
    CurrentSubStrProperty mCurrentSubStrProperty;

    private static class CurrentSubStrProperty{
        String originalStr;
        int maxRepeatCount;
        int currentRepeatSize = 0;
        int[] starts;
        int[] ends;
        int length;
        String subStr;

        public CurrentSubStrProperty(int maxRepeatCount) {
            this.maxRepeatCount = maxRepeatCount;
            this.starts = new int[this.maxRepeatCount];
            this.ends = new int[this.maxRepeatCount];
        }

        /**
         * 如果数组空间不够，则自动增长
         * @param newSize
         */
        private void arrayAutoIncrease(int newSize){
            this.maxRepeatCount = newSize;
            int[] newStarts = new int[this.maxRepeatCount];
            int[] newEnds = new int[this.maxRepeatCount];
            System.arraycopy(this.starts,0,newStarts,0,this.starts.length);
            System.arraycopy(this.ends,0,newEnds,0,this.ends.length);
            this.starts = newStarts;
            this.ends = newEnds;
//            printError("数组自动增加: newSize = " + newSize );
        }

        /**
         * 有新的子串加入，重置子串相关的各种属性
         * @param subStr
         */
        private void reset(String subStr){
            this.subStr = subStr;
            this.length = subStr.length();
            this.currentRepeatSize = 0;

            int start = 0,end;
            while (true){
                start = this.originalStr.indexOf(subStr,start);
                if (start >= 0){
                    end = start + length;
                    if (this.currentRepeatSize >= this.maxRepeatCount){
                        arrayAutoIncrease(this.maxRepeatCount + 10);
                    }
                    this.starts[this.currentRepeatSize] = start;
                    this.ends[this.currentRepeatSize] = end;
                    this.currentRepeatSize ++;
                    start = end;
                }else {
                    break;
                }
            }
        }
    }

    public static void printError(String message){
        Log.e("error",Log.getStackTraceString(new RuntimeException(message)));
    }

    private SpannableStringBuilder() {
    }

    public static SpannableStringBuilder builder(Context context, String originalStr){
        return builder(context,originalStr,3);
    }

    public static SpannableStringBuilder builder(Context context, String originalStr,int maxRepeatCount){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.mCurrentSubStrProperty = new CurrentSubStrProperty(maxRepeatCount);
        spannableStringBuilder.mCurrentSubStrProperty.originalStr = originalStr;
        spannableStringBuilder.mSpannableString = new SpannableString(originalStr);
        spannableStringBuilder.mContext = context.getApplicationContext();
        spannableStringBuilder.setSubStr(originalStr);
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
        for (int i = 0; i < mCurrentSubStrProperty.currentRepeatSize; i++) {
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
            mSpannableString.setSpan(foregroundColorSpan, mCurrentSubStrProperty.starts[i], mCurrentSubStrProperty.ends[i], Spannable.SPAN_EXCLUSIVE_INCLUSIVE);//颜色
        }
        return this;
    }

    /**
     * 设置字段加粗
     * @return
     */
    public SpannableStringBuilder setIsBold(){
        for (int i = 0; i < mCurrentSubStrProperty.currentRepeatSize; i++) {
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            mSpannableString.setSpan(styleSpan,mCurrentSubStrProperty.starts[i],mCurrentSubStrProperty.ends[i], Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return this;
    }

    /**
     * 设置字段大小
     * @param dp
     * @return
     */
    public SpannableStringBuilder setTextSize(int dp){
        for (int i = 0; i < mCurrentSubStrProperty.currentRepeatSize; i++) {
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(dp,true);
            mSpannableString.setSpan(absoluteSizeSpan,mCurrentSubStrProperty.starts[i],mCurrentSubStrProperty.ends[i],Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return this;
    }

    public interface OnClickSpanStringListener{
        void onClickSpanString(@NonNull String subString, @NonNull View widget);
    }

    private static class BuilderClickableSpan extends ClickableSpan{
        protected String mSubString;
        OnClickSpanStringListener mOnClickSpanStringListener;

        @Override
        public void onClick(@NonNull View widget) {
            if (mOnClickSpanStringListener != null){
                mOnClickSpanStringListener.onClickSpanString(mSubString == null ? "" : mSubString,widget);
            }
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            //去掉可点击区域的文字的下划线
            ds.setUnderlineText(false);
        }
    }

    /**
     * 设置 TextView 点击可跳转
     * @param textView
     * @return
     */
    public SpannableStringBuilder setCanMovement(TextView textView){
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    /**
     * 设置点击事件
     * @return
     *
     * //记得设置 TextView 允许跳转，不设置点击反应
     * tv.setMovementMethod(LinkMovementMethod.getInstance());
     */
    public SpannableStringBuilder setOnClickListener(OnClickSpanStringListener clickListener){
        for (int i = 0; i < mCurrentSubStrProperty.currentRepeatSize; i++) {
            BuilderClickableSpan builderClickableSpan = new BuilderClickableSpan();
            builderClickableSpan.mSubString = mCurrentSubStrProperty.subStr;
            builderClickableSpan.mOnClickSpanStringListener = clickListener;
            mSpannableString.setSpan(builderClickableSpan,mCurrentSubStrProperty.starts[i],mCurrentSubStrProperty.ends[i],Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return this;
    }


    /**
     * 设置删除线
     * @return
     */
    public SpannableStringBuilder setStrikethrough(){
        for (int i = 0; i < mCurrentSubStrProperty.currentRepeatSize; i++) {
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            mSpannableString.setSpan(strikethroughSpan,mCurrentSubStrProperty.starts[i],mCurrentSubStrProperty.ends[i],Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return this;
    }

    /**
     * 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍
     * @param proportion
     * @return
     */
    public SpannableStringBuilder setRelativeSize(@FloatRange(from = 0) float proportion){
        for (int i = 0; i < mCurrentSubStrProperty.currentRepeatSize; i++) {
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(proportion);
            mSpannableString.setSpan(relativeSizeSpan,mCurrentSubStrProperty.starts[i],mCurrentSubStrProperty.ends[i],Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return this;
    }

    public SpannableString build(){
        return mSpannableString;
    }

}
