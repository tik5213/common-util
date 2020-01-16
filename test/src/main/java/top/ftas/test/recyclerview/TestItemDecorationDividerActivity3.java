package top.ftas.test.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ftas.test.R;
import top.ftas.dunit.annotation.DUnit;
import top.ftas.util.TipUtil;
import top.ftas.util.recyclerview.ItemDecorationUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-01-04 13:17
 */
@DUnit(name = "部分不绘制分割线且无间隙", group = DividerGroup.class)
public class TestItemDecorationDividerActivity3 extends AppCompatActivity {

    public static class MyBeanWrapper {
        public MyBeanWrapper(String title) {
            this.title = title;
        }

        public String title;
    }

    public static class MyWithDivider extends MyBeanWrapper{

        public MyWithDivider(String title) {
            super(title);
        }
    }

    public static class MyWithSpace extends MyBeanWrapper {
        public MyWithSpace(String title) {
            super(title);
        }
    }

    public static class MyBigItem extends MyBeanWrapper {
        public MyBigItem(String title) {
            super(title);
        }
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
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            MyBeanWrapper obj = mDataList.get(position);
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TipUtil.toast(obj.title);
                }
            });
            holder.mTextView.setText(obj.title);

            if (obj instanceof MyBigItem){
                holder.mTextView.setBackgroundColor(Color.parseColor("#ff9200"));
            }else {
                holder.mTextView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mDataList.get(position).getClass().hashCode();
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
        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);

        recycler_view.addItemDecoration(ItemDecorationUtil.buildDivider(new ItemDecorationUtil.DecorationResetConfig() {

            @Override
            public boolean resetForGetItemOffsets(@NonNull ItemDecorationUtil.ItemDecorationUtilHolder holder, int position, @NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                if (mMyBeanWrappers.get(position) instanceof MyWithSpace){
                    outRect.bottom = dip2px(100);
                    return true;
                }
                return false;
            }

            @Override
            public boolean isShowDividerLine(int position) {
                if (getItem(mMyBeanWrappers,position) instanceof MyWithDivider && getItem(mMyBeanWrappers,position + 1) instanceof MyWithDivider){
                    return true;
                }
                return false;
            }
        }));

        recycler_view.setLayoutManager(linearLayout);
        recycler_view.setAdapter(new MyAdapter(mMyBeanWrappers));


    }

    public static int dip2px(Context context, float dipValue) {
        if (context == null) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initData() {
        mMyBeanWrappers = new ArrayList<>();
        mMyBeanWrappers.add(new MyBigItem("阿尔兹海默症"));
        mMyBeanWrappers.add(new MyWithSpace("查看详情"));

        mMyBeanWrappers.add(new MyWithSpace("大卡片"));

        mMyBeanWrappers.add(new MyBeanWrapper("标题"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithSpace("更多99个医生"));

        mMyBeanWrappers.add(new MyBeanWrapper("标题"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithSpace("更多99个医生"));


        mMyBeanWrappers.add(new MyBigItem("阿尔兹海默症"));

        mMyBeanWrappers.add(new MyBeanWrapper("标题"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithDivider("王建国"));
        mMyBeanWrappers.add(new MyWithSpace("更多99个医生"));





    }
}
