package cn.dxy.util.recyclerview;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-01-03 17:28
 * 解决 GridLayoutManager 空隙分割问题
 * 针对的样式：一个标题 -> 一堆小标签 -> 一个 Banner -> 一堆其它小标签
 * 不可使用的样式：一堆 A 小标签 -> 一堆 B 小标签 这种小标签混排的样式
 * 可使用的样式：大标签与各种小标签相互交替
 *
 */
public class ItemDecorationUtil {
    public static ItemDecorationUtil sItemDecorationUtil = new ItemDecorationUtil();

    private int mLeftRightSpace;
    private int mHorizontalCenterSpace;
    private int mTopSpace;
    private int mBottomSpace;
    private int mVerticalCenterSpace;


    public ItemDecorationUtil setLeftRightSpace(int leftRightSpace) {
        sItemDecorationUtil.mLeftRightSpace = leftRightSpace;
        return sItemDecorationUtil;
    }

    public ItemDecorationUtil setHorizontalCenterSpace(int horizontalCenterSpace) {
        sItemDecorationUtil.mHorizontalCenterSpace = horizontalCenterSpace;
        return sItemDecorationUtil;
    }

    public ItemDecorationUtil setTopSpace(int topSpace) {
        sItemDecorationUtil.mTopSpace = topSpace;
        return sItemDecorationUtil;
    }

    public ItemDecorationUtil setBottomSpace(int bottomSpace) {
        sItemDecorationUtil.mBottomSpace = bottomSpace;
        return sItemDecorationUtil;
    }

    public ItemDecorationUtil setVerticalCenterSpace(int verticalCenterSpace) {
        sItemDecorationUtil.mVerticalCenterSpace = verticalCenterSpace;
        return sItemDecorationUtil;
    }

    public static ItemDecorationUtil reset(){
        sItemDecorationUtil.mLeftRightSpace = 0;
        sItemDecorationUtil.mHorizontalCenterSpace = 0;
        sItemDecorationUtil.mTopSpace = 0;
        sItemDecorationUtil.mBottomSpace = 0;
        sItemDecorationUtil.mVerticalCenterSpace = 0;
        return sItemDecorationUtil;
    }

    public ItemDecorationUtil getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state){

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null || ! (layoutManager instanceof GridLayoutManager)){
            Log.e("ItemDecorationUtil","非 GridLayoutManager ，使用了 ItemDecorationUtil");
            return sItemDecorationUtil;
        }

        RecyclerView.Adapter recyclerAdapter = parent.getAdapter();
        if (recyclerAdapter == null){
            Log.e("ItemDecorationUtil","无法获取到 Adapter ");
            return sItemDecorationUtil;
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
        final int leftRightSpace = sItemDecorationUtil.mLeftRightSpace;
        final int horizontalCenterSpace = sItemDecorationUtil.mHorizontalCenterSpace;
        final int verticalCenterSpace = sItemDecorationUtil.mVerticalCenterSpace;
        final int allSpace = leftRightSpace * 2 + (currentTypeSpanCount - 1) * horizontalCenterSpace;
        final int averageSpace = allSpace / currentTypeSpanCount;
        final int endColumnIndex = currentTypeSpanCount - 1;


        //处理水平方向上空白间距
        if (typeColumn == 0){
            outRect.left = leftRightSpace;
            outRect.right = averageSpace - outRect.left;
        }else if (typeColumn == endColumnIndex){
            outRect.right = leftRightSpace;
            outRect.left = averageSpace - outRect.right;
        }else {
            outRect.left = averageSpace / 2;
            outRect.right = averageSpace / 2;
        }

        //处理垂直方向上空白间距
        //判断是否是某种换行模式的第一行
        boolean isFirstRow = currentSpanSize != preSpanSize;
        if (isFirstRow) {
            outRect.top = sItemDecorationUtil.mTopSpace;
        }else {
            outRect.top = verticalCenterSpace; // item top
        }

        //判断是否是某种换行模式的最后一行
        boolean isEndRow = currentSpanSize != endSpanSize;
        if (isEndRow){
            outRect.bottom = sItemDecorationUtil.mBottomSpace;
        }
        return sItemDecorationUtil;
    }
}
