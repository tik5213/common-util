package top.ftas.demo.inputdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ftas.demo.R;


/**
 * 表单输入框
 * @author tik5213 (yangb@dxy.cn)
 * @since 2018-10-21 15:37
 */
public class FormInputText extends RelativeLayout {
    TextView tv_item_title;
    EditText et_item_value;
    ImageView iv_item_right_icon;
    FrameLayout item_right_icon_parent_layout;
    View v_item_bottom_line;
    MaxMinValueWatcher mMaxMinValueWatcher;

    public FormInputText(Context context) {
        this(context, null);
    }

    public FormInputText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FormInputText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.app_health_write_info_item, this);

        tv_item_title = findViewById(R.id.tv_item_title);
        et_item_value = findViewById(R.id.et_item_value);
        item_right_icon_parent_layout = findViewById(R.id.item_right_icon_parent_layout);
        iv_item_right_icon = findViewById(R.id.iv_item_right_icon);
        v_item_bottom_line = findViewById(R.id.v_item_bottom_line);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.FormInputText, defStyleAttr, 0);
        String title = a.getString(R.styleable.FormInputText_item_title);
        String hint = a.getString(R.styleable.FormInputText_item_value_hint);
        boolean editable = a.getBoolean(R.styleable.FormInputText_item_editable, true);
        //如果不可编辑，则默认显示向右箭头
        //如果可编辑，则默认不显示向右箭头
        boolean item_show_right_arrow = a.getBoolean(R.styleable.FormInputText_item_show_right_arrow, !editable);
        //底部分割线
        boolean item_show_bottom_line = a.getBoolean(R.styleable.FormInputText_item_show_bottom_line, true);
        int item_input_type = a.getInt(R.styleable.FormInputText_item_input_type, InputType.TYPE_CLASS_TEXT);
        int item_ime_options = a.getInt(R.styleable.FormInputText_item_ime_options, -1); //default EditorInfo.IME_ACTION_NEXT
        int item_max_length = a.getInt(R.styleable.FormInputText_item_max_length, 50);
        int item_decimal_number = a.getInt(R.styleable.FormInputText_item_decimal_number, -1);
        String item_min_value_str = a.getString(R.styleable.FormInputText_item_min_value_str);
        String item_max_value_str = a.getString(R.styleable.FormInputText_item_max_value_str);
        String item_digits = a.getString(R.styleable.FormInputText_item_digits);
        //右侧小图标
        int item_right_icon_ref = a.getResourceId(R.styleable.FormInputText_item_right_icon_ref,-1);

        //可编辑且没有置右侧小图标，则显示右侧清除图标
        boolean item_show_right_clear = a.getBoolean(R.styleable.FormInputText_item_show_right_clear,editable && item_right_icon_ref < 0);

        //仅展示左侧标题
        boolean item_only_display_title = a.getBoolean(R.styleable.FormInputText_item_only_display_title, false);
        if (item_only_display_title){
            editable = false;
        }

        a.recycle();

        tv_item_title.setText(title);
        et_item_value.setHint(hint);

        et_item_value.setInputType(item_input_type);
        if (item_ime_options != -1){
            et_item_value.setImeOptions(item_ime_options);
        }

        setEditable(editable);

        //是否显示向右箭头
        item_right_icon_parent_layout.setVisibility(item_show_right_arrow ? VISIBLE : GONE);

        //如果设置了右侧图标，则显示右侧图标
        if (item_only_display_title){
            item_right_icon_parent_layout.setVisibility(GONE);
            et_item_value.setVisibility(INVISIBLE);
            et_item_value.setEnabled(false);
            et_item_value.setFocusable(false);
        } else if (item_right_icon_ref > 0){
            iv_item_right_icon.setImageResource(item_right_icon_ref);
            item_right_icon_parent_layout.setVisibility(VISIBLE);
        }else if (item_show_right_clear){ //是否显示右侧清除按钮
            iv_item_right_icon.setImageResource(R.drawable.ic_delete_searchbar);
            item_right_icon_parent_layout.setVisibility(INVISIBLE);
            item_right_icon_parent_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清除已经输入的内容
                    et_item_value.setText(null);
                }
            });
            et_item_value.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    item_right_icon_parent_layout.setVisibility(TextUtils.isEmpty(s) ? INVISIBLE : VISIBLE);
                }
            });
        }


        //是否显示底部分割线
        v_item_bottom_line.setVisibility(item_show_bottom_line ? VISIBLE : GONE);

        ArrayList<InputFilter> inputFilterList = new ArrayList<>();
        //最大字数过滤器
        inputFilterList.add(new InputFilter.LengthFilter(item_max_length));

        if (item_decimal_number >= 0) {
            //最大精度过滤器（保留两位小数）
            inputFilterList.add(new DecimalNumberInputFilter(item_decimal_number));
        }

        //保留旧的过滤器
        InputFilter[] oldInputFilters = et_item_value.getFilters();
        if (oldInputFilters != null && oldInputFilters.length > 0) {
            for (InputFilter filter : oldInputFilters) {
                inputFilterList.add(filter);
            }
        }
        //更新过滤器
        et_item_value.setFilters(inputFilterList.toArray(new InputFilter[inputFilterList.size()]));


        //设置了输入框的最大最小值
        if (item_min_value_str != null || item_max_value_str != null){
            mMaxMinValueWatcher = new MaxMinValueWatcher(item_min_value_str,item_max_value_str,et_item_value);
            et_item_value.addTextChangedListener(mMaxMinValueWatcher);
        }

        //设置了 item_digits
        if (!TextUtils.isEmpty(item_digits)){
            et_item_value.setKeyListener(DigitsKeyListener.getInstance(item_digits));
        }
    }

    public void setEditable(boolean editable) {
        if (editable) {
            et_item_value.setFocusable(true);
            et_item_value.setFocusableInTouchMode(true);
            et_item_value.setEnabled(true);
        } else {
            et_item_value.setFocusable(false);
            et_item_value.setFocusableInTouchMode(false);
            et_item_value.setEnabled(false);
        }
    }

    /**
     * 设置最小值，支持浮点数
     */
    public FormInputText setMinValueStr(String minValueStr){
        if (mMaxMinValueWatcher == null){
            mMaxMinValueWatcher = new MaxMinValueWatcher(et_item_value);
            et_item_value.addTextChangedListener(mMaxMinValueWatcher);
        }
        mMaxMinValueWatcher.setMinValueStr(minValueStr);
        return this;
    }

    /**
     * 设置最大值，支持浮点数
     */
    public FormInputText setMaxValueStr(String maxValueStr){
        if (mMaxMinValueWatcher == null){
            mMaxMinValueWatcher = new MaxMinValueWatcher(et_item_value);
            et_item_value.addTextChangedListener(mMaxMinValueWatcher);
        }
        mMaxMinValueWatcher.setMaxValueStr(maxValueStr);
        return this;
    }

    public static class MaxMinValueWatcher implements TextWatcher{
        private float minValue;
        private float maxValue;
        private EditText mEditText;
        private String minValueStr;
        private String maxValueStr;

        public void setMinValueStr(String minValueStr) {
            if (!TextUtils.isEmpty(minValueStr)){
                this.minValueStr = minValueStr;
                try {
                    this.minValue = Float.parseFloat(minValueStr);
                }catch (Exception e){
                }
            }
        }

        public void setMaxValueStr(String maxValueStr) {
            if (!TextUtils.isEmpty(maxValueStr)){
                this.maxValueStr = maxValueStr;
                try {
                    this.maxValue = Float.parseFloat(maxValueStr);
                }catch (Exception e){
                }
            }
        }

        public MaxMinValueWatcher(EditText editText) {
            mEditText = editText;
        }

        public MaxMinValueWatcher(String minValueStr, String maxValueStr, EditText editText) {
            this.mEditText = editText;
            setMinValueStr(minValueStr);
            setMaxValueStr(maxValueStr);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)){
                try {
                    float value = Float.parseFloat(s.toString());
                    if (minValueStr != null && value < minValue){
                        mEditText.setText(minValueStr);
                        mEditText.setSelection(minValueStr.length());
                    }else if (maxValueStr != null && value > maxValue){
                        mEditText.setText(maxValueStr);
                        mEditText.setSelection(maxValueStr.length());
                    }
                }catch (Exception e){
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
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
                if (dend - index >= mDecimalNumber + 1) {
                    return "";
                }
            }
            return null;
        }
    }

    /**
     * 设置整个 Item 布局的点击事件
     */
    public FormInputText setItemLayoutClickListener(OnClickListener onClickListener) {
        et_item_value.setEnabled(false);
        View health_btn_layout = findViewById(R.id.health_btn_layout);
        health_btn_layout.setVisibility(View.VISIBLE);
        health_btn_layout.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置 Value 输入框的点击事件
     */
    public FormInputText setItemValueClickListener(OnClickListener onClickListener) {
        et_item_value.setEnabled(false);
        View btn_value_area_layout = findViewById(R.id.btn_value_area_layout);
        btn_value_area_layout.setVisibility(View.VISIBLE);
        btn_value_area_layout.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置右侧区域的点击事件
     */
    public FormInputText setRightViewClickListener(OnClickListener onClickListener){
        item_right_icon_parent_layout.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 获取表单的表单的值
     */
    public String getItemValue() {
        return et_item_value.getText().toString();
    }

    /**
     * 设置表单的值
     *
     */
    public FormInputText setItemValue(String value) {
        et_item_value.setText(value);
        if (!TextUtils.isEmpty(value)){
            et_item_value.setSelection(value.length());
        }
        return this;
    }

    /**
     * 获取表单控件
     *
     */
    public EditText getItemValueEdit() {
        return et_item_value;
    }
}
