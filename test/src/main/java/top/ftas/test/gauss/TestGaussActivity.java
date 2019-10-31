package top.ftas.test.gauss;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import cn.ftas.test.R;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-08-27 11:17
 */
public class TestGaussActivity extends Activity {
    ImageView iv_gauss;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_gauss_activity);

        iv_gauss = findViewById(R.id.iv_gauss);

        // Storage permission are allowed.
        String gaussBitmapPath = getIntent().getStringExtra("gaussBitmapPath");
        Bitmap gaussBitmap = BitmapFactory.decodeFile(gaussBitmapPath);
        iv_gauss.setImageBitmap(gaussBitmap);
    }


}
