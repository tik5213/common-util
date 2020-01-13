package top.ftas.test.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import cn.ftas.test.R;
import top.ftas.dunit.annotation.DUnit;
import top.ftas.util.bitmap.DisplayBigPictureUtil;
import top.ftas.util.bitmap.ShareRedPaperPictureUtil;
import top.ftas.util.window_statusbar.WindowUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-08-27 23:50
 */
@DUnit(group = BitmapGroup.class)
public class TestShareRedPaperPictureUtilActivity extends AppCompatActivity {
    ImageView iv_share;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);

        setContentView(DisplayBigPictureUtil.getDisplayBigPictureView(this, true));
        iv_share = findViewById(android.R.id.icon);

        WindowUtil.setActivityToFullScreen(this, true, false);


        ShareRedPaperPictureUtil.ShareRedPaperPictureCreateBean shareRedPaperPictureCreateBean = new ShareRedPaperPictureUtil.ShareRedPaperPictureCreateBean();

        shareRedPaperPictureCreateBean.doctorIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.s_icon_avatar_none);
        shareRedPaperPictureCreateBean.miniProgramQrCode = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.im_qr_code);
        shareRedPaperPictureCreateBean.topLetter = "你的好友天梦工作号，送你丁香医生大礼包";
        shareRedPaperPictureCreateBean.redPaperMoney = 54;
        shareRedPaperPictureCreateBean.bottomEm = "54元";
        shareRedPaperPictureCreateBean.bottomTitle = "大礼包  先领再说";
        shareRedPaperPictureCreateBean.bottomSubTitle = "长按识别二维码 领取大礼包";

        final String picPath = ShareRedPaperPictureUtil.createRedPaperBitmapAndSave(mContext, shareRedPaperPictureCreateBean);

        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
        iv_share.setImageBitmap(bitmap);


    }
}
