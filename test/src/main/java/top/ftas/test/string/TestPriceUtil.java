package top.ftas.test.string;

import top.ftas.dunit.annotation.DUnit;
import top.ftas.dunit.core.AbstractDisplayUnit;
import top.ftas.util.string.PriceUtil;

/**
 * Created by tik on 2019-10-30.
 */
@DUnit(group = StringGroup.class)
public class TestPriceUtil extends AbstractDisplayUnit {
    public void parseFen2YuanStrWithoutZero(int priceFen) {
        mMessageHelper.appendLine("" + priceFen + " -> " + PriceUtil.parseFen2YuanStrWithoutZero(priceFen));
    }

    public void parsePriceYuanStr2Fen(String priceStr) {
        mMessageHelper.appendLine(priceStr + " -> " + PriceUtil.parsePriceYuanStr2Fen(priceStr));
    }

    @Override
    public void callUnit() {
        parseFen2YuanStrWithoutZero(1);
        parseFen2YuanStrWithoutZero(10);
        parseFen2YuanStrWithoutZero(100);
        parseFen2YuanStrWithoutZero(3210);
        parseFen2YuanStrWithoutZero(3115);

        parsePriceYuanStr2Fen("1.7");
        parsePriceYuanStr2Fen("0");
        parsePriceYuanStr2Fen("0.");
        parsePriceYuanStr2Fen("0.1");
        parsePriceYuanStr2Fen("0.15");
        parsePriceYuanStr2Fen("0.01");
        parsePriceYuanStr2Fen("0.91");
        parsePriceYuanStr2Fen("0.00");
        parsePriceYuanStr2Fen("4.30");
        parsePriceYuanStr2Fen("0000");

    }
}
