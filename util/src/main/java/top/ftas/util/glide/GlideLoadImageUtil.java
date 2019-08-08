package top.ftas.util.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-08 17:48
 */
public class GlideLoadImageUtil {

    /**
     * 加载一个图片到宽和高都是 wrap_content 的 ImageView
     */
    public static void loadToWrapContentImageView(final ImageView imageView, String picUrl) {
        loadToWrapContentImageView(imageView, picUrl, 0);
    }

    /**
     * 加载一个图片到宽和高都是 wrap_content 的 ImageView
     */
    public static void loadToWrapContentImageView(final ImageView imageView, String picUrl, int dp_roundingRadius) {
        if (imageView == null) return;
        RequestBuilder<Bitmap> requestBuilder = Glide.with(imageView.getContext())
                .asBitmap()
                .load(picUrl);
        if (dp_roundingRadius > 0) {
            int px = dip2px(imageView.getContext(), dp_roundingRadius);
            requestBuilder = requestBuilder.transform(new RoundedCorners(px));
        }
        requestBuilder
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                        layoutParams.width = resource.getWidth();
                        layoutParams.height = resource.getHeight();
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    private static int dip2px(Context context, float dipValue) {
        if (context == null) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
