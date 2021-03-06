package top.ftas.test.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ftas.test.R;
import top.ftas.util.TipUtil;
import top.ftas.util.recyclerview.ItemDecorationUtil;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-01-04 13:17
 */
@DUnit(group = DividerGroup.class)
public class TestItemDecorationUtilActivity extends AppCompatActivity {
    public static final int TYPE_FULL_TITLE = 1;
    public static final int TYPE_TITLE_2_ITEM = 2;
    public static final int TYPE_TITLE_3_ITEM = 3;
    public static final int TYPE_TITLE_4_ITEM = 4;
    public static final int TYPE_TITLE_4_ITEM_2 = 42;

    public static class MyBeanWrapper {
        public MyBeanWrapper(int type, String title) {
            this.type = type;
            this.title = title;
        }

        public int type;
        public String title;
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private List<MyBeanWrapper> mDataList;

        public MyAdapter(List<MyBeanWrapper> dataList) {
            mDataList = dataList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            switch (viewType) {
                case TYPE_FULL_TITLE:
                    view.setBackgroundColor(Color.BLUE);
                    break;
                case TYPE_TITLE_2_ITEM:
                    view.setBackgroundColor(Color.GREEN);
                    break;
                case TYPE_TITLE_3_ITEM:
                    view.setBackgroundColor(Color.RED);
                    break;
                case TYPE_TITLE_4_ITEM:
                    view.setBackgroundColor(Color.YELLOW);
                    break;
                case TYPE_TITLE_4_ITEM_2:
                    view.setBackgroundColor(Color.GRAY);
                    break;
            }
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TipUtil.toast(mDataList.get(position).title);
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return mDataList.get(position).type;
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(android.R.id.text1);
        }
    }

    RecyclerView recycler_view;
    Context mContext;
    List<MyBeanWrapper> mMyBeanWrappers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.test_item_decoration_util_activity);

        initData();

        recycler_view = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 12);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mMyBeanWrappers.get(position).type) {
                    case TYPE_FULL_TITLE:
                        return 12;
                    case TYPE_TITLE_2_ITEM:
                        return 6;
                    case TYPE_TITLE_3_ITEM:
                        return 4;
                    case TYPE_TITLE_4_ITEM:
                        return 3;
                    case TYPE_TITLE_4_ITEM_2:
                        return 3;
                    default:
                        return 12;
                }
            }
        });

        recycler_view.addItemDecoration(ItemDecorationUtil.buildConfig(new ItemDecorationUtil.DecorationResetConfig() {

            @Override
            public boolean resetForGetItemOffsets(@NonNull ItemDecorationUtil.ItemDecorationUtilHolder holder, int position, @NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                MyBeanWrapper myBeanWrapper = mMyBeanWrappers.get(position);
                switch (myBeanWrapper.type){
                    case TYPE_FULL_TITLE:
                        break;
                    case TYPE_TITLE_2_ITEM:
                        holder
                                .setLeftRightSpace(40)
                                .setVerticalCenterSpace(12)
                                .setHorizontalCenterSpace(10)
                                .setBottomSpace(30);
                        break;
                    case TYPE_TITLE_3_ITEM:
                        holder
                                .setLeftRightSpace(60)
                                .setVerticalCenterSpace(12)
                                .setHorizontalCenterSpace(12)
                                .setBottomSpace(30);

                        break;
                    case TYPE_TITLE_4_ITEM:
                        holder
                                .setLeftRightSpace(40)
                                .topIsEqualCenter()
                                .setVerticalCenterSpace(10)
                                .setHorizontalCenterSpace(10)
                                .setBottomSpace(55);
                        break;
                    case TYPE_TITLE_4_ITEM_2:
                        holder
                                .setLeftRightSpace(0)
                                .topIsEqualCenter()
                                .setVerticalCenterSpace(10)
                                .setHorizontalCenterSpace(15)
                                .setBottomSpace(0);
                        break;
                }
                return false;
            }
        }));


        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setAdapter(new MyAdapter(mMyBeanWrappers));


    }

    public static int dip2px(Context context, float dipValue) {
        if (context == null) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initData() {
        mMyBeanWrappers = new ArrayList<>();

        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "所有疾病"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "避孕"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "支气管炎"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "过敏性鼻炎"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签1"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签2"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "最后一个标签"));

        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "所有科室"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));

        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "所有疾病"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_2_ITEM, "避孕"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_2_ITEM, "支气管炎"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_2_ITEM, "过敏性鼻炎"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_2_ITEM, "标签1"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_2_ITEM, "标签2"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_2_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_2_ITEM, "最后一个标签"));

        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "所有科室"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM_2, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM_2, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM_2, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM_2, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM_2, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM_2, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM_2, "神经内科"));

        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "所有科室"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));

        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "标题三"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签三"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签三"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签三"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签三"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签三"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签三"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签三"));

        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "标题四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "标题四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "标签四"));

        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "下面的会乱七八糟，原因 - 暂不支持 3 个 4 个混排的这种。计算复杂度较大。"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "下面的会乱七八糟，原因 - 暂不支持 3 个 4 个混排的这种。计算复杂度较大。"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "下面的会乱七八糟，原因 - 暂不支持 3 个 4 个混排的这种。计算复杂度较大。"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "下面的会乱七八糟，原因 - 暂不支持 3 个 4 个混排的这种。计算复杂度较大。"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "下面的会乱七八糟，原因 - 暂不支持 3 个 4 个混排的这种。计算复杂度较大。"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "下面的会乱七八糟，原因 - 暂不支持 3 个 4 个混排的这种。计算复杂度较大。"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "标题"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "标题"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_4_ITEM, "神经内科"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_TITLE_3_ITEM, "标签3"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "标题"));
        mMyBeanWrappers.add(new MyBeanWrapper(TYPE_FULL_TITLE, "标题"));
    }
}
