package top.ftas.util.gauss;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhouwei.blurlibrary.EasyBlur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import top.ftas.util.size.NavigationBarUtil;

/**
 * 获取高斯截屏 保存到缓存文件中 返回保存的路径
 *
 * // 高斯模糊兼容库
 * maven {url "https://jitpack.io"}
 *
 * //高斯模糊
 * api 'com.github.pinguo-zhouwei:EasyBlur:v1.0.0'
 *
 * //build.xml -> android -> defaultConfig
 * //高斯模糊-使用renderscript 兼容包
 * renderscriptTargetApi 27
 * renderscriptSupportModeEnabled true
 *
 *
 */
public class GaussBackgroundUtil {
    public static final String GAUSS_BITMAP_CACHE_FILE_NAME = "getGaussBitmapCacheFile.jpg";
    public static final String GAUSS_BITMAP_CACHE_FILE_NAME_ORIGINAL = "getGaussBitmapCacheFile_original.jpg";

    /**
     * 如果有缓存，则使用缓存中的高斯图片
     */
    public static Bitmap getGaussBitmapCache(@NonNull Activity activity) {
        String gaussBitmapPath = getGaussBitmapCacheFile(activity);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap gaussBitmap = BitmapFactory.decodeFile(gaussBitmapPath, bmOptions);
        return gaussBitmap;
    }

    /**
     * 如果有缓存，则使用缓存中的高斯图片
     */
    public static String getGaussBitmapCacheFile(Activity activity) {
        return getGaussBitmapCacheFile(activity, false);
    }

    public static String getGaussBitmapCacheFile(Activity activity, boolean forceReset) {
        File file = parseGaussBitmapFilePath(activity, GAUSS_BITMAP_CACHE_FILE_NAME);
        if (file.exists() && !forceReset) {
            return file.getPath();
        } else if (forceReset) {
            try {
                File gaussOriginalFile = createAndSaveGaussBitmapToFile(activity, GAUSS_BITMAP_CACHE_FILE_NAME_ORIGINAL);
                if (gaussOriginalFile != null && gaussOriginalFile.exists()) {
                    boolean renameResult = gaussOriginalFile.renameTo(file);
                    if (renameResult) {
                        return file.getPath();
                    }
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 判断高斯模糊图片缓存是否存在
     */
    public static boolean gaussBitmapCacheFileNotExist(Context context) {
        return !parseGaussBitmapFilePath(context, GAUSS_BITMAP_CACHE_FILE_NAME).exists();
    }

    /**
     * 获取高斯截屏 保存到缓存文件中 返回保存的路径
     */
    @Nullable
    public static File createAndSaveGaussBitmapToFile(Activity activity, String fileName) {
        Bitmap bitmap = getGaussBitmap(activity);
        if (bitmap == null) return null;
        File path = saveBitmapToSDCard(activity, bitmap, fileName);
        bitmap.recycle();
        return path;
    }

    /**
     * 根据文件名解析出高斯图片的位置
     */
    public static File parseGaussBitmapFilePath(Context context, String fileName) {
        File dir = context.getExternalCacheDir();

        File file = new File(dir, fileName);
        return file;
    }

    /**
     * 保存文件到sdcard
     */
    public static File saveBitmapToSDCard(Context context, Bitmap bitmap, String fileName) {
        File file = parseGaussBitmapFilePath(context, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            fos.flush();
            fos.close();
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

        return file;
    }

    /**
     * 获取高斯截屏
     */
    public static Bitmap getGaussBitmap(Activity activity) {
        try {
            Bitmap bitmap = getScreenCapture(activity);
//            Bitmap newBitmap = rsBlur(activity,bitmap,23);
            Bitmap newBitmap = EasyBlur.with(activity)
                    .bitmap(bitmap) //要模糊的图片
                    .radius(10)//模糊半径
                    .scale(5)//指定模糊前缩小的倍数
                    .blur();

            return newBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getScreenCapture(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        boolean isCacheEnable = decorView.isDrawingCacheEnabled();

        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache(true);
        //获取当前窗口快照，相当于截屏
        Bitmap bitmap = decorView.getDrawingCache();

        int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(activity);
        Bitmap bmpScreenshot = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() - navigationBarHeight);

        decorView.destroyDrawingCache();
        decorView.setDrawingCacheEnabled(isCacheEnable);

        return bmpScreenshot;

    }

}
