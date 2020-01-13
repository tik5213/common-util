package top.ftas.test.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import cn.ftas.test.R;
import top.ftas.test.UrlUtil;
import top.ftas.util.TipUtil;
import top.ftas.util.bitmap.BitmapSaveUtil;
import top.ftas.util.bitmap.DisplayBigPictureUtil;
import top.ftas.util.bitmap.LoadBitmapFromViewUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-10-19 19:02
 */
@DUnit(group = BitmapGroup.class)
public class TestLoadBitmapFromViewUtilActivity extends AppCompatActivity {
    Context mContext;
    ImageView iv_share;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(DisplayBigPictureUtil.getDisplayBigPictureViewWithActionBtn(this, false, "生成分享图片", v -> {
            //
            shareWechatMoment(v);
        }));

        iv_share = findViewById(android.R.id.icon);

    }


    public void shareWechatMoment(View view) {

        final String date = "2018-10-19";
        final String title = "今日真相题目：生命在于运动还是在于静止？";
        final String KEY_SHARE_BITMAP_NAME_JPG = "share_bitmap_name";
        Observable.just(UrlUtil.test_qr_code)
                .subscribeOn(Schedulers.io())
                .map(qrCodeUrl -> {
                    try {
                        return LoadBitmapFromViewUtil.loadBitmapAndCloseStream(new URL(qrCodeUrl).openStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .map(qrBitmap -> {
                    View rootView = LoadBitmapFromViewUtil.loadView(mContext, R.layout.daily_health_truth_share_moment_bg_layout);
                    TextView tv_health_time = rootView.findViewById(R.id.tv_health_time);
                    TextView tv_health_content = rootView.findViewById(R.id.tv_health_content);
                    ImageView iv_health_qrcode = rootView.findViewById(R.id.iv_health_qrcode);
                    ImageView root_image_view = rootView.findViewById(R.id.root_image_view);
                    tv_health_time.setText(date);
                    tv_health_content.setText(title);
                    iv_health_qrcode.setImageBitmap(qrBitmap);


                    Bitmap bgBitmap = LoadBitmapFromViewUtil.loadBitmapFromRawResource(mContext, R.raw.im_share_big_card);
                    root_image_view.setImageBitmap(bgBitmap);

                    Bitmap shareViewBitmap = LoadBitmapFromViewUtil.loadBitmap(rootView);
                    qrBitmap.recycle();

                    root_image_view.destroyDrawingCache();
                    return shareViewBitmap;
                })
                .map(shareViewBitmap -> {
                    String shareViewBitmapPath = BitmapSaveUtil.saveBitmapToSDCard(mContext, shareViewBitmap, KEY_SHARE_BITMAP_NAME_JPG);
                    shareViewBitmap.recycle();
                    return shareViewBitmapPath;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shareViewBitmapPath -> {
                    //
                    Log.e("test","shareViewBitmapPath = " + shareViewBitmapPath);

                    Bitmap bitmap = BitmapFactory.decodeFile(shareViewBitmapPath);
                    iv_share.setImageBitmap(bitmap);


                }, throwable -> {
                    //
                    TipUtil.toast("异常：",throwable.getMessage());
                });

    }
}
