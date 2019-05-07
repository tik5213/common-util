package top.ftas.util.drawbitmap;

import android.graphics.Bitmap;

public class ShareRedPaperPictureCreateBean {
    public int redPaperMoney = 0;

    public Bitmap doctorIcon;
    public Bitmap miniProgramQrCode;

    public String topLetter;
    public String bottomEm;
    public String bottomTitle;
    public String bottomSubTitle;

    public void recycle(){
        if (doctorIcon != null && !doctorIcon.isRecycled()){
            doctorIcon.recycle();
        }

        if (miniProgramQrCode != null && !miniProgramQrCode.isRecycled()){
            miniProgramQrCode.recycle();
        }
    }
}
