package top.ftas.util.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-01-03 17:28
 * 解决 GridLayoutManager LinearLayoutManager 空隙分割问题
 * 针对的样式：一个标题 -> 一堆小标签 -> 一个 Banner -> 一堆其它小标签
 * 不可使用的样式：一堆 A 小标签 -> 一堆 B 小标签 这种小标签混排的样式
 * 可使用的样式：大标签与各种小标签相互交替
 */
public class ItemDecorationUtil {
    private static ItemDecorationUtilHolder sItemDecorationUtil;
    //默认的分割线配置类，这样下面的代码中不需要判空
    private static DecorationConfig sDefaultConfig;
    //默认的分割线 id
    private static int sDefaultDividerId = -1;

    public static void setDefaultDividerId(@DrawableRes int defaultDividerId) {
        sDefaultDividerId = defaultDividerId;
    }

    public static @NonNull DecorationConfig getDefaultConfig() {
        if (sDefaultConfig == null){
            sDefaultConfig = new DecorationConfig();
        }
        return sDefaultConfig;
    }

    public static abstract class DecorationDividerConfig extends DecorationConfig{
        @Override
        public boolean isShowDividerLine(int position) {
            return true;
        }

        @Override
        public boolean isShowLastDividerLine() {
            return false;
        }

        public abstract boolean hideOffsetAtPosition(int position);
    }

    public static class DecorationConfig {
        private Drawable mDivider;
        private ItemDecorationUtilHolder mHolder;

        final DecorationConfig setHolder(ItemDecorationUtilHolder holder) {
            mHolder = holder;
            return this;
        }

        protected final @Nullable ItemDecorationUtilHolder getHolder() {
            return mHolder;
        }

        /**
         * 绘制前 重新设置 ItemDecorationUtilHolder 的配置项
         * @return 返回 true，将终止后续步骤
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

        public @Nullable Drawable getDividerDrawable(Context context, int position) {
            if (mDivider == null && getDividerDrawableId(position) != -1) {
                mDivider = ContextCompat.getDrawable(context, getDividerDrawableId(position));
            }
            return mDivider;
        }

        /**
         * 是否绘制分割线。（前提是 hideOffsetAtPosition 返回 false）
         */
        public boolean isShowDividerLine(int position){
            if (mHolder != null){
                return mHolder.mShowDividerLine;
            }
            return false;
        }

        public boolean isShowLastDividerLine(){
            if (mHolder != null){
                return mHolder.mShowLastDividerLine;
            }
            return false;
        }

        /**
         * 如果禁止了分割间距，则 canShowDivider 无效
         */
        public boolean hideOffsetAtPosition(int position) {
            return false;
        }

