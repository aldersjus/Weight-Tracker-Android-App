package jp.blogspot.jusoncode.weighttracker;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

class MyYAxisValueFormatter implements IAxisValueFormatter {

    private DecimalFormat decimalFormat;

    MyYAxisValueFormatter(){
        decimalFormat = new DecimalFormat("###,###,##0.0");
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(HomeScreen.kilograms) {
            return decimalFormat.format(value) + "kg";
        }else{
            return decimalFormat.format(value) + "lbs";
        }
    }
}
