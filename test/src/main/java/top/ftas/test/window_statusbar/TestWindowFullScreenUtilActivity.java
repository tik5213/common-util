package top.ftas.test.window_statusbar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.ftas.test.R;
import top.ftas.util.window_statusbar.WindowUtil;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-02-25 21:51
 */
@DUnit(group = WindowStatusbarGroup.class)
public class TestWindowFullScreenUtilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowUtil.beforeSetContentViewSetToDark(this);

        setContentView(R.layout.test_window_full_screen_util_activity);
    }
}
