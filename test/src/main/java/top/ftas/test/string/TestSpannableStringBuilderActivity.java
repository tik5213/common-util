package top.ftas.test.string;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.ftas.test.R;
import top.ftas.dunit.annotation.DUnit;
import top.ftas.util.string.SpannableStringBuilder;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @desc
 * @since 2019-04-24 14:11
 */
@DUnit(group = StringGroup.class)
public class TestSpannableStringBuilderActivity extends AppCompatActivity {
    TextView tv_info;

    TextView tv_text_orange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_spannable_string_builder_activity);

        tv_info = findViewById(R.id.tv_info);

        String  call_time_period = "09:00-12:00";
        String hint_cellphone = "156****8304";

        //医生指导提示语
        String originalStr = String.format("医生将在一个工作日内的 %s 时间段，主动与你电话联系，请注意接听 %s 来电",call_time_period,hint_cellphone);
        SpannableStringBuilder
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
                .setSubStr("主动")
                .setOnClickListener(new SpannableStringBuilder.OnClickSpanStringListener() {
                    @Override
                    public void onClickSpanString(@NonNull String subString, @NonNull View widget) {
                        Toast.makeText(TestSpannableStringBuilderActivity.this,"主动",Toast.LENGTH_LONG).show();
                    }
                })
                .setColorInt(Color.parseColor("#FF00FF"))
                .into(tv_info);



        /////////////////// 分割线 /////////////////

        tv_text_orange = findViewById(R.id.tv_text_orange);

        SpannableStringBuilder builder =
                SpannableStringBuilder.builder(this, "提问医生已收到，正在整理打字中，预计1～6小时内回复。医生会给出处方建议。查看更多");

        builder.setSubStr("查看更多")
                .setColorInt(Color.parseColor("#00c792"))
                .setIsBold()
                .setOnClickListener(new SpannableStringBuilder.OnClickSpanStringListener() {
                    @Override
                    public void onClickSpanString(@NonNull String subString, @NonNull View widget) {
                        Toast.makeText(TestSpannableStringBuilderActivity.this,"link----link",Toast.LENGTH_LONG).show();
                    }
                })
                .into(tv_text_orange);
    }
}
