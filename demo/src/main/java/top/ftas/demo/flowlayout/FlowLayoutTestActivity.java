package top.ftas.demo.flowlayout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import cn.ftas.demo.R;
import top.ftas.dunit.annotation.DUnit;
import top.ftas.util.TipUtil;
import top.ftas.util.anim.AnimationForMinIconUtil;
import top.ftas.util.size.DisplayUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-15 11:41
 */
@DUnit
public class FlowLayoutTestActivity extends AppCompatActivity {
    FlowLayout flowLayout;
    View more_layout;
    TextView tv_more_view;
    ImageView iv_more_image;
    private boolean mIsShowMore;
    String[] list = new String[]{"标签1","标签2","标签3","标签4","被选中的标签","长长的标签5","长长的长长的长长的标签1","长长的长长的长长的长长的标签2","标签3","长长的长长的长长的长长的标签4","标签5","长长的长长的长长的长长的标签4","标签5"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_flow_layout_activity);
        flowLayout = findViewById(R.id.flowLayout);
        more_layout = findViewById(R.id.more_layout);
        tv_more_view = findViewById(R.id.tv_more_view);
        iv_more_image = findViewById(R.id.iv_more_image);


        int size = new Random().nextInt(list.length) + 1;
        String[] newList = new String[size];
        System.arraycopy(list,0,newList,0,size);
        list = newList;

        flowLayout.removeAllViews();
        flowLayout.setOnStateChangeListener(new FlowLayout.OnStateChangeListener() {
            @Override
            public void onMeasured(FlowLayout flowLayout) {
                boolean hideMoreButton = flowLayout.isAllItemVisible() && flowLayout.getCurrentLineSize() <= 2;
                more_layout.setVisibility(hideMoreButton ? View.GONE : View.VISIBLE);
            }
        });

        flowLayout.setShowLine(mIsShowMore ? Integer.MAX_VALUE : 2);
        for (String tagStr : list) {
            TextView itemView = createItemView(this, tagStr, "被选中的标签");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TipUtil.toast(tagStr);
                }
            });
            flowLayout.addView(itemView);
        }
        flowLayout.requestLayout();
        more_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsShowMore = !mIsShowMore;
                AnimationForMinIconUtil.rotateArrow(iv_more_image,mIsShowMore);
                tv_more_view.setText(mIsShowMore ? "收起" : "展开");
                flowLayout.setShowLine(mIsShowMore ? Integer.MAX_VALUE : 2);
                flowLayout.requestLayout();
            }
        });
    }


    public static TextView createItemView(Context mContext, String text,String selectedTagText) {

        TextView tagView = new TextView(mContext);

        tagView.setText(text);

        int topOrBottom = DisplayUtil.dip2px(mContext, 8);
        int leftOrRight = DisplayUtil.dip2px(mContext, 12);
        tagView.setPadding(leftOrRight, topOrBottom, leftOrRight, topOrBottom);

        tagView.setTextSize(14);
        tagView.setIncludeFontPadding(false);
        tagView.setSingleLine();
        tagView.setMaxEms(7);
        tagView.setEllipsize(TextUtils.TruncateAt.END);
        if (text.equals(selectedTagText)) {
            setSelected(mContext, tagView, true);
        } else {
            setSelected(mContext, tagView, false);
        }

        return tagView;
    }

    private static void setSelected(Context mContext, TextView tagView, boolean selected) {
        if (selected) {
            tagView.setSelected(true);
            tagView.setTextColor(Color.parseColor("#00c792"));
            tagView.setBackgroundColor(Color.parseColor("#e5f9f4"));
        } else {
            tagView.setSelected(false);
            tagView.setTextColor(Color.parseColor("#4d4d4d"));
            tagView.setBackgroundColor(Color.parseColor("#fafafa"));
        }
    }
}
