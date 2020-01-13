package top.ftas.demo.animation.evaluate;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.ftas.demo.R;
import top.ftas.demo.animation.AnimationGroup;
import top.ftas.dunit.annotation.DUnit;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2019-05-16 20:03
 */
@DUnit(group = AnimationGroup.class)
public class EvaluateUnfoldedViewTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_unfolded_view_test_activity);

        EvaluateUnfoldedView evaluate_view_layout2 = findViewById(R.id.evaluate_view_layout2);

        evaluate_view_layout2.setAnimationDuration(1300);


        EvaluateUnfoldedView evaluate_view_layout3 = findViewById(R.id.evaluate_view_layout3);

        evaluate_view_layout3.setAnimationDuration(3300);
    }
}
