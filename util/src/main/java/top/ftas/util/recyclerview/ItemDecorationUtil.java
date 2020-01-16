package top.ftas.util.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-01-03 17:28
 *
 * 解决 GridLayoutManager LinearLayoutManager 空隙分割问题
 * 针对的样式：一个标题 -> 一堆小标签 -> 一个 Banner -> 一堆其它小标签
 * 不可使用的样式：一堆 A 小标签 -> 一堆 B 小标签 这种小标签混排的样式
 * 可使用的样式：大标签与各种小标签相互交替
 *
 * ItemDecorationUtil.setDefaultDividerId(R.drawable.layer_list_listview_divider);
 * 可全局设置分割线的样式
 *
 *
 * 优先级 getItemOffsets_GridLayoutManager：isAutoResetHolderFreeze -> resetForGetItemOffsets -> hideOffsetAtPosition -> isShowDividerLine -> 正常绘制 Item 空隙
 * 优先级 getItemOffsets_LinearLayoutManager：isAutoResetHolderFreeze -> resetForGetItemOffsets -> hideOffsetAtPosition -> isShowDividerLine -> 正常绘制 Item 空隙
 * 优先级 onDraw：isForceNotDrawAnyFreeze -> isAutoResetHolderFreeze -> resetForOnDraw -> hideOffsetAtPosition -> isShowDividerLine -> 绘制分割线
 *
 * DecorationConfig 类中不应当存储成员变量。
 * DecorationConfig 类中的方法的返回值优先级永远大于 set 设置的属性值
 *
 * 所有带有 freeze 标记属性，都不会被 reset 方法处理
 *
 * 只有 {@link ItemDecorationUtil#buildDivider} 和 {@link DecorationDividerConfig} 类会自动将 mIsForceNotDrawAnyFreeze 置为 false，（{@link ItemDecorationUtilHolder#setShowDividerLine(boolean)} ）
 * 其它都需要手动调用 {@link ItemDecorationUtil#setShowDividerLine()}，否则分割线可能不出现。
 */
public class ItemDecorationUtil {
    //默认的分割线 id
    private static int sDefaultDividerId = -1;

    public static void setDefaultDividerId(@DrawableRes int defaultDividerId) {
        sDefaultDividerId = defaultDividerId;
    }

    /**
     * 分割线配置类
     * isShowDividerLine 默认返回 true -- 请求绘制分割线
     * isShowLastDividerLine 默认返回 false -- 不绘制最后一个 Item 的分割线
     * isForceNotDrawAnyFreeze 默认返回 false -- 允许绘制分割线，即允许执行 onDraw 方法
     */
    public static abstract class DecorationDividerConfig extends DecorationResetConfig{

        @Override
        public boolean isShowDividerLine(int position) {
            return true;
        }

        @Override
        public boolean isShowLastDividerLine() {
            return false;
        }

        @Override
        public boolean isForceNotDrawAnyFreeze() {
            return false;
        }

        /**
         * 设置 hideOffsetAtPosition 返回 true，将不会绘制分割线
         */
        public abstract boolean hideOffsetAtPosition(int position);

        @Override
        public boolean resetForGetItemOffsets(@NonNull ItemDecorationUtilHolder holder, int position, @NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            return false;
        }
    }

    /**
     * 此类会自动重置 Holder，即调用 reset 方法
     * isAutoResetHolderFreeze 返回 true -- 调用 resetForGetItemOffsets ，框架会自动重置 Holder
     */
    public static abstract class DecorationResetConfig extends DecorationConfig{

        /**
         * 设置 Item 间隙前 重新设置 ItemDecorationUtilHolder 的配置项
         * @return 返回 true，将终止后续步骤
         */
        public abstract boolean resetForGetItemOffsets(@NonNull ItemDecorationUtilHolder holder,int position,@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state);
    }

    /**
     * 默认的 DecorationConfig ，不会在运行的时候，每次都自动调用 reset 方法。
     * isAutoResetHolderFreeze 返回 false
     */
    private static class DefaultDecorationConfig extends DecorationConfig{
        @Override
        public boolean isAutoResetHolderFreeze() {
            return false;
        }
    }

    /**
     * 分割线配置类
     */
    public static class DecorationConfig {
        private ItemDecorationUtilHolder mHolder;
        private Context mContext;

        final DecorationConfig setHolder(ItemDecorationUtilHolder holder) {
            mHolder = holder;
            return this;
        }

        protected final ItemDecorationUtilHolder getHolder() {
            return mHolder;
        }

        /**
         * 强制不绘制任何内容（包括分割线）。即 onDraw 方法直接 return 了。
         * 优化级：在 onDraw 中，优先级最高。当然，也仅控件 onDraw 方法。
         */
        public boolean isForceNotDrawAnyFreeze(){
            return mHolder.mIsForceNotDrawAnyFreeze;
        }

        /**
         * 是否自动重置 Holder
         * 如果设置了自定义的 Config ，默认会自动重置 Holder
         * 如果没有设置自定义的 Config ，默认不会自动重置 Holder， 请使用 {@link ItemDecorationUtilHolder#setAutoResetHolderFreeze(boolean)}
         */
        public boolean isAutoResetHolderFreeze(){
            return mHolder.mIsAutoResetHolderFreeze;
        }

        /**
         * 绘制前 重新设置 ItemDecorationUtilHolder 的配置项
         * @return 返回 true，将终止后续步骤。即终止当次分割线的绘制
         */
        public boolean resetForOnDraw(@NonNull ItemDecorationUtilHolder holder,int position,@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            return false;
        }

        /**
         * 设置 Item 间隙前 重新设置 ItemDecorationUtilHolder 的配置项
         * @return 返回 true，将终止后续步骤
         */
        public boolean resetForGetItemOffsets(@NonNull ItemDecorationUtilHolder holder,int position,@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            return false;
        }

        /**
         * 获取分割线的 Drawable 对象。
         */
        public @NonNull Drawable getDividerDrawable(Context context, int position) {
            if (mHolder.mDivider == null) {
                if (getDividerDrawableId(position) != -1){
                    mHolder.mDivider = ContextCompat.getDrawable(context, getDividerDrawableId(position));
                }
                if (mHolder.mDivider == null){
                    //默认
                    mHolder.mDivider = new ColorDrawable(Color.parseColor("#ebebeb")){
                        @Override
                        public int getIntrinsicHeight() {
                            return 1;
                        }
                    };
                }
            }
            return mHolder.mDivider;
        }

        /**
         * 是否绘制分割线。
         */
        public boolean isShowDividerLine(int position){
            return mHolder.mShowDividerLine;
        }

        /**
         * 是否展示最后一个 Item 的分割线
         */
        public boolean isShowLastDividerLine(){
            return mHolder.mShowLastDividerLine;
        }

        /**
         * 如果禁止了分割间距，则 canShowDivider 无效
         */
        public boolean hideOffsetAtPosition(int position) {
            return false;
        }

        public @DrawableRes int getDividerDrawableId(int position){
            return mHolder.mDividerDrawableIdFreeze;
        }

        /**
         * 将dip或dp值转换为px值，保证尺寸大小不变
         */
        public int dip2px(float dipValue) {
            return ItemDecorationUtil.dip2px(mContext,dipValue);
        }

        /**
         * 获取列表中的项
         */
        public @Nullable Object getItem(@Nullable List list,int position){
            if (list == null) return null;
            if (position < 0 || position >= list.size()){
                return null;
            }
            return list.get(position);
        }

    }

    public static class ItemDecorationUtilHolder {
        private float mLeftRightSpace;
        private float mHorizontalCenterSpace;
        private float mTopSpace;
        private float mBottomSpace;
        private float mVerticalCenterSpace;
        private boolean mTopEqualCenter = false;
        private boolean mBottomEqualCenter = false;
        //配置分割线
        private DecorationConfig mDecorationConfig;
        //是否展示分割线
        private boolean mShowDividerLine = false;
        //是否显示最后一行的分割线
        private boolean mShowLastDividerLine = false;
        private final Rect mBounds = new Rect();
        private Drawable mDivider;



        /* ******* 不会被 reset 方法重置掉。 ******* */
        //强制不绘制任何内容。在 onDraw 方法中，优先级最高。
        private boolean mIsForceNotDrawAnyFreeze = true;
        /**
         * 是否自动重置 Holder，
         * 如果设置了用户自定义的 Config，会每次自动重置 Holder。
         * 如果没有设置用户自定义的 Config，会使用 {@link DefaultDecorationConfig#isAutoResetHolderFreeze()}，默认不自动重置 Holder
         */
        private boolean mIsAutoResetHolderFreeze = true;
        //分割线的 Drawable Id
        private int mDividerDrawableIdFreeze = sDefaultDividerId;

        /**
         * 重置此分割线配置器，参数还原到默认值。
         * 特别注意，reset 方法不会重置 $isForceNotDrawAnyFreeze 属性。如果需要重置他，请再调用 {@link #setForceNotDrawAnyFreeze}
         */
        public final ItemDecorationUtilHolder reset() {
            mLeftRightSpace = 0;
            mHorizontalCenterSpace = 0;
            mTopSpace = 0;
            mBottomSpace = 0;
            mVerticalCenterSpace = 0;
            mTopEqualCenter = false;
            mBottomEqualCenter = false;
            mShowDividerLine = false;
            mShowLastDividerLine = false;
            mDivider = null;
            return this;
        }

        private @NonNull DecorationConfig getDecorationConfig(@NonNull RecyclerView parent) {
            if (mDecorationConfig == null){
                setDecorationConfig(new DefaultDecorationConfig());
            }
            if (mDecorationConfig.mContext == null){
                mDecorationConfig.mContext = parent.getContext();
            }
            return mDecorationConfig;
        }

        public final ItemDecorationUtilHolder setDecorationConfig(DecorationConfig decorationConfig) {
            if (decorationConfig != null){
                decorationConfig.setHolder(this);
                mDecorationConfig = decorationConfig;
            }
            return this;
        }

        /**
         * 设置是否强制不绘制任何内容
         */
        public ItemDecorationUtilHolder setForceNotDrawAnyFreeze(boolean forceNotDrawAnyFreeze) {
            mIsForceNotDrawAnyFreeze = forceNotDrawAnyFreeze;
            return this;
        }

        /**
         * 设置是否自动重置 Holder
         */
        public ItemDecorationUtilHolder setAutoResetHolderFreeze(boolean autoResetHolderFreeze) {
            mIsAutoResetHolderFreeze = autoResetHolderFreeze;
            return this;
        }

        /**
         * 设置分割线的 drawableId
         */
        public ItemDecorationUtilHolder setDividerDrawableIdFreeze(@DrawableRes int dividerDrawableIdFreeze) {
            mDividerDrawableIdFreeze = dividerDrawableIdFreeze;
            return this;
        }

        /**
         * 请求展示最后一行的分割线，默认不展示
         */
        public ItemDecorationUtilHolder setShowLastDividerLine() {
            return setShowLastDividerLine(true);
        }

        /**
         * 请求展示最后一个 Item 的分割线
         */
        public ItemDecorationUtilHolder setShowLastDividerLine(boolean showLastDividerLine) {
            mShowLastDividerLine = showLastDividerLine;
            return this;
        }

        /**
         * 是否显示分割线。
         * 如果要求显示分割线，则会使用分割线的宽度来计算 item 间距。同时，mIsForceNotDrawAnyFreeze 会自动设置为 false。即允许绘制分割线。
         */
        public ItemDecorationUtilHolder setShowDividerLine(boolean showDividerLine) {
            if (showDividerLine){
                mIsForceNotDrawAnyFreeze = false;
            }
            mShowDividerLine = showDividerLine;
            return this;
        }

        public ItemDecorationUtilHolder setShowDividerLine() {
            return setShowDividerLine(true);
        }

        public ItemDecorationUtilHolder setLeftRightSpace(float dp_leftRightSpace) {
            mLeftRightSpace = dp_leftRightSpace;
            return this;
        }

        public ItemDecorationUtilHolder setHorizontalCenterSpace(float dp_horizontalCenterSpace) {
            mHorizontalCenterSpace = dp_horizontalCenterSpace;
            return this;
        }

        public ItemDecorationUtilHolder setTopSpace(float dp_topSpace) {
            mTopSpace = dp_topSpace;
            return this;
        }


        public ItemDecorationUtilHolder setBottomSpace(float dp_bottomSpace) {
            mBottomSpace = dp_bottomSpace;
            return this;
        }


        public ItemDecorationUtilHolder setVerticalCenterSpace(float dp_verticalCenterSpace) {
            mVerticalCenterSpace = dp_verticalCenterSpace;
            if (mTopEqualCenter) {
                mTopSpace = mVerticalCenterSpace;
            }
            if (mBottomEqualCenter) {
                mBottomSpace = mVerticalCenterSpace;
            }
            return this;
        }

        /**
         * 顶部间隙与中间相等
         */
        public ItemDecorationUtilHolder topIsEqualCenter() {
            mTopEqualCenter = true;
            mTopSpace = mVerticalCenterSpace;
            return this;
        }

        /**
         * 底部间隙与中间相等
         */
        public ItemDecorationUtilHolder bottomIsEqualCenter() {
            mBottomEqualCenter = true;
            mBottomSpace = mVerticalCenterSpace;
            return this;
        }

        /**
         * 处理 LinearLayoutManager
         */
        private ItemDecorationUtilHolder getItemOffsets_LinearLayoutManager(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            RecyclerView.Adapter recyclerAdapter = parent.getAdapter();
            Context context = view.getContext();
            if (recyclerAdapter == null || context == null) {
                Log.e("ItemDecorationUtil", "无法获取到 Adapter / context ");
                return this;
            }

            int position = parent.getChildAdapterPosition(view); // item position
            int count = recyclerAdapter.getItemCount();
            DecorationConfig decorationConfig = getDecorationConfig(parent);

            //判断是否自动重置 Holder
            if (decorationConfig.isAutoResetHolderFreeze()){
                reset();
            }

            //resetForGetItemOffsets 作为设置 Item 间隙的前置条件，可以重新配置 Item 间隙
            if (decorationConfig.resetForGetItemOffsets(this,position,outRect,view,parent,state)){
                return this;
            }

            //检查是否允许为每个 Item 保留间隙
            //根据 Recycler 源码可知，mTempRect 在传递过来之前，已经设置为了 0,0,0,0。故 hideOffsetAtPosition 的时候，不需要两次设置
            //mTempRect.set(0, 0, 0, 0);
            //mItemDecorations.get(i).getItemOffsets(mTempRect, child, this, mState);
            if (decorationConfig.hideOffsetAtPosition(position)){
                return this;
            }

            /* ************* 分割线 保留间隙 ****************** */
            //检查是否需要展示分割线。如果需要展示分割线，将会按照分割线的高度来保留 Item 的间隙。
            if (decorationConfig.isShowDividerLine(position)){
                //判断是否是最后一行
                boolean isEndRow = position == count - 1;
                if (isEndRow && !decorationConfig.isShowLastDividerLine()){
                    //根据 Recycler 源码可知，mTempRect 在传递过来之前，已经设置为了 0,0,0,0。
                    return this;
                }
                //根据 Recycler 源码可知，mTempRect 在传递过来之前，已经设置为了 0,0,0,0。
                outRect.bottom = decorationConfig.getDividerDrawable(context,position).getIntrinsicHeight();
                return this;
            }

            //计算左右和中间的空白区域总大小和平均大小
            final int leftRightSpace = dip2px(context, mLeftRightSpace);
            final int verticalCenterSpace = dip2px(context, mVerticalCenterSpace);

            //处理水平方向上空白间距
            outRect.left = leftRightSpace;
            outRect.right = leftRightSpace;

            //处理垂直方向上空白间距
            //判断是否是第一行
            boolean isFirstRow = position == 0;
            if (isFirstRow) {
                outRect.top = dip2px(context, mTopSpace);
            } else {
                outRect.top = verticalCenterSpace; // item top
            }

            //判断是否是最后一行
            boolean isEndRow = position == count - 1;
            if (isEndRow) {
                outRect.bottom = dip2px(context, mBottomSpace);
            }
            return this;

        }

        /**
         * 为每个 Item 保留间隙
         */
        private ItemDecorationUtilHolder getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                return getItemOffsets_GridLayoutManager((GridLayoutManager) layoutManager,outRect, view, parent, state);
            } else if (layoutManager instanceof LinearLayoutManager) {
                return getItemOffsets_LinearLayoutManager(outRect, view, parent, state);
            } else {
                Log.e("ItemDecorationUtil", "非 GridLayoutManager 或者 LinearLayoutManager  ，使用了 ItemDecorationUtil -> " + layoutManager);
                return this;
            }
        }

        /**
         * 绘制分割线
         */
        private void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            DecorationConfig decorationConfig = getDecorationConfig(parent);

            if (decorationConfig.isForceNotDrawAnyFreeze()){
                return;
            }

            if (parent.getLayoutManager() == null) {
                return;
            }

            canvas.save();
            final int left;
            final int right;
            //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
            if (parent.getClipToPadding()) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
            } else {
                left = 0;
                right = parent.getWidth();
            }

            int childCount = parent.getChildCount();

            int length = decorationConfig.isShowLastDividerLine() ? childCount : childCount - 1;


            for (int i = 0; i < length; i++) {

                int position = parent.getChildAdapterPosition(parent.getChildAt(i));


                //判断是否自动重置 Holder
                if (decorationConfig.isAutoResetHolderFreeze()){
                    reset();
                }

                //resetForOnDraw 作为绘制 Item 分割线的前置条件，可以重新配置分割线。也可以终止当次分割线的绘制
                if (decorationConfig.resetForOnDraw(this,position,canvas,parent,state)){
                    continue;
                }

                if (decorationConfig.hideOffsetAtPosition(position) || !decorationConfig.isShowDividerLine(position)){
                    continue;
                }

                Drawable dividerDrawable = decorationConfig.getDividerDrawable(parent.getContext(),position);

                final View child = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - dividerDrawable.getIntrinsicHeight();
                dividerDrawable.setBounds(left, top, right, bottom);
                dividerDrawable.draw(canvas);
            }
            canvas.restore();
        }

        /*
# RecyclerView 水平方向上间距算法详解

要求：左右 item 间隙 15 ，记作 lrs ，中间item 间隙 10 ，记作cs

item 的左手记作：L
item 的右手记作：R
item 的中间（item 自己）记作：C

L+10  C10-4=6  R-6  L+6  C10-4*2=2  R-2  L+2  C10-4*3=-2  R-(-2)  L+(-2)  C10-4*4=-6  R-(-6)  L+(-6)  C10-4*5=-10  R-(-10)

注意：最左侧 item 左手多占领的空隙（默认情况上，每个 item 的左右手都是占领 cs / 2 的空隙）
lrs - cs / 2 = 15 - 10 /2 = 10 记作 lrm

L+(10 - 0 * 4)  C10-4=6  R-(10 - (0 + 1) * 4)  L+(10 - 1 * 4)  C10-4*2=2  R-(10 - (1 + 1) * 4)  L+(10 - 2 * 4)  C10-4*3=-2  R-(10 - (2 + 1) * 4)  L+(10 - 3 * 4)  C10-4*4=-6  R-(10 - (3 + 1) * 4)  L+(10 - 4 * 4)  C10-4*5=-10  R-(10 - (4 + 1) * 4)

增量算法（item 左右手多占领的空隙）：
L+(lrm - column * 4 )
R-(lrm - (column + 1) * r)

=>
完整的左右空隙算法（每个 item 左右手实际占领的空隙）：
其中，column 从 0 开始
其中，正常情况下一半的中间 item 空隙大小 halfCS = cs / 2
其中，左右 item 需要多占领的空隙大小 lrm = lrs - halfCS
其中，4 代表每个 item 需要均摊的大小（lrm * 2 / allColumnCount

left = halfCS + (lrm - column * 4 )
right = halfCS - (lrm - (column + 1) * 4)
         */

        /**
         * 算法参考网址
         * https://www.jianshu.com/p/a024c66ddb3f
         */
        private ItemDecorationUtilHolder getItemOffsets_GridLayoutManager(GridLayoutManager gridLayoutManager,Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            RecyclerView.Adapter recyclerAdapter = parent.getAdapter();
            Context context = view.getContext();
            if (recyclerAdapter == null || context == null) {
                Log.e("ItemDecorationUtil", "无法获取到 Adapter / context ");
                return this;
            }

            int position = parent.getChildAdapterPosition(view); // item position

            DecorationConfig decorationConfig = getDecorationConfig(parent);

            //判断是否自动重置 Holder
            if (decorationConfig.isAutoResetHolderFreeze()){
                reset();
            }

            //resetForGetItemOffsets 作为设置 Item 间隙的前置条件，可以重新配置 Item 间隙
            if (decorationConfig.resetForGetItemOffsets(this,position,outRect,view,parent,state)){
                return this;
            }

            if (decorationConfig.hideOffsetAtPosition(position)){
                return this;
            }

            //检查是否需要展示分割线
            if (decorationConfig.isShowDividerLine(position)){
                outRect.bottom = decorationConfig.getDividerDrawable(context,position).getIntrinsicHeight();
                return this;
            }

            GridLayoutManager.LayoutParams layoutParams = ((GridLayoutManager.LayoutParams) (view.getLayoutParams()));
            int spanIndex = layoutParams.getSpanIndex();
            int spanSize = layoutParams.getSpanSize();
            int typeColumn = spanIndex / spanSize;
            int prePosition = position - 1 - typeColumn;
            int endPosition;

            int allSpanCount = gridLayoutManager.getSpanCount();
            int currentSpanSize = 1;
            int preSpanSize = prePosition >= 0 ? 1 : -1;
            int endSpanSize = -1;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            if (spanSizeLookup != null) {
                currentSpanSize = spanSizeLookup.getSpanSize(position);
                preSpanSize = prePosition >= 0 ? spanSizeLookup.getSpanSize(prePosition) : -1;
            }
            //当前 item 类型，一行最多允许展示的 item 数
            //每行的总列数
            int currentTypeSpanCount = allSpanCount / currentSpanSize;

            if (spanSizeLookup != null) {
                //GridLayout 的换行逻辑是，先在当前行排列。排列不下，才换行。或者遇到 spanCount 不同的类型，则直接换行。
                //这里，得从当前列 n 开始。向后依次的信息 columnCount + n 个。如果 spanCount 一直和当前 item 一样，则表示不是最后一行。
                //下面，将从当前列开始，一直推算到可能的下一行。根据下一行的 spanSize 与 当前 item 的 spanSize 是否相等，来判断当前 item 所在的行是否是本类型的最后一行。
                int remainCalculateColumn = currentTypeSpanCount - typeColumn;
                int adapterItemCount = recyclerAdapter.getItemCount();
                for (int i = 1; i <= remainCalculateColumn; i++) {
                    endPosition = position + i;
                    if (endPosition < adapterItemCount) {
                        endSpanSize = spanSizeLookup.getSpanSize(endPosition);
                        if (currentSpanSize != endSpanSize) {
                            break;
                        }
                    } else {
                        endSpanSize = -1;
                        break;
                    }
                }
            }

            //计算左右和中间的空白区域总大小和平均大小
            final float leftRightSpace = dip2px(context, mLeftRightSpace);
            final float horizontalCenterSpace = dip2px(context, mHorizontalCenterSpace);
            final int verticalCenterSpace = dip2px(context, mVerticalCenterSpace);
//            final int allSpace = (currentTypeSpanCount - 1) * horizontalCenterSpace + leftRightSpace * 2;
//            final int averageSpace = allSpace / currentTypeSpanCount;
//            final int endColumnIndex = currentTypeSpanCount - 1;
            final float halfHorizontalCenterSpace = horizontalCenterSpace / 2f;
            //左右 item 的空隙正常情况下，是占领 halfHorizontalCenterSpace 的空间则刚刚好。由于单独设定了左右 item 距离侧边的大小，下面计算左右 item 因此而需要多占领的空间。
            final float leftMoreSpace = leftRightSpace - halfHorizontalCenterSpace;
            //由于左右 item 的空隙需要多占领一些空间，那么这些多占领的空间需均摊到每个 item 上面。下面计算每个 item 需要均摊的空间。
            final float leftRightAppendSpace = (leftMoreSpace * 2f) / currentTypeSpanCount;

            //处理水平方向上空白间距
            outRect.left = (int) (halfHorizontalCenterSpace + (leftMoreSpace - typeColumn * leftRightAppendSpace));
            outRect.right = (int) (halfHorizontalCenterSpace - (leftMoreSpace - (typeColumn + 1) * leftRightAppendSpace));

            //处理垂直方向上空白间距
            //判断是否是某种换行模式的第一行
            boolean isFirstRow = currentSpanSize != preSpanSize;
            if (isFirstRow) {
                outRect.top = dip2px(context, mTopSpace);
            } else {
                outRect.top = verticalCenterSpace; // item top
            }

            //判断是否是某种换行模式的最后一行
            boolean isEndRow = currentSpanSize != endSpanSize;
            if (isEndRow) {
                outRect.bottom = dip2px(context, mBottomSpace);
            }
            return this;
        }

        public RecyclerView.ItemDecoration build() {
            return new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    ItemDecorationUtilHolder.this.getItemOffsets(outRect, view, parent, state);
                }

                @Override
                public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    ItemDecorationUtilHolder.this.onDraw(c, parent, state);
                }
            };
        }
    }

    public static ItemDecorationUtilHolder setLeftRightSpace(float dp_leftRightSpace) {
        ItemDecorationUtilHolder itemDecorationUtilHolder = new ItemDecorationUtilHolder();
        itemDecorationUtilHolder.mLeftRightSpace = dp_leftRightSpace;
        return itemDecorationUtilHolder;
    }

    public static ItemDecorationUtilHolder setHorizontalCenterSpace(float dp_horizontalCenterSpace) {
        ItemDecorationUtilHolder itemDecorationUtilHolder = new ItemDecorationUtilHolder();
        itemDecorationUtilHolder.mHorizontalCenterSpace = dp_horizontalCenterSpace;
        return itemDecorationUtilHolder;
    }

    public static ItemDecorationUtilHolder setTopSpace(float dp_topSpace) {
        ItemDecorationUtilHolder itemDecorationUtilHolder = new ItemDecorationUtilHolder();
        itemDecorationUtilHolder.mTopSpace = dp_topSpace;
        return itemDecorationUtilHolder;
    }

    public static ItemDecorationUtilHolder setBottomSpace(float dp_bottomSpace) {
        ItemDecorationUtilHolder itemDecorationUtilHolder = new ItemDecorationUtilHolder();
        itemDecorationUtilHolder.mBottomSpace = dp_bottomSpace;
        return itemDecorationUtilHolder;
    }

    public static ItemDecorationUtilHolder setVerticalCenterSpace(float dp_verticalCenterSpace) {
        ItemDecorationUtilHolder itemDecorationUtilHolder = new ItemDecorationUtilHolder();
        itemDecorationUtilHolder.mVerticalCenterSpace = dp_verticalCenterSpace;
        return itemDecorationUtilHolder;
    }


    /**
     * 是否显示分割线。
     * 如果要求显示分割线，则会使用分割线的宽度来计算 item 间距。
     */
    public static ItemDecorationUtilHolder setShowDividerLine() {
        return setShowDividerLine(true);
    }

    /**
     * 是否显示分割线
     */
    public static ItemDecorationUtilHolder setShowDividerLine(boolean showDividerLine) {
        return new ItemDecorationUtilHolder().setShowDividerLine(showDividerLine);
    }

    /**
     * 设置是否强制不绘制任何内容
     */
    public ItemDecorationUtilHolder setForceNotDrawAnyFreeze(boolean forceNotDrawAnyFreeze) {
        return new ItemDecorationUtilHolder().setForceNotDrawAnyFreeze(forceNotDrawAnyFreeze);
    }

    /**
     * 设置是否自动重置 Holder
     */
    public ItemDecorationUtilHolder setAutoResetHolderFreeze(boolean autoResetHolderFreeze) {
        return new ItemDecorationUtilHolder().setAutoResetHolderFreeze(autoResetHolderFreeze);
    }

    /**
     * 创建一条分割线
     */
    public static RecyclerView.ItemDecoration buildDivider(){
        return buildDivider(false);
    }

    /**
     * 创建一条分割线
     */
    public static RecyclerView.ItemDecoration buildDivider(boolean showLastDividerLine){
        return setShowDividerLine()
                .setShowLastDividerLine(showLastDividerLine)
                .build();
    }

    /**
     * 此方法默认会绘制分割线。不需要对 dividerConfig 做特殊处理。
     * 当然，你也可以传递 {@link DecorationDividerConfig}，这样默认你也不需要做其它处理就会绘制分割线。
     */
    public static RecyclerView.ItemDecoration buildDivider(DecorationResetConfig dividerConfig){
        return setShowDividerLine()
                .setDecorationConfig(dividerConfig)
                .build();
    }


    /**
     * 由于有根据 position 动态配置分割线的情况，这里可以直接通过 build 方法传递一个 DecorationConfig
     * 注意，此方法默认不会绘制分割线。
     * 如果需要绘制分割线，请使用 {@link #buildDivider(DecorationResetConfig)} ，或者调用 {@link ItemDecorationUtilHolder#setForceNotDrawAnyFreeze(boolean)}
     */
    public static RecyclerView.ItemDecoration buildConfig(DecorationResetConfig decorationConfig){
        return new ItemDecorationUtilHolder()
                .setDecorationConfig(decorationConfig)
                .build();
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    private static int dip2px(Context context, float dipValue) {
        if (context == null || dipValue == 0) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
