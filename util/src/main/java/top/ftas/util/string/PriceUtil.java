package top.ftas.util.string;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 常用金额转换工具类
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-10-30 09:59
 */
public class PriceUtil {
    /**
     * 金额转换 输入分 -> 输出元 保留两位小数 前面不包含￥
     * 输入 -> 输出
     * 1 -> 0.01
     * 10 -> 0.1
     * 100 -> 1
     * 3210 -> 32.1
     * 3115 -> 31.15
     * @param priceFen 金额-单位分
     * @return 以字符串表示的元，保留两位小数，末浮点数位没有0
     */
    public static String parseFen2YuanStrWithoutZero(int priceFen) {
        BigDecimal priceBig = new BigDecimal(priceFen);
        BigDecimal ratioBig = new BigDecimal(100);
        BigDecimal priceBigYuan = priceBig.divide(ratioBig);
        DecimalFormat decimalFormat = new DecimalFormat("##################.##");
        return decimalFormat.format(priceBigYuan);
    }
    /**
     * 从字符串元 中 解析出分
     *
     String s1 = "1.7";  170
     String s2 = "0";   0
     String s3 = "0."; 0
     String s4 = "0.1"; 10
     String s5 = "0.15";  15
     String s6 = "0.01";  1
     String s7 = "0.91";  91
     String s8 = "0.00";  0
     String s9 = "4.30";  430
     String s10 = "0000";  0
     * @param priceStr 金额字符串元，两位小数
     * @return 金额-单位分
     */
    public static int parsePriceYuanStr2Fen(String priceStr){
        if (TextUtils.isEmpty(priceStr)){
            return 0;
        }

        float priceFloat;
        try {
            priceFloat = Float.parseFloat(priceStr);
        }catch (Exception e){
            Log.e("error",priceStr);
            e.printStackTrace();
            return 0;
        }
        int price = (int) (priceFloat * 100);
        return price;
    }
}
