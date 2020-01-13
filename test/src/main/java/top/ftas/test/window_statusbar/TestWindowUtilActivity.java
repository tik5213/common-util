package top.ftas.test.window_statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import cn.ftas.test.R;
import top.ftas.util.window_statusbar.StatusBarUtil;
import top.ftas.util.window_statusbar.WindowUtil;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-08-27 12:44
 */
@DUnit(group = WindowStatusbarGroup.class)
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
