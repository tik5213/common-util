package cn.dxy.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import cn.dxy.util.bitmap.DisplayBigPictureUtil;
import cn.dxy.util.drawbitmap.ShareRedPaperPictureCreateBean;
import cn.dxy.util.drawbitmap.ShareRedPaperPictureUtil;
import cn.dxy.util.window_statusbar.WindowUtil;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-08-27 23:50
 */
@DUnit
public class TestShareRedPaperPictureUtilActivity extends AppCompatActivity{
    ImageView iv_share;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);

        setContentView(DisplayBigPictureUtil.getDisplayBigPictureView(this,true));
        iv_share = findViewById(android.R.id.icon);

        WindowUtil.setActivityToFullScreen(this,true,false);


        ShareRedPaperPictureCreateBean shareRedPaperPictureCreateBean = new ShareRedPaperPictureCreateBean();

        shareRedPaperPictureCreateBean.doctorIcon = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.s_icon_avatar_none);
        shareRedPaperPictureCreateBean.miniProgramQrCode = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.im_qr_code);
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
