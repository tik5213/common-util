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

import cn.ftas.test.R;
import top.ftas.util.dialog.DialogTransparentUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-11-21 11:37
 */
public class KnowRuleFragment extends DialogFragment {

    LinearLayout ll_rule_list;


    public static KnowRuleFragment newInstance() {
        Bundle args = new Bundle();
        KnowRuleFragment fragment = new KnowRuleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_dialog_feature_know_rule_fragment, container, false);

        ll_rule_list = view.findViewById(R.id.ll_rule_list);

        view.findViewById(R.id.btn_has_known).setOnClickListener(v -> {
            dismissAllowingStateLoss();
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return DialogTransparentUtil.onCreateDialog(super.onCreateDialog(savedInstanceState), true);
    }
}
