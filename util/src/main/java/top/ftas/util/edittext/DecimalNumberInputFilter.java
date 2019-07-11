package top.ftas.util.edittext;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-04-22 21:02
 */
public class DecimalNumberInputFilter implements InputFilter {
    private int mDecimalNumber;

    public DecimalNumberInputFilter(int decimalNumber) {
        mDecimalNumber = decimalNumber;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        String lastInputContent = dest.toString();

        if (source.equals(".") && lastInputContent.length() == 0) {
            return "0.";
        }
        if (lastInputContent.contains(".")) {
            int index = lastInputContent.indexOf(".");
            if (dend - index >= mDecimalNumber + 1) {
                return "";
            }
        }
        return null;
    }
}
