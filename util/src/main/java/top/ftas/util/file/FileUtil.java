package top.ftas.util.file;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-10-26 22:29
 */
public class FileUtil {
    /**
     * 将一个输入流写入到sdcard缓存中
     */
    public static String saveStreamToSdcard(Context context, InputStream inputStream, String fileName) {
        if (inputStream == null || context == null || TextUtils.isEmpty(fileName)) return "";
        File dir = context.getExternalCacheDir();
        File outFile = new File(dir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = inputStream.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }

        return outFile.getAbsolutePath();
    }
}
