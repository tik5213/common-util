package top.ftas.demo.inputdemo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.ftas.demo.R;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-10-21 19:57
 */
@DUnit("表单View-左边标题右边输入框限定两位小数")
public class HealthWriteInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_health_write_info_activity);
    }
}
