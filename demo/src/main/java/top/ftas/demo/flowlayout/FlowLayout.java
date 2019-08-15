package top.ftas.demo.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.ftas.demo.R;


/**
 * Created by ybf326 on 17/4/19.
 */

public class FlowLayout extends ViewGroup{

	/**
	 * 存储行的集合，管理行
	 */
	private List<Line> mLines = new ArrayList<>();

	/**
	 * 水平和竖起的间距
	 */
	private float vertical_space;
	private float horizontal_space;

	private boolean split_space;

	/**
	 * 当前行
	 */
	private Line mCurrentLine;

	/**
	 * 行的最大宽度，除去边距的宽度
	 */
	private int mMaxWidth;

	private int mShowLine = Integer.MAX_VALUE;

	private int mCurrentVisibleLastItemIndex;

	private OnStateChangeListener mOnStateChangeListener;

	public FlowLayout(Context context){
		this(context,null);
	}

	public FlowLayout(Context context, AttributeSet attrs){
		super(context,attrs);

		//获取自定义属性
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
		horizontal_space = array.getDimension(R.styleable.FlowLayout_width_space,0);
		vertical_space = array.getDimension(R.styleable.FlowLayout_height_space,0);
		split_space = array.getBoolean(R.styleable.FlowLayout_split_space,false);
		array.recycle();
	}

	private void addViewToCurrentLine(int index,View view){
		mCurrentLine.addView(view);
		mCurrentVisibleLastItemIndex = index;
	}

	public int getCurrentVisibleItemCount() {
		return mCurrentVisibleLastItemIndex + 1;
	}

	public boolean isAllItemVisible(){
		return getCurrentVisibleItemCount() == getChildCount();
	}

	public int getCurrentLineSize(){
		return mLines.size();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mCurrentVisibleLastItemIndex = 0;
		//每次测量之前都清空集合
		mLines.clear();
		mCurrentLine = null;

		//获取总宽度
		int width = MeasureSpec.getSize(widthMeasureSpec);
		//计算最大的宽度
		mMaxWidth = width - getPaddingLeft() - getPaddingRight();

		//测量子视图
		//遍历获取子视图
		int childCount = getChildCount();
		int line = 0;
		for (int i = 0;i < childCount; i++){
			View childView = getChildAt(i);
			//测量子视图
			measureChild(childView,widthMeasureSpec,heightMeasureSpec);

			//测量完需要将子视图添加到行
			if (mCurrentLine == null){
				line = 1;
				if (line > mShowLine){
					break;
				}
				//初次添加第一个子视图
				mCurrentLine = new Line(mMaxWidth,horizontal_space);

				//添加子视图
				addViewToCurrentLine(i,childView);
				//添加行
				mLines.add(mCurrentLine);
			}else {
				//行中有子视图的时候，判断能否继续添加
				if (mCurrentLine.canAddView(childView)){
					//继续往该行里添加
					addViewToCurrentLine(i,childView);
				}else {
					line++;
					if (line > mShowLine){
						break;
					}
					//添加到下一行
					mCurrentLine = new Line(mMaxWidth,horizontal_space);
					addViewToCurrentLine(i,childView);
					mLines.add(mCurrentLine);
				}
			}
		}


		//测量下自己
		//测量自己只需要计算高度，宽度肯定会被填充满的
		int height = getPaddingTop() + getPaddingBottom();
		for (int i = 0;i < mLines.size(); i++){
			//所有行的调试
			height = height + mLines.get(i).mHeight;
		}

		//所有竖直的间距
		height = (int) (height + (mLines.size() - 1) * vertical_space);

		//测量
		setMeasuredDimension(width,height);

		if (mOnStateChangeListener != null) {
			mOnStateChangeListener.onMeasured(this);
		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		//这里只负责高度的位置，具体的宽度和子视图位置让具体的行去管理
		l = getPaddingLeft();
		t = getPaddingTop();

		for (int i = 0; i < mLines.size(); i++){
			//获取行
			Line line = mLines.get(i);
			//管理
			line.layout(t,l);

			//更新高度
			t = t + line.mHeight;
			if (i != mLines.size() - 1){
				//不是最后一行就添加行间距
				t += vertical_space;
			}
		}
	}

	public interface OnStateChangeListener {
	    void onMeasured(FlowLayout flowLayout);
	}

	public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
		mOnStateChangeListener = onStateChangeListener;
	}

	public void setShowLine(int showLine) {
		mShowLine = showLine;
	}

	/**
	 * 行管理类，管理每一行的视图
	 */
	public class Line {
		//定义一个行的集合来存放子View
		private List<View> mViews = new ArrayList<>();
		//行的最大宽度
		private int mMaxWidth;
		//行中已经使用的宽度
		private int mUsedWidth;
		//行的高度
		private int mHeight;
		//子视图之间的距离
		private float mSpace;

		//通过构造器初始化最大宽度和边距
		public Line(int maxWidth,float horizontalSpace){
			mMaxWidth = maxWidth;
			mSpace = horizontalSpace;
		}

		/**
		 * 往集合里添加子视图
		 * @param view
		 */
		public void addView(View view){
			int childWidth = view.getMeasuredWidth();
			int childHeight = view.getMeasuredHeight();

			//更新行的使用宽度和高度
			if (mViews.size() == 0){
				//集合里没有子视图时
				if (childWidth > mMaxWidth){
					mUsedWidth = mMaxWidth;
					mHeight = childHeight;
				}else {
					mUsedWidth = childWidth;
					mHeight = childHeight;
				}
			}else {
				//集合中有子视图时
				mUsedWidth = (int) (mUsedWidth + childWidth + mSpace);
				mHeight = childHeight > mHeight ? childHeight : mHeight;
			}

			//添加子视图到集合中
			mViews.add(view);
		}

		/**
		 * 判断当前的行是否能添加子视图
		 */
		public boolean canAddView(View view){
			//集合里没有数据可以添加
			if (mViews.size() == 0){
				return true;
			}

			if (view.getMeasuredWidth() > (mMaxWidth - mUsedWidth - mSpace)){
				return false;
			}

			//默认可以添加
			return true;
		}

		/**
		 * 指定子视图的显示位置
		 * @param t 距顶部的距离
		 * @param l 距左边的距离
		 */
		public void layout(int t,int l){
			int avg = 0;
			if (split_space){
				//平分剩下的空间
				avg = (mMaxWidth - mUsedWidth) / mViews.size();
			}

			//循环指定子视图位置
			for (View view : mViews){
				//获取宽高
				int measuredWidth = view.getMeasuredWidth();
				int measuredHeight = view.getMeasuredHeight();

				if (split_space){
					//重新测量
					view.measure(MeasureSpec.makeMeasureSpec(measuredWidth + avg,MeasureSpec.EXACTLY),
							MeasureSpec.makeMeasureSpec(measuredHeight,MeasureSpec.EXACTLY)
					);
					//重新获取宽度值
					measuredWidth = view.getMeasuredWidth();
				}

				int top = t;
				int left = l;
				int right = measuredWidth + left;
				int bottom = measuredHeight + top;
				//指定位置
				view.layout(left,top,right,bottom);

				//更新数据
				l = (int) (l + measuredWidth + mSpace);
			}
		}




	}
}
