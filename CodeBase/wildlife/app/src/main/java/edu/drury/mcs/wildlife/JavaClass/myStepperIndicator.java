package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.util.AttributeSet;

import com.badoualy.stepperindicator.StepperIndicator;

import java.io.Serializable;

/**
 * Created by mark93 on 12/11/2016.
 */

public class myStepperIndicator extends StepperIndicator implements Serializable {
    public myStepperIndicator(Context context) {
        super(context);
    }

    public myStepperIndicator(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public myStepperIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
