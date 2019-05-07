package cn.dxy.util.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * 将一个Drawable转换为一张Bitmap
 */
public class DrawableToBitmapUtil {

    public static Bitmap drawableToBitmap (Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context,drawableId);
        return drawableToBitmap(drawable,1,1);
    }

    public static Bitmap drawableToBitmap (Context context, int drawableId,int bitmapWidth,int bitmapHeight) {
        Drawable drawable = ContextCompat.getDrawable(context,drawableId);
        return drawableToBitmap(drawable,bitmapWidth,bitmapHeight);
    }

    public static Bitmap drawableToBitmap (Drawable drawable,int bitmapWidth,int bitmapHeight) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
