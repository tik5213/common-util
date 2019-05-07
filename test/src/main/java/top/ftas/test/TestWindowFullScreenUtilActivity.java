package top.ftas.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.ftas.test.R;
import top.ftas.util.window_statusbar.WindowUtil;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-02-25 21:51
 */
@DUnit
public class TestWindowFullScreenUtilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowUtil.beforeSetContentViewSetToDark(this);

        setContentView(R.layout.test_window_full_screen_util_activity);
    }
}
