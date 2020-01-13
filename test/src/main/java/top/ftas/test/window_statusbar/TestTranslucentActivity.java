package top.ftas.test.window_statusbar;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.ftas.test.R;
import top.ftas.util.window_statusbar.TranslucentActivityUtil;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-08-27 23:20
 */
@DUnit(group = WindowStatusbarGroup.class)
public class TestTranslucentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TranslucentActivityUtil.setActivityTranslucent(this);
        setContentView(R.layout.test_translucent_activity);
    }
}
