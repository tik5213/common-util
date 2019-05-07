package top.ftas.test.string;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.widget.TextView;

import cn.ftas.test.R;
import top.ftas.util.string.SpannableStringBuilder;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @desc
 * @since 2019-04-24 14:11
 */
@DUnit
public class TestSpannableStringBuilderActivity extends AppCompatActivity {
    TextView tv_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_spannable_string_builder_activity);

        tv_info = findViewById(R.id.tv_info);

        String  call_time_period = "09:00-12:00";
        String hint_cellphone = "156****8304";

        //医生指导提示语
        String originalStr = String.format("医生将在一个工作日内的 %s 时间段，主动与你电话联系，请注意接听 %s 来电",call_time_period,hint_cellphone);
        SpannableString tipSpan = SpannableStringBuilder
                .builder(this,originalStr)
                .setSubStr(call_time_period)
                .setColorInt(Color.parseColor("#666666"))
                .setIsBold()
                .setSubStr(hint_cellphone)
                .setColorInt(Color.parseColor("#666666"))
                .setIsBold()
                .setTextSize(30)
                .setSubStr("请注意")
                .setStrikethrough()
                .setIsBold()
                .build();

        tv_info.setText(tipSpan);

    }
}
