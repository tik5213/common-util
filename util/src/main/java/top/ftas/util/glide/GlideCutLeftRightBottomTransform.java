package top.ftas.util.glide;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/*
示例：
int px = DisplayUtil.dip2px(context, 4);

            GlideApp.with(context)
                    .asBitmap()
                    .transform(new CutLeftRightBottomTransform(px))
                    .placeholder(R.drawable.im_lecture_hall_default_picture)
                    .error(R.drawable.im_lecture_hall_default_picture)
                    .load(item.small_image)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageView);
 */

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-01-18 16:18
 * <p>
 * 总说明：等比缩放图片并充满图框，允许裁剪图片的左右或者下边
 * a
 * 在一个固定宽高的 ImageView 中，如果希望同时满足以下几个条件，可以使用此转换器
 * 1、图片不变形，永远等比例缩放
 * 2、图片水平方向上居中展示。左右不留任何空白。（即，允许等比例缩放并剪裁左右）
 * 3、图片垂直方向上居上展示。上边和下边不留任何空白。（即，允许等比例缩放并剪裁图片底边。不允许剪裁顶部）
 * <p>
 * GlideApp.with(mContext).asBitmap().transform(new CutLeftRightBottomTransform()).load(picPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivStartAdView);
 * <p>
 * <p>
 * 圆角参考：RoundedCorners.java TransformationUtils.java  ，圆角单位 px
 */
public class GlideCutLeftRightBottomTransform extends BitmapTransformation {
    // The version of this transformation, incremented to correct an error in a previous version.
    // See #455.
    private static final int VERSION = 1;
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.CutLeftRightBottomTransform." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private final int roundingRadius;

    public GlideCutLeftRightBottomTransform(int pxRoundingRadius) {
        this.roundingRadius = pxRoundingRadius;
    }

    public GlideCutLeftRightBottomTransform() {
        roundingRadius = 0;
    }

    // Bitmap doesn't implement equals, so == and .equals are equivalent here.
    @SuppressWarnings("PMD.CompareObjectsWithEquals")
    @Override
    protected Bitmap transform(
            @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Matrix matrix = new Matrix();

        int bitmapWidth = toTransform.getWidth();
        int bitmapHeight = toTransform.getHeight();

        float maxScale = outWidth / (float) bitmapWidth;
        float minScale = outHeight / (float) bitmapHeight;
        if (maxScale < minScale) {
            float tmpScale = maxScale;
            maxScale = minScale;
            minScale = tmpScale;
        }
        float differenceScale = maxScale / minScale;

        //如果网络图片的宽高比，和目标 ImageView 的宽高比差了 5 倍以上。说明此图片有问题。不做缩放处理。
        //原因。如果比值差得太多，图片缩放得过大。对于小内存手机，会引发 OOM 危机。

        //注意，如果仅仅是图片无法展示。很可能是硬件对图片大小有限制。不同设备可能有不同的最大值限制。只须 android:hardwareAccelerated="false"。
        // （但是，如果关闭了硬件加速，图片及动画展示可能会不够平滑，突兀）
        if (differenceScale > 5) {
            Log.e("CutTransform", "比值差得过大，可能 OOM，不进行缩放");
            return toTransform;
        }


        matrix.setScale(maxScale, maxScale);

        //传递的 outHeight 而非
        Bitmap newBitmap = Bitmap.createBitmap(toTransform, (bitmapWidth - outWidth) / 2, 0, outWidth, outHeight, matrix, true);

        //处理圆角
        if (roundingRadius > 0) {
            newBitmap = TransformationUtils.roundedCorners(pool, newBitmap, roundingRadius);
        }

        return newBitmap;
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof GlideCutLeftRightBottomTransform;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

        if (roundingRadius > 0) {
            byte[] radiusData = ByteBuffer.allocate(4).putInt(roundingRadius).array();
            messageDigest.update(radiusData);
        }
    }

}
