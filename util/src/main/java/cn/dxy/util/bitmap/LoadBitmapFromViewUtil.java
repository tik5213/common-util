package cn.dxy.util.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-10-18 16:45
 * 利用一个xml布局文件生成出一张图片，分享给好友
 * 参考：https://www.cnblogs.com/devinzhang/archive/2012/06/05/2536848.html
 */
public class LoadBitmapFromViewUtil {

    /**
     * 从res-raw目录下获取一张Bitmap图片
     * @param context
     * @param id
     * @return
     */
    public static @Nullable
    Bitmap loadBitmapFromRawResource(@NonNull Context context, @RawRes int id) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(id);
            Bitmap rawBitmap = BitmapFactory.decodeStream(inputStream);
            return rawBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * 从 输入流中 获取一张Bitmap图片，图片创建完成后，关闭此输入流
     * @param inputStream
     * @return
     */
    public static @Nullable
    Bitmap loadBitmapAndCloseStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 根据资源 Id，加载出一个 View。并且保留原始布局的 layout 属性
     * 注意，使用 LayoutInflater 的 inflate 方法加载 xml 布局文件时，如果第二个参数（即 parent root）为null，则会丢失所有 layout 开头的属性（如 layout_width等）
     */
    public static View loadView(Context context, int viewResId) {
        if (context == null || viewResId <= 0) return null;

        FrameLayout frameLayout = new FrameLayout(context);
        View view = LayoutInflater.from(context).inflate(viewResId, frameLayout, false);
        return view;
    }

    /**
     * 从静态资源中，渲染并导出一张Bitmap图片
     * @param context
     * @param viewResId
     * @return
     */
    public static Bitmap loadBitmap(Context context, int viewResId) {
        if (context == null || viewResId <= 0) return null;
        return loadBitmap(loadView(context, viewResId));

    }

    /**
     * 从一个动态 View 中，渲染并导出一张Bitmap图片
     */
    public static Bitmap loadBitmap(View view) {
        if (view == null) return null;

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
