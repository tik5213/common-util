package top.ftas.test.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-08-19 12:15
 */
@DUnit(group = DialogGoup.class)
public class TestDialogTransparentUtilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KnowRuleFragment.newInstance().show(getSupportFragmentManager(),"KnowRuleFragment");
    }
}
