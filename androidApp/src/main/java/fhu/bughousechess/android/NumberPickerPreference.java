package fhu.bughousechess.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

/**
 * A {@link android.preference.Preference} that displays a number picker as a dialog.
 */
public class NumberPickerPreference extends DialogPreference {

    // allowed range
    public static final int MAX_VALUE = 59;
    public static final int MIN_VALUE = 0;
    // enable or disable the 'circular behavior'
    public static final boolean WRAP_SELECTOR_WHEEL = true;

    private NumberPicker picker;
    private int value;
    private NumberPicker picker2;
    private int value2;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT;

        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.gravity = Gravity.RIGHT;

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 17)
        {
            layoutParams.setMarginStart(MainActivity.dialog_margin);
            layoutParams2.setMarginEnd(MainActivity.dialog_margin);
        }

        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);
        picker2 = new NumberPicker(getContext());
        picker2.setLayoutParams(layoutParams2);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);
        dialogView.addView(picker2);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        setValue(prefs.getInt("time1", GameActivity.minute), 1);
        setValue(prefs.getInt("time2", GameActivity.second), 2);

        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setMinValue(MIN_VALUE);
        picker.setMaxValue(MAX_VALUE);
        picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        picker.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        picker.setValue(getValue(1));
        picker2.setMinValue(MIN_VALUE);
        picker2.setMaxValue(MAX_VALUE);
        picker2.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        picker2.setWrapSelectorWheel(WRAP_SELECTOR_WHEEL);
        picker2.setValue(getValue(2));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult)
        {
            picker.clearFocus();
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                setValue(newValue, 1);
            }
            picker2.clearFocus();
            int newValue2 = picker2.getValue();
            if (callChangeListener(newValue2)) {
                setValue(newValue2, 2);
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            SharedPreferences.Editor preferenceEditor = prefs.edit();
            preferenceEditor.putInt("time1", value);
            preferenceEditor.putInt("time2", value2);
            preferenceEditor.apply();

        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, MIN_VALUE);

    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue)
    {
        //setValue(restorePersistedValue ? getPersistedInt(MIN_VALUE) : (Integer) defaultValue, 1);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        setValue(prefs.getInt("time1", GameActivity.minute), 1);
        setValue(prefs.getInt("time2", GameActivity.second), 2);
    }

    public void setValue(int value, int spinner)
    {
        if (spinner == 1)
        {
            this.value = value;
            persistInt(this.value);
        }
        if (spinner == 2)
        {
            this.value2 = value;
            persistInt(this.value2);
        }
    }

    public int getValue(int spinner)
    {
        if (spinner == 1)
        {
            return this.value;
        }
        if (spinner == 2)
        {
            return this.value2;
        }
        return -1;
    }
}