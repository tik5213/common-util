package top.ftas.util.string;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
    private SpannableString mSpannableString;
    private CurrentSubStrProperty mCurrentSubStrProperty;
    //是否设置过点击事件
    private boolean mHasSetOnClickListener;
    //是否已经设置过 CanMovement
    private boolean mHasSetMovementMethod;
    private Manual mManual;

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
     * {@link #into}
     * @deprecated
     */
    public SpannableStringBuilder setCanMovement(TextView textView){
        setMovementMethod(textView);
        return this;
    }

    /**
     * 设置 TextView 点击可跳转 ，以及可点击文字的样式
     */
    private void setMovementMethod(TextView textView){
        if (mHasSetMovementMethod){
            return;
        }
        mHasSetMovementMethod = true;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        //点击之后，可点击文字被激活了，所以使用了系统默认的 highlight 颜色。
        //textView.invalidate(); -> 可以解决 ”一直“ 高亮的问题，不过点击的瞬间会闪现一下高亮的颜色。而 RecyclerView 里面使用则完全不会闪现高亮的颜色。
        textView.setHighlightColor(Color.TRANSPARENT);
    }


    /**
     * 设置点击事件
     *
     * 记得设置 TextView 允许跳转，不设置点击无反应
     * tv.setMovementMethod(LinkMovementMethod.getInstance());
     */
    public SpannableStringBuilder setOnClickListener(OnClickSpanStringListener clickListener){
        mHasSetOnClickListener = true;
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

    /**
     * {@link #into}
     * @deprecated
     */
    public @CheckResult SpannableString build(){
        return mSpannableString;
    }

    /**
     * 将构建好的 SpannableString 赋值到 TextView
     */
    public void into(TextView textView){
        if (textView == null) return;
        if (mHasSetOnClickListener){
            setMovementMethod(textView);
        }
        textView.setText(mSpannableString);
    }

    /**
     * 在 {@link #into} 不适应的场景下，可手动改造并赋值到 TextView
     */
    public Manual manual(){
        if (mManual == null){
            mManual = new Manual(this);
        }
        return mManual;
    }













    ///////////////////////////// 分割线 /////////////////////////////
    /**
     * 一些不常用的方法，则封装到 Manual 中，在特殊情况下可以调用。
     */
    public static class Manual{
        SpannableStringBuilder mStringBuilder;

        public Manual(SpannableStringBuilder stringBuilder) {
            mStringBuilder = stringBuilder;
        }

        public SpannableStringBuilder builder() {
            return mStringBuilder;
        }

        public @CheckResult SpannableString build(){
            return mStringBuilder.mSpannableString;
        }

        public Manual setMovementMethod(TextView textView){
            mStringBuilder.setMovementMethod(textView);
            return this;
        }
    }

}
