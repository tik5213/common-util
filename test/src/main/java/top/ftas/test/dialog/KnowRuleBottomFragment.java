package top.ftas.test.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.ftas.test.R;
import top.ftas.util.dialog.DialogSettingUtil;
import top.ftas.util.dialog.DialogTransparentUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-11-21 11:37
 */
public class KnowRuleBottomFragment extends DialogFragment {

    @Override
    public void onStart() {
        super.onStart();
        DialogSettingUtil.onStart(getDialog()).setWindowIsTransparent();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_dialog_feature_know_rule_bottom_fragment, container, false);
        view.findViewById(R.id.contentView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击了 Dialog 成功", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return DialogSettingUtil
                .onCreateDialog(super.onCreateDialog(savedInstanceState))
                .setCanceledOnTouchOutside(true)
                .setContentTransparent()
                .setNotFocusable()
                .setBottomCenter()
                .toDialog();
    }
}