        public @DrawableRes int getDividerDrawableId(int position){
            return sDefaultDividerId;
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

        public ItemDecorationUtilHolder() {
            setDecorationConfig(getDefaultConfig());
        }

        public final DecorationConfig getDecorationConfig() {
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
         * 请求展示最后一行的分割线，默认不展示
         */
        public ItemDecorationUtilHolder setShowLastDividerLine() {
            mShowLastDividerLine = true;
            return this;
        }

        /**
         * 是否显示分割线。
         * 如果要求显示分割线，则会使用分割线的宽度来计算 item 间距。
         */
        public ItemDecorationUtilHolder setShowDividerLine() {
            mShowDividerLine = true;
            return this;
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

            //resetForGetItemOffsets 作为设置 Item 间隙的前置条件，可以重新配置 Item 间隙
            if (mDecorationConfig.resetForGetItemOffsets(this,position,outRect,view,parent,state)){
                return this;
            }

            //检查是否允许为每个 Item 保留间隙
            if (mDecorationConfig.hideOffsetAtPosition(position)){
                outRect.set(0, 0, 0, 0);
                return this;
            }

            /* ************* 分割线 保留间隙 ****************** */
            //检查是否需要展示分割线
            if (mDecorationConfig.isShowDividerLine(position)){
                //判断是否是最后一行
                boolean isEndRow = position == count - 1;
                if (isEndRow && !mDecorationConfig.isShowLastDividerLine()){
                    outRect.set(0, 0, 0, 0);
                    return this;
                }
                Drawable dividerDrawable = mDecorationConfig.getDividerDrawable(context,position);
                if (dividerDrawable != null){
                    outRect.set(0, 0, 0, dividerDrawable.getIntrinsicHeight());
                }
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
        public ItemDecorationUtilHolder getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager == null) {
                return this;
            }
            if (layoutManager instanceof GridLayoutManager) {
                return getItemOffsets_GridLayoutManager((GridLayoutManager) layoutManager,outRect, view, parent, state);
            } else if (layoutManager instanceof LinearLayoutManager) {
                return getItemOffsets_LinearLayoutManager(outRect, view, parent, state);
            } else {
                Log.e("ItemDecorationUtil", "非 GridLayoutManager 或者 LinearLayoutManager ，使用了 ItemDecorationUtil");
                return this;
            }
        }

        /**
         * 绘制分割线
         */
        public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
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

            int length = mDecorationConfig.isShowLastDividerLine() ? childCount : childCount - 1;


            for (int i = 0; i < length; i++) {

                int position = parent.getChildAdapterPosition(parent.getChildAt(i));

                Drawable dividerDrawable = mDecorationConfig.getDividerDrawable(parent.getContext(),position);

                //resetForOnDraw 作为绘制 Item 分割线的前置条件，可以重新配置分割线
                if (mDecorationConfig.resetForOnDraw(this,position,canvas,parent,state)){
                    continue;
                }

                if (mDecorationConfig.hideOffsetAtPosition(position) || !mDecorationConfig.isShowDividerLine(position) || dividerDrawable == null){
                    continue;
                }

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

            //resetForGetItemOffsets 作为设置 Item 间隙的前置条件，可以重新配置 Item 间隙
            if (mDecorationConfig.resetForGetItemOffsets(this,position,outRect,view,parent,state)){
                return this;
            }

            if (mDecorationConfig.hideOffsetAtPosition(position)){
                outRect.set(0, 0, 0, 0);
                return this;
            }
            //检查是否需要展示分割线
            if (mDecorationConfig.isShowDividerLine(position)){
                Drawable dividerDrawable = mDecorationConfig.getDividerDrawable(context,position);
                if (dividerDrawable != null){
                    outRect.set(0, 0, 0, dividerDrawable.getIntrinsicHeight());
                }
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

            //Log.e("test","left : " + outRect.left + " right:" + outRect.right + "       l:" + leftRightSpace + " h:" + horizontalCenterSpace + " a:" + leftRightAppendSpace + " h:" + halfHorizontalCenterSpace);

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

    /**
     * 一般不建议使用此方法，而应当使用 build 的方式。
     * 除非你只有部分 item 需要设置间距
     *
     * 废弃的，考虑到稳定性，这里保留 2 个版本
     */
    @Deprecated
    public static ItemDecorationUtilHolder reset() {
        if (sItemDecorationUtil == null) {
            sItemDecorationUtil = new ItemDecorationUtilHolder();
        }
        sItemDecorationUtil.mLeftRightSpace = 0;
        sItemDecorationUtil.mHorizontalCenterSpace = 0;
        sItemDecorationUtil.mTopSpace = 0;
        sItemDecorationUtil.mBottomSpace = 0;
        sItemDecorationUtil.mVerticalCenterSpace = 0;
        sItemDecorationUtil.mTopEqualCenter = false;
        sItemDecorationUtil.mBottomEqualCenter = false;
        sItemDecorationUtil.setDecorationConfig(getDefaultConfig());
        sItemDecorationUtil.mShowDividerLine = false;
        return sItemDecorationUtil;
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
        ItemDecorationUtilHolder itemDecorationUtilHolder = new ItemDecorationUtilHolder();
        return itemDecorationUtilHolder.setShowDividerLine();
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
        if (showLastDividerLine){
            return setShowDividerLine()
                    .setShowLastDividerLine()
                    .build();
        }else {
            return setShowDividerLine()
                    .build();
        }
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    private static int dip2px(Context context, float dipValue) {
        if (context == null || dipValue == 0) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
