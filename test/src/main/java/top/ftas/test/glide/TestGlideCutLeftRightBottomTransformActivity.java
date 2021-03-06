package top.ftas.test.glide;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.ftas.test.R;
import top.ftas.dunit.annotation.DUnit;
import top.ftas.test.UrlUtil;
import top.ftas.util.glide.GlideCutLeftRightBottomTransform;
import top.ftas.util.glide.GlideLoadImageUtil;
import top.ftas.util.size.DisplayUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-07-19 15:51
 */
@DUnit(group = GlideGroup.class)
public class TestGlideCutLeftRightBottomTransformActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_glide_cut_left_right_bottom_transform_layout);
        imageView = findViewById(R.id.image);
        loadImage();


        ImageView image_wrap_content = findViewById(R.id.image_wrap_content);
        GlideLoadImageUtil.loadToWrapContentImageView(image_wrap_content, UrlUtil.ic_label);


        ImageView image_wrap_content2 = findViewById(R.id.image_wrap_content2);
        GlideLoadImageUtil.loadToWrapContentImageView(image_wrap_content2, UrlUtil.banner,8);
    }

    private void loadImage() {
        int px = DisplayUtil.dip2px(this, 4);
        Glide.with(this)
                .asBitmap()
                .transform(new GlideCutLeftRightBottomTransform(px))
                .load(R.raw.doctor_list)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
    }
}
