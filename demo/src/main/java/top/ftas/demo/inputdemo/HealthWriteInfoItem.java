package top.ftas.demo.inputdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ftas.demo.R;


/**
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-10-21 15:37
 */
public class HealthWriteInfoItem extends RelativeLayout {
    TextView tv_item_title;
    EditText et_item_value;
    ImageView iv_item_right_arrow;
    View v_item_bottom_line;

    public HealthWriteInfoItem(Context context) {
        this(context, null);
    }

    public HealthWriteInfoItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HealthWriteInfoItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.app_health_write_info_item, this);

        tv_item_title = findViewById(R.id.tv_item_title);
        et_item_value = findViewById(R.id.et_item_value);
        iv_item_right_arrow = findViewById(R.id.iv_item_right_arrow);
        v_item_bottom_line = findViewById(R.id.v_item_bottom_line);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HealthWriteInfoItem, defStyleAttr, 0);
        String title = a.getString(R.styleable.HealthWriteInfoItem_item_title);
        String hint = a.getString(R.styleable.HealthWriteInfoItem_item_value_hint);
        boolean editable = a.getBoolean(R.styleable.HealthWriteInfoItem_item_editable, true);
        //如果不可编辑，则默认显示向右箭头
        //如果可编辑，则默认不显示向右箭头
        boolean item_show_right_arrow = a.getBoolean(R.styleable.HealthWriteInfoItem_item_show_right_arrow, !editable);
        //底部分割线
        boolean item_show_bottom_line = a.getBoolean(R.styleable.HealthWriteInfoItem_item_show_bottom_line,true);
        int item_input_type = a.getInt(R.styleable.HealthWriteInfoItem_item_input_type, InputType.TYPE_CLASS_TEXT);
        int item_max_length = a.getInt(R.styleable.HealthWriteInfoItem_item_max_length, 50);
        int item_decimal_number = a.getInt(R.styleable.HealthWriteInfoItem_item_decimal_number,-1);

        a.recycle();

        tv_item_title.setText(title);
        et_item_value.setHint(hint);

        et_item_value.setInputType(item_input_type);

        if (!editable) {
            et_item_value.setFocusable(false);
            et_item_value.setFocusableInTouchMode(false);
        }

        //是否显示向右箭头
        iv_item_right_arrow.setVisibility(item_show_right_arrow ? VISIBLE : GONE);


        //是否显示底部分割线
        v_item_bottom_line.setVisibility(item_show_bottom_line ? VISIBLE : GONE);

        ArrayList<InputFilter> inputFilterList = new ArrayList<>();
        //最大字数过滤器
        inputFilterList.add(new InputFilter.LengthFilter(item_max_length));

        if (item_decimal_number >= 0){
            //最大精度过滤器（保留两位小数）
            inputFilterList.add(new DecimalNumberInputFilter(item_decimal_number));
        }

        //保留旧的过滤器
        InputFilter[] oldInputFilters = et_item_value.getFilters();
        if (oldInputFilters != null && oldInputFilters.length > 0) {
            for (InputFilter filter:oldInputFilters) {
                inputFilterList.add(filter);
            }
        }
        //更新过滤器
        et_item_value.setFilters(inputFilterList.toArray(new InputFilter[inputFilterList.size()]));


    }

    /**
     * 保留两位小数
     */
    public static class DecimalNumberInputFilter implements InputFilter {
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
                if(dend - index >= mDecimalNumber + 1){
                    return "";
                }
            }
            return null;
        }
    }

    public HealthWriteInfoItem setItemValueClickListener(OnClickListener onClickListener) {
        et_item_value.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 获取表单的表单的值
     *
     * @return
     */
    public String getItemValue() {
        return et_item_value.getText().toString();
    }

    /**
     * 获取表单控件
     *
     * @return
     */
    public EditText getEt_item_value() {
        return et_item_value;
    }
}
