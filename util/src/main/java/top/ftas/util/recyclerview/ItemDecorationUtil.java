package top.ftas.util.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
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
 *
 */
public class ItemDecorationUtil {
    public static ItemDecorationUtilHolder sItemDecorationUtil;

    public static class ItemDecorationUtilHolder {
        private float mLeftRightSpace;
        private float mHorizontalCenterSpace;
        private float mTopSpace;
        private float mBottomSpace;
        private float mVerticalCenterSpace;
        private boolean mTopEqualCenter = false;
        private boolean mBottomEqualCenter = false;

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
            if (mTopEqualCenter){
                mTopSpace = mVerticalCenterSpace;
            }
            if (mBottomEqualCenter){
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
         * @param outRect
         * @param view
         * @param parent
         * @param state
         * @return
         */
        private ItemDecorationUtilHolder getItemOffsets_LinearLayoutManager(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state){
            RecyclerView.Adapter recyclerAdapter = parent.getAdapter();
            Context context = view.getContext();
            if (recyclerAdapter == null || context == null){
                Log.e("ItemDecorationUtil","无法获取到 Adapter / context ");
                return this;
            }

            int position = parent.getChildAdapterPosition(view); // item position
            int count = recyclerAdapter.getItemCount();

            //计算左右和中间的空白区域总大小和平均大小
            final int leftRightSpace = dip2px(context,mLeftRightSpace);
            final int verticalCenterSpace = dip2px(context,mVerticalCenterSpace);

            //处理水平方向上空白间距
            outRect.left = leftRightSpace;
            outRect.right = leftRightSpace;

            //处理垂直方向上空白间距
            //判断是否是第一行
            boolean isFirstRow = position == 0;
            if (isFirstRow) {
                outRect.top = dip2px(context,mTopSpace);
            }else {
                outRect.top = verticalCenterSpace; // item top
            }

            //判断是否是最后一行
            boolean isEndRow = position == count - 1;
            if (isEndRow){
                outRect.bottom = dip2px(context,mBottomSpace);
            }
            return this;

        }

        public ItemDecorationUtilHolder getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state){

            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager == null){
                return this;
            }
            if (layoutManager instanceof GridLayoutManager){
                return getItemOffsets_GridLayoutManager(outRect,view,parent,state);
            }else if (layoutManager instanceof LinearLayoutManager){
                return getItemOffsets_LinearLayoutManager(outRect,view,parent,state);
            }else {
                Log.e("ItemDecorationUtil","非 GridLayoutManager ，使用了 ItemDecorationUtil");
                return this;
            }
        }

        /**
         * 算法参考网址
         * https://www.jianshu.com/p/a024c66ddb3f
         * @param outRect
         * @param view
         * @param parent
         * @param state
         * @return
         */
        private ItemDecorationUtilHolder getItemOffsets_GridLayoutManager(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            RecyclerView.Adapter recyclerAdapter = parent.getAdapter();
            Context context = view.getContext();
            if (recyclerAdapter == null || context == null){
                Log.e("ItemDecorationUtil","无法获取到 Adapter / context ");
                return this;
            }

            int position = parent.getChildAdapterPosition(view); // item position

            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();



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
            if (spanSizeLookup != null){
                currentSpanSize = spanSizeLookup.getSpanSize(position);
                preSpanSize = prePosition >= 0 ? spanSizeLookup.getSpanSize(prePosition) : -1;
            }
            //当前 item 类型，一行最多允许展示的 item 数
            int currentTypeSpanCount = allSpanCount / currentSpanSize;

            if (spanSizeLookup != null){
                //GridLayout 的换行逻辑是，先在当前行排列。排列不下，才换行。或者遇到 spanCount 不同的类型，则直接换行。
                //这里，得从当前列 n 开始。向后依次的信息 columnCount + n 个。如果 spanCount 一直和当前 item 一样，则表示不是最后一行。
                //下面，将从当前列开始，一直推算到可能的下一行。根据下一行的 spanSize 与 当前 item 的 spanSize 是否相等，来判断当前 item 所在的行是否是本类型的最后一行。
                int remainCalculateColumn = currentTypeSpanCount - typeColumn;
                int adapterItemCount = recyclerAdapter.getItemCount();
                for (int i = 1; i <= remainCalculateColumn; i++){
                    endPosition = position + i;
                    if (endPosition < adapterItemCount){
                        endSpanSize = spanSizeLookup.getSpanSize(endPosition);
                        if (currentSpanSize != endSpanSize){
                            break;
                        }
                    }else {
                        endSpanSize = -1;
                        break;
                    }
                }
            }

            //计算左右和中间的空白区域总大小和平均大小
            final int leftRightSpace = dip2px(context,mLeftRightSpace);
            final int horizontalCenterSpace = dip2px(context,mHorizontalCenterSpace);
            final int verticalCenterSpace = dip2px(context,mVerticalCenterSpace);
            final int allSpace = (currentTypeSpanCount - 1) * horizontalCenterSpace;
            final int averageSpace = allSpace / currentTypeSpanCount;
            final int halfHorizontalCenterSpace = horizontalCenterSpace / 2;
            final int leftRightAppendSpace = horizontalCenterSpace - averageSpace;
            final int endColumnIndex = currentTypeSpanCount - 1;


            //处理水平方向上空白间距
            if (typeColumn == 0){
                outRect.left = leftRightSpace;
                outRect.right = averageSpace;
            }else if (typeColumn == endColumnIndex){
                outRect.right = leftRightSpace;
                outRect.left = averageSpace;
            }else {
                //处理中间 item 的间距
                outRect.left = halfHorizontalCenterSpace;
                outRect.right = halfHorizontalCenterSpace;
                //如果是第 2 列，则需要采用补位的方式。
                if (typeColumn == 1){
                    outRect.left = leftRightAppendSpace;
                }
                //如果是倒数第 2 列，则需要采用补位的方式。
                if (typeColumn == (endColumnIndex - 1)){
                    outRect.right = leftRightAppendSpace;
                }
            }

            //处理垂直方向上空白间距
            //判断是否是某种换行模式的第一行
            boolean isFirstRow = currentSpanSize != preSpanSize;
            if (isFirstRow) {
                outRect.top = dip2px(context,mTopSpace);
            }else {
                outRect.top = verticalCenterSpace; // item top
            }

            //判断是否是某种换行模式的最后一行
            boolean isEndRow = currentSpanSize != endSpanSize;
            if (isEndRow){
                outRect.bottom = dip2px(context,mBottomSpace);
            }
            return this;
        }

        public RecyclerView.ItemDecoration build(){
            return new RecyclerView.ItemDecoration(){
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    ItemDecorationUtilHolder.this.getItemOffsets(outRect,view,parent,state);
                }
            };
        }
    }

    /**
     * 一般不建议使用此方法，而应当使用 build 的方式。
     * 除非你只有部分 item 需要设置间距
     * @return
     */
    public static ItemDecorationUtilHolder reset(){
        if (sItemDecorationUtil == null){
            sItemDecorationUtil = new ItemDecorationUtilHolder();
        }
        sItemDecorationUtil.mLeftRightSpace = 0;
        sItemDecorationUtil.mHorizontalCenterSpace = 0;
        sItemDecorationUtil.mTopSpace = 0;
        sItemDecorationUtil.mBottomSpace = 0;
        sItemDecorationUtil.mVerticalCenterSpace = 0;
        sItemDecorationUtil.mTopEqualCenter = false;
        sItemDecorationUtil.mBottomEqualCenter = false;
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
