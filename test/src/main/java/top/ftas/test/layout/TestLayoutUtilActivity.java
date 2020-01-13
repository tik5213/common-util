package top.ftas.test.layout;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.ftas.test.R;
import top.ftas.dunit.annotation.DUnit;
import top.ftas.test.UrlUtil;
import top.ftas.util.layout.LayoutUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-07 14:23
 */
@DUnit(group = LayoutGroup.class)
public class TestLayoutUtilActivity extends AppCompatActivity {
    ImageView iv_banner;
    ImageView iv_banner_2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout_util_activity);

        iv_banner = findViewById(R.id.iv_banner);
        iv_banner_2 = findViewById(R.id.iv_banner_2);

        LayoutUtil.setImageViewLayoutVisible(iv_banner,641,269,80);
        Glide.with(iv_banner)
                .load(UrlUtil.banner)
                .into(iv_banner);

        LayoutUtil.setImageViewLayoutVisible(iv_banner_2,430,430,80);
        Glide.with(iv_banner_2)
                .load(UrlUtil.test_qr_code)
                .into(iv_banner_2);

    }
}
