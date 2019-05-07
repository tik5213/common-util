package cn.dxy.util.drawbitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.dxy.util.R;


/**
 * 创建一张五星评价分享红包的图片
 * 最佳参考网址： https://zhuanlan.zhihu.com/p/28077890
 */
public class ShareRedPaperPictureUtil {

    public static String saveBitmapToSDCard(Context context, Bitmap bitmap, String fileName) {

        File dir = context.getExternalCacheDir();

        File file = new File(dir, fileName);

        doSaveBitmapToSdcard(context, bitmap, file);

        return file.getPath();
    }

    private static void doSaveBitmapToSdcard(Context context, Bitmap bitmap, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.flush();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static @Nullable
    String createRedPaperBitmapAndSave(Context context, ShareRedPaperPictureCreateBean pictureCreateBean) {
        Bitmap bitmap = createRedPaperBitmap(context, pictureCreateBean);
        if (bitmap != null){
            String picPath = saveRedPaperBitmap(context,bitmap);
            bitmap.recycle();
            return picPath;
        }else {
            return "";
        }
    }


    public static @Nullable
    String saveRedPaperBitmap(Context context, Bitmap bitmap) {
        if (bitmap == null){
            return null;
        }
        return saveBitmapToSDCard(context, bitmap, "share_red_paper_picture.png");
    }

    public static @Nullable
    Bitmap createRedPaperBitmap(Context context, ShareRedPaperPictureCreateBean pictureCreateBean) {
        if (pictureCreateBean.doctorIcon == null || pictureCreateBean.miniProgramQrCode == null) return null;

        //背景图
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap im_bg_share_coupon = BitmapFactory.decodeResource(context.getResources(), R.raw.im_bg_share_coupon,options);

        int bgWidth = im_bg_share_coupon.getWidth();
        int half_BgWidth = bgWidth / 2;
        int bgHeight = im_bg_share_coupon.getHeight();

        //创建一个以 im_bg_share_coupon 为背景的画布
        Canvas canvas = new Canvas(im_bg_share_coupon);

        /***计算全局比例尺***/
        //比例尺规则 = 画布总宽度 / UI界面总宽度
        float scale = canvas.getWidth() / 375f;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        /***绘制-医生头像***/
        //生成圆形医生头像
        int doctorIconWidth = (int) (45 * scale);
        int doctorIconHeight = doctorIconWidth;
        int half_doctorHeight = doctorIconHeight / 2;
        int doctorIconRadius =half_doctorHeight;
        int doctorIconLeft = (bgWidth - doctorIconWidth) / 2;
        float doctorIconCenterY = 52 * scale;
        float doctorIconTop = doctorIconCenterY - half_doctorHeight;
        Bitmap circleDoctorIcon = circleBitmapByShader(pictureCreateBean.doctorIcon,doctorIconWidth,doctorIconRadius);
        canvas.drawBitmap(circleDoctorIcon, doctorIconLeft, doctorIconTop, paint);
        circleDoctorIcon.recycle();


        /***绘制-分享红包者的名字***/
        if (!TextUtils.isEmpty(pictureCreateBean.topLetter)){
            //设置 14号 文字
            setTextRegular(paint);
            resetPaintTextStyle(paint, Paint.Align.CENTER,"#333333",14 * scale);

            Paint.FontMetrics shareMsgFontMetrics = paint.getFontMetrics();
            //当从顶部开始绘绘图时，baseLine的Y坐标
            float shareMsgTextBaseLineY = - shareMsgFontMetrics.top ;
            //从医生头像位置，向下移动 5dp，绘制 你的好友王璐璐，送你丁香医生大礼包
            shareMsgTextBaseLineY += doctorIconCenterY + half_doctorHeight + 5 * scale;
            canvas.drawText(pictureCreateBean.topLetter,half_BgWidth,shareMsgTextBaseLineY,paint);
        }


        /***************************绘制双文本居中-start***************************/
        setTextSemibold(paint);
        /***计算-顶部的金额￥号***/
        //设置 29号 文字
        resetPaintTextStyle(paint, Paint.Align.LEFT,"#ff5b5b",29 * scale);

        PointF topMoneyPointPointF = new PointF();
        Rect topMoneyPointRect = calculateTextBaseLineY("￥",paint,28.5f * scale,34 * scale,topMoneyPointPointF);
        topMoneyPointPointF.y += 108.5f * scale;


        /***计算-顶部的金额数值***/
        //设置 39.9号 文字
        resetPaintTextStyle(paint, Paint.Align.LEFT,"#ff5b5b",39.9f * scale);
        //由于设计稿上面写的是54，那么这里根据 54 这个数值来计算出文字的左右间隙，并作为这个字号的标准间隙
        PointF base_topMoneyValuePointF = new PointF();
        calculateTextBaseLineY("54",paint,48 * scale,0,base_topMoneyValuePointF);

        PointF topMoneyValuePointF = new PointF();
        String redPaperMoneyStr = pictureCreateBean.redPaperMoney + "";
        Rect topMoneyValueRect = calculateTextBaseLineY(redPaperMoneyStr,paint,0,38 * scale,topMoneyValuePointF);
        // 106.5 为Zeplin上测试出来的文字到顶部的距离
        topMoneyValuePointF.y += 106.5f * scale;

        /***计算-两个文本居中，右边的金额可以任意变化***/
        //计算两个文本的总实际显示宽度
        float topAllTextDisplayWidthNoSpace = topMoneyPointRect.width() + topMoneyValueRect.width();
        //计算两个文本的总间隙 = 左边文字右侧间隙 + 右边文字左侧间隙
        float topAllTextSelfSpace = topMoneyPointPointF.x + base_topMoneyValuePointF.x;
        //计算两个文本之间的公共间隙 5.1 为Zeplin上测试出来的两个文字的间隙
        float topAllTextPublicSpace = 5.1f * scale;
        //统计两个文字实际显示的宽度（包含各自的文字间隙和公共间隙）
        float topAllTextDisplayWidthWithSpace = topAllTextDisplayWidthNoSpace + topAllTextSelfSpace  + topAllTextPublicSpace;

        //计算左边第一个文字实际显示的点到最左侧的距离
        float topLeftTextDisplayX = ( bgWidth - topAllTextDisplayWidthWithSpace ) / 2;
        //计算左边第一个文字的左侧绘制点
        float topLeftTextDrawX = topLeftTextDisplayX - topMoneyPointPointF.x;

        //计算右边第一个文字实际显示的点到最左侧的距离
        float topRightTextDrawX = topLeftTextDisplayX + topMoneyPointRect.width() + topMoneyPointPointF.x + topAllTextPublicSpace;


        /***绘制-顶部的金额￥号***/
        resetPaintTextStyle(paint, Paint.Align.LEFT,"#ff5b5b",29 * scale);
        canvas.drawText("￥",topLeftTextDrawX,topMoneyPointPointF.y,paint);

        /***绘制-顶部的金额数值***/
        resetPaintTextStyle(paint, Paint.Align.LEFT,"#ff5b5b",39.9f * scale);
        canvas.drawText(redPaperMoneyStr,topRightTextDrawX,topMoneyValuePointF.y,paint);
        /***************************绘制双文本居中-end***************************/


        /***********坐标平移到左下角**********/
        canvas.translate(0,bgHeight);

        /***绘制-底部的金额数值***/
        setTextSemibold(paint);
        float bottomMoneyStrLeft = 15 * scale;
        float bottomMoneyStrUseWidth = 0;
        if (!TextUtils.isEmpty(pictureCreateBean.bottomEm)){
            String bottomMoneyStr = pictureCreateBean.bottomEm;
            //设置 25号字
            resetPaintTextStyle(paint, Paint.Align.LEFT,"#ff5b5b",25f * scale);
            drawBottomText(canvas,paint,bottomMoneyStrLeft,53 * scale,bottomMoneyStr);
            //计算底部左边金额数据占用的宽度
            bottomMoneyStrUseWidth = paint.measureText(bottomMoneyStr);
        }


        /***绘制-底部金额结束语***/
        if (!TextUtils.isEmpty(pictureCreateBean.bottomTitle)){
            //设置18号字
            resetPaintTextStyle(paint, Paint.Align.LEFT,"#333333",18f * scale);
            //计算底部金额结束语到左侧的距离
            float bottomMoneyEndStrLeft = bottomMoneyStrLeft + bottomMoneyStrUseWidth + 2 * scale;
            drawBottomText(canvas,paint,bottomMoneyEndStrLeft,54 * scale,pictureCreateBean.bottomTitle);
        }

        /***绘制-底部长按识别二维码提示语***/
        if (!TextUtils.isEmpty(pictureCreateBean.bottomSubTitle)){
            setTextRegular(paint);
            //设置12号字
            resetPaintTextStyle(paint, Paint.Align.LEFT,"#999999",12f * scale);
            drawBottomText(canvas,paint,15 * scale,31 * scale,pictureCreateBean.bottomSubTitle);
        }

        /***绘制-二维码小程序***/
        float miniProgramQrCodeWidth = 80 * scale;
        float miniProgramQrCodeHeight = miniProgramQrCodeWidth;
        float miniProgramQrCodeLeft = bgWidth - 15 * scale - miniProgramQrCodeWidth;
        float miniProgramQrCodeTop = - 10 * scale -  miniProgramQrCodeHeight;

        /***********坐标平移到小程序码的左上角**********/
        float translateMiniProgramQrCodeDx = bgWidth - miniProgramQrCodeWidth - 15 * scale;
        float translateMiniProgramQrCodeDy = - miniProgramQrCodeHeight - 10 * scale;

        canvas.translate(translateMiniProgramQrCodeDx,translateMiniProgramQrCodeDy);
        Matrix miniProgramQrCodeMatrix = zoomImgMatrix(pictureCreateBean.miniProgramQrCode,miniProgramQrCodeWidth,miniProgramQrCodeHeight);
        canvas.drawBitmap(pictureCreateBean.miniProgramQrCode,miniProgramQrCodeMatrix,paint);

        return im_bg_share_coupon;
    }

    public static void setTextSemibold(Paint paint){
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    public static void setTextRegular(Paint paint){
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
    }

    /*

        Paint.FontMetrics topMoneyFontMetrics = paint.getFontMetrics();
        float topMoneyPointTextX = 142.5f * scale;
        float topMoneyPointTextBaseLineY = - topMoneyFontMetrics.top;
        topMoneyPointTextBaseLineY += shareMsgTextBaseLineY + shareMsgFontMetrics.bottom + 13 * scale;
        canvas.drawText("￥",topMoneyPointTextX,topMoneyPointTextBaseLineY,paint);

        上面，网上流行的这一套计算方式会因为程序文字默认的padding值和设计师的不一样，而产生一些位置偏差

     */
    /**
     * 以左上角为坐标原点，计算文字基线的Y坐标，返回值可传递给drawText函数，绘制出来的文字和设计师给出的文字间隙一样。
     * pointF.y 是可以直接使用 ， pointF.x 表示的是文字的的左侧（或右侧）间隙，如果要以原点开始写字，一般取负，即 - pointF.x
     * 特别注意，记得进行坐标转换，将返回值加上在Zeplin.app上测试出来的文字到页面顶部的距离才是文字实际的基线坐标
     * 下面这一套自创的计算方式解决了设计师文字padding与程序内部文字padding不一样的问题
     * @param str
     * @param paint
     * @param textViewFullHeight
     * @return
     */
    public static Rect calculateTextBaseLineY(String str, Paint paint, float textViewFullWidth, float textViewFullHeight, PointF pointF){
        //下面获取文字的实际显示区域高度
        Rect topMoneyValueTextBounds = new Rect();
        paint.getTextBounds(str,0,str.length(),topMoneyValueTextBounds);
        float topMoneyValueTextDisplayHeight = topMoneyValueTextBounds.height();
        float textDisplayWidth = topMoneyValueTextBounds.width();

        //根据设计稿上，测量出来的文本框的Height属性，计算出文字的上两空隙
        //文字的多余空隙 = 文本框的总高度 - 文字内容实际显示的高度
        float half_vertical_TextSpace = (textViewFullHeight - topMoneyValueTextDisplayHeight) / 2;
        float half_horizontal_TextSpace = (textViewFullWidth - textDisplayWidth) / 2;

        pointF.y = topMoneyValueTextDisplayHeight + half_vertical_TextSpace;
        pointF.x = half_horizontal_TextSpace;
        return topMoneyValueTextBounds;
    }


    /**
     * 缩放图片
     */
    public static Matrix zoomImgMatrix(Bitmap bm, float newWidth , float newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return matrix;
    }

    /**
     * 利用BitmapShader绘制底部圆角图片
     *
     * @param bitmap
     *              待处理图片
     * @param edgeWidth
     *              正方形控件大小
     * @param radius
     *              圆角半径大小
     * @return
     *              结果图片
     */
    public static Bitmap circleBitmapByShader(Bitmap bitmap, int edgeWidth, int radius) {
        if(bitmap == null) {
            throw new NullPointerException("Bitmap can't be null");
        }

        float btWidth = bitmap.getWidth();
        float btHeight = bitmap.getHeight();
        // 水平方向开始裁剪的位置
        float btWidthCutSite = 0;
        // 竖直方向开始裁剪的位置
        float btHeightCutSite = 0;
        // 裁剪成正方形图片的边长，未拉伸缩放
        float squareWidth = 0f;
        if(btWidth > btHeight) { // 如果矩形宽度大于高度
            btWidthCutSite = (btWidth - btHeight) / 2f;
            squareWidth = btHeight;
        } else { // 如果矩形宽度不大于高度
            btHeightCutSite = (btHeight - btWidth) / 2f;
            squareWidth = btWidth;
        }

        // 设置拉伸缩放比
        float scale = edgeWidth * 1.0f / squareWidth;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);

        // 将矩形图片裁剪成正方形并拉伸缩放到控件大小
        Bitmap squareBt = Bitmap.createBitmap(bitmap, (int)btWidthCutSite, (int)btHeightCutSite, (int)squareWidth, (int)squareWidth, matrix, true);

        // 初始化绘制纹理图
        BitmapShader bitmapShader = new BitmapShader(squareBt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        // 初始化目标bitmap
        Bitmap targetBitmap = Bitmap.createBitmap(edgeWidth, edgeWidth, Bitmap.Config.ARGB_8888);

        // 初始化目标画布
        Canvas targetCanvas = new Canvas(targetBitmap);

        // 初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        // 利用画笔绘制圆形图
        targetCanvas.drawRoundRect(new RectF(0, 0, edgeWidth, edgeWidth), radius, radius, paint);

        return targetBitmap;
    }


    /**
     * 缩放图片
     */
    public static Bitmap zoomImg(Bitmap bm, float newWidth , float newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newBitmap;
    }

    /*
     * 调用这个函数的前提是将坐标原点从初始位置移动到了左下角。这个时候，Y值全部变成了负值。不过传入的时候，只需要传递目标对象到底部的绝对值就好了
     */
    public static void drawBottomText(Canvas canvas, Paint paint, float left, float bottom, String strMsg){
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
        float baseLineY = - paintFontMetrics.bottom;
        baseLineY -= bottom;
        canvas.drawText(strMsg,left,baseLineY,paint);
    }

    /*
     * 调用这个函数的前提是将坐标原点从初始位置移动到了左下角。这个时候，Y值全部变成了负值。不过传入的时候，只需要传递目标对象到底部的绝对值就好了
     */
    public static void drawBottomTextUseFontMetrics(Canvas canvas, Paint paint, float scale, float dp_left, float dp_bottom, String strMsg){
        dp_bottom = dp_bottom * scale;
        dp_left = dp_left * scale;
        Paint.FontMetrics paintFontMetrics = paint.getFontMetrics();
        float baseLineY = - paintFontMetrics.bottom;
        baseLineY -= dp_bottom;
        canvas.drawText(strMsg,dp_left,baseLineY,paint);
    }

    /**
     * 重置画笔
     * @param paint
     * @param align
     * @param colorString
     * @param textSize
     */
    public static void resetPaintTextStyle(Paint paint, Paint.Align align, String colorString, float textSize){
        //该方法即为设置基线上那个点究竟是left,center,还是right
        paint.setTextAlign(align);
        paint.setColor(Color.parseColor(colorString));
        paint.setTextSize(textSize);
    }

}
