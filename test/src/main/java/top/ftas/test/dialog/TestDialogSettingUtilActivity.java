package top.ftas.test.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-19 12:15
 */
@DUnit(group = DialogGoup.class)
public class TestDialogSettingUtilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,50);
        textView.setText("我在 Activity 上，点了啊！");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"点击了 Activity",Toast.LENGTH_LONG).show();
            }
        });
        setContentView(textView);
        new KnowRuleBottomFragment().show(getSupportFragmentManager(),"KnowRuleFragment");
    }
}
