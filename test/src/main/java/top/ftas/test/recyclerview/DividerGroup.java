package top.ftas.test.recyclerview;

import cn.ftas.test.R;
import top.ftas.dunit.group.DUnitRootGroup;
import top.ftas.util.recyclerview.ItemDecorationUtil;

/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2020-01-10 10:33
 */
public class DividerGroup extends DUnitRootGroup {
    static {
        ItemDecorationUtil.setDefaultDividerId(R.drawable.layer_list_listview_divider);
    }
}
