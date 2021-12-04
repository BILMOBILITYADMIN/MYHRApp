package Utility;

/**
 * Created by Deeksha on 29-03-2016.
 */

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.text.DecimalFormat;

public class MyYAxisValueFormatter implements YAxisValueFormatter {

    private DecimalFormat mFormat;

    public MyYAxisValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return mFormat.format(value) + " $";
    }
}