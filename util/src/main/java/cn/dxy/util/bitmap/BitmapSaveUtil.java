package cn.dxy.util.bitmap;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-10-18 17:48
 */
public class BitmapSaveUtil {
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
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
}
