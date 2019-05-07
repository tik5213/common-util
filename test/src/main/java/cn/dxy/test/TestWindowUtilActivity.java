package cn.dxy.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.dxy.test.R;
import cn.dxy.util.window_statusbar.StatusBarUtil;
import cn.dxy.util.window_statusbar.WindowUtil;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-08-27 12:44
 */
@DUnit
public class TestWindowUtilActivity extends AppCompatActivity {
    private Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mActivity = this;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_windowutil_activity);
        WindowUtil.setActivityToFullScreenWithInvisibleStatusBar(mActivity);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void changeShowFullScreen(View v) {
        WindowUtil.showActivityStatusBar(mActivity);
        StatusBarUtil.setStatusBarColorWithColorValue(this, Color.BLUE,false);
    }
}
