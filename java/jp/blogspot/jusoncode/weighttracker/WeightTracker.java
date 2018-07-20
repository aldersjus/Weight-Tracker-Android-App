package jp.blogspot.jusoncode.weighttracker;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static jp.blogspot.jusoncode.weighttracker.HomeScreen.kilograms;
import static jp.blogspot.jusoncode.weighttracker.HomeScreen.userHeight;
import static jp.blogspot.jusoncode.weighttracker.HomeScreen.userSet;
import static jp.blogspot.jusoncode.weighttracker.HomeScreen.userTargetWeight;
import static jp.blogspot.jusoncode.weighttracker.HomeScreen.userWeight;
import static jp.blogspot.jusoncode.weighttracker.R.attr.weight;


public class WeightTracker extends Fragment {

    private TextView bmiTextView;
    private TextView weightNow;
    private final String YES = "yes";
    private final String PERCENTAGE = "%";
    private WeightTrackerCustomViewGrid second;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight_tracker, container, false);

        setRetainInstance(true);

        bmiTextView = (TextView)view.findViewById(R.id.bmiTextViewSet);
        WeightTrackerCustomViewGrid first = (WeightTrackerCustomViewGrid)view.findViewById(R.id.firstCustomView);
        second = (WeightTrackerCustomViewGrid)view.findViewById(R.id.secondCustomView);
        weightNow = (TextView)first.findViewById(R.id.customTextTwo);
        if(HomeScreen.kilograms) {
            weightNow.setText(userWeight.concat(getString(R.string.kilogram)));
        }else{
            weightNow.setText(userWeight.concat(getString(R.string.lbs)));
        }
        TextView weightTarget = (TextView)second.findViewById(R.id.customTextTwo);
        if(HomeScreen.kilograms) {
            weightTarget.setText(userTargetWeight.concat(getString(R.string.kilogram)));
        }else{
            weightTarget.setText(userTargetWeight.concat(getString(R.string.lbs)));
        }

        calculateBMI();

        getActivity().databaseList();

        return view;
    }

    void updateWeight(String weight){
        weightNow.setText(weight);
        calculateBMI();
    }
    private void calculateBMI(){
        double bmi,weight,height;

        if(userSet.equalsIgnoreCase(YES) & kilograms){
            height = Double.parseDouble(userHeight);
            weight = Double.parseDouble(userWeight);
            bmi =  weight / (height * height);
            bmiTextView.setText(String.valueOf(bmi).substring(0,4).concat(PERCENTAGE));
        }else if(userSet.equalsIgnoreCase(YES) & !kilograms){
            String[] splitHeight = userHeight.split("\\.");
            height = Double.parseDouble(splitHeight[0]) * 12;//12 inches in a foot
            height += Double.parseDouble(splitHeight[1]);
            weight = Double.parseDouble(userWeight);
            bmi =  weight / (height * height)*703;
            bmiTextView.setText(String.valueOf(bmi).substring(0,4).concat(PERCENTAGE));
        }

    }


}