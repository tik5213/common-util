package top.ftas.util.paint;

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * android canvas drawText()文字居中 https://blog.csdn.net/zly921112/article/details/50401976
 * Canvas DrawText详解 https://blog.csdn.net/qqqq245425070/article/details/79027979
 * <p>
 * 绘制垂直居中的文字
 *
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-15 21:19
 */
public class TextPaintUtil {
    //文字向下为正，向上为负
    //top 为基线到文字顶部的距离，为负值
    //bottom 为基线到文字底部的距离，为正值
    //rect.centerY 为文字中心点的 Y 坐标
    //(- top + bottom) 为文字总高度
    //(- top + bottom) / 2 文字高的一半
    //(- top + bottom) / 2 - bottom 为文字基线到文字中心线的距离
    //rect.centerY + (- top + bottom) / 2 - bottom 为文字基线的 Y 坐标
    //简化得基线的 Y 坐标为： rect.centerY - top / 1 - bottom/2

    /**
     * 绘制垂直居中的文字
     *
     * @param rectCenterY 文字所在区别的中心线的 Y 坐标
     * @return 文字基线坐标
     */
    public static float getBaseLineY(float rectCenterY, Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return rectCenterY - fontMetrics.top / 2 - fontMetrics.bottom / 2;//基线中间点的y轴计算公式
    }


    /**
     * 绘制垂直居中的文字 - 去除因为文字间隙而带来的 baseLineY 的差值。（也就是说，由于文字上下间隙的存在，使用 baseLineY 绘制的文字也并非完全垂直居中，一般有点偏下）
     *
     * @param rectCenterY 文字所在区域的中心线 Y 坐标（你期望的最终文字垂直居中时，文字中心点的 Y 坐标）
     * @param baseLineY   文字基线的 Y 坐标，（使用 {@link TextPaintUtil#getBaseLineY(float, Paint)} 获取）
     * @param text        要绘制的目标文字（之所以要传递要绘制的目标文字，是因为每个文字的间隙并不一样）
     * @return 最终文字基线的 Y 坐标（使用此值绘制的文字将垂直居中显示）
     */
    public static float getBaseLineYTrimFontSpace(float rectCenterY, Paint paint, float baseLineY, String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, 1, bounds);
        int textHeight = bounds.height();
        float textSpace = (baseLineY - rectCenterY) - textHeight / 2.0f;
        return baseLineY - textSpace;
    }

}
