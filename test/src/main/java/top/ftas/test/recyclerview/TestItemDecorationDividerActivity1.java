package top.ftas.test.recyclerview;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.Toast;

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
@DUnit(name = "正常分割线，最后一行有/无分割线", group = DividerGroup.class)
public class TestItemDecorationDividerActivity1 extends AppCompatActivity {
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
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
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
        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext);

        boolean showLastDividerLine = System.currentTimeMillis() % 2 == 0;

        Toast.makeText(mContext,showLastDividerLine ? "有最后一行分割线" : "无最后一行分割线",Toast.LENGTH_LONG).show();

        recycler_view.addItemDecoration(ItemDecorationUtil.buildDivider(showLastDividerLine));

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
