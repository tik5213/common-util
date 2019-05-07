package cn.dxy.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import cn.dxy.test.R;
import cn.dxy.util.window_statusbar.StatusBarUtil;
import cn.dxy.util.window_statusbar.WindowUtil;
import top.ftas.dunit.annotation.DUnit;

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
