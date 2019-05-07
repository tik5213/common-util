package top.ftas.demo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ftas.demo.R;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-09-26 12:46
 * TabLayout 自定义ItemView 平分屏幕宽度
 */
@DUnit("TabLayout自定义View并且平分屏幕宽度")
public class TabLayoutCustomViewDemoActivity extends AppCompatActivity {
    public static class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        ArrayList<String> mData;
        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<String> data) {
            super(fm);
            mData = data;
        }
        @Override
        public Fragment getItem(int position) {
            TestFragment testFragment = new TestFragment();
            Bundle bundle = new Bundle();
            bundle.putString("dataStr",mData.get(position));
            testFragment.setArguments(bundle);
            return testFragment;
        }
        @Override
        public int getCount() {
            return mData.size();
        }
    }
    public static class TestFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            String dataStr = getArguments().getString("dataStr");
            View view = inflater.inflate(android.R.layout.simple_list_item_1,container,false);
            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(dataStr);
            return view;
        }
    }
    // 屏幕宽度（像素）
    public static  int getWindowWidth(Context context){
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout_custom_view_demo_activity);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        final ViewPager viewPager = findViewById(R.id.viewPager);
        ArrayList<String> mData = new ArrayList<>();
        mData.add("项目一");
        mData.add("项目二");
        mData.add("项目三");
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),mData));
        //获取屏幕宽度，计算所有Tab的总宽度 各个Tab的宽度
        int windowWidth = getWindowWidth(this);
        int marginWidth = 30;
        int remainWidth = windowWidth - marginWidth - marginWidth;
        int itemSize = mData.size();
        int itemWidth = remainWidth / itemSize;
        //设置TabLayout的左右间距
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) tabLayout.getLayoutParams();
        marginLayoutParams.leftMargin = marginWidth;
        marginLayoutParams.rightMargin = marginWidth;
        tabLayout.setLayoutParams(marginLayoutParams);
        //将ViewPager与TabLayout进行绑定
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //添加tabLayout点击事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        /***********************
         * 这里特别注意，addTab的调用一定要放置在 addOnPageChangeListener addOnTabSelectedListener 设置好之后
         * 否则，第一次进入的时候，不会选中你 addTab时指定的那个Tab，并且第一次进入的时候，那个Tab也无法点击。总之，就是会出bug
         ***********/

        //一个一个添加自定义Tab
        for (int i = 0; i < itemSize; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = new View(this);
            view.setBackgroundColor(i % 2 == 0 ? Color.YELLOW : Color.BLUE);
            TabLayout.LayoutParams layoutParams = new TabLayout.LayoutParams(itemWidth, TabLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(layoutParams);
            tab.setCustomView(view);
            tabLayout.addTab(tab,i == 2);
        }

    }
}
