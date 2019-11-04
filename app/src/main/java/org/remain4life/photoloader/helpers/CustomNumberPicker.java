package org.remain4life.photoloader.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.NumberPicker;

import org.remain4life.photoloader.R;

import static org.remain4life.photoloader.helpers.Constants.ERROR_TAG;

public class CustomNumberPicker extends NumberPicker {

    public CustomNumberPicker(Context context) {
        super(context);
        init(context);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        Drawable gradientDrawable = context.getResources().getDrawable(R.drawable.numberpicker_divider);
        setDividerDrawable(gradientDrawable);
    }


    public void setDividerDrawable(Drawable customDrawable) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(this, customDrawable);
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                    Log.e(ERROR_TAG, e.toString());
                }
                break;
            }
        }
    }
}
