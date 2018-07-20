package jp.blogspot.jusoncode.weighttracker;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class EnterWeight extends Fragment{

    NumberPicker numberPickerOne, numberPickerTwo;
    ////talk  to activity
    Communication communication;
    interface Communication{
        void passWeight(String weight);
        void checkTargetReached();
    }
    ///Android dev uses activity be careful if change api...... deprecated activity version below only works on pre API 23
    ///context version works on api 23 and over, but now seems to call both methods in api 23 and over
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try{
            communication = (Communication) context;

        }catch (ClassCastException e){
            Toast.makeText(context,"Failed to attach Communication, report to developer",Toast.LENGTH_SHORT).show();
        }
    }

    @Deprecated
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            communication = (Communication) activity;

        }catch (ClassCastException e){
            Toast.makeText(activity,"Failed to attach CommunicationHere, report to developer",Toast.LENGTH_SHORT).show();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_weight, container, false);

        setRetainInstance(true);



        final Button button = (Button)view.findViewById(R.id.enterWeightButton);
        numberPickerOne = (NumberPicker)view.findViewById(R.id.numberPickerOne);
        numberPickerTwo = (NumberPicker)view.findViewById(R.id.numberPickerTwo);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String date = new SimpleDateFormat(GlobalVariables.DATE_STYLE, Locale.ENGLISH).format(new Date());
                Weight weightNew = new Weight(date, new StringBuilder().append(numberPickerOne.getValue()).append(".").append(numberPickerTwo.getValue()).toString(),HomeScreen.measurement);

                HomeScreen.databaseAccess.addWeight(weightNew,view);

                HomeScreen.userWeight = weightNew.weight;

                try {
                    if(HomeScreen.kilograms) {
                       communication.passWeight(weightNew.weight.concat(getString(R.string.kilogram)));
                    }else{
                       communication.passWeight(weightNew.weight.concat(getString(R.string.lbs)));
                    }

                }catch (Exception e){
                    Toast.makeText(view.getContext(),"Failed on click Communication, report to developer",Toast.LENGTH_SHORT).show();
                }
                ///Testing snackbar
                Snackbar.make(view,getString(R.string.weightEntered), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                communication.checkTargetReached();
                ///Reload list after new weight added.
                HomeScreen.databaseAccess.getWeights();

            }
        });



        if(HomeScreen.kilograms) {
            numberPickerOne.setMinValue(30);
            numberPickerOne.setMaxValue(300);
            String[] splitWeight = HomeScreen.userWeight.split("\\.");
            final int weightFirst = Integer.parseInt(splitWeight[0]);
            numberPickerOne.setValue(weightFirst);
            numberPickerTwo.setMinValue(0);
            numberPickerTwo.setMaxValue(99);
            final int weightSecond = Integer.parseInt(splitWeight[1]);
            numberPickerTwo.setValue(weightSecond);
        }
        else if(!HomeScreen.kilograms){
            numberPickerOne.setMinValue(100);
            numberPickerOne.setMaxValue(500);
            numberPickerTwo.setMinValue(0);
            numberPickerTwo.setMaxValue(15);
            String[] splitWeight = HomeScreen.userWeight.split("\\.");
            final int weightFirst = Integer.parseInt(splitWeight[0]);
            numberPickerOne.setValue(weightFirst);
            final int weightSecond = Integer.parseInt(splitWeight[1]);
            numberPickerTwo.setValue(weightSecond);
            TextView tv = (TextView)view.findViewById(R.id.textViewEnterWeightThree);
            tv.setText(getString(R.string.lbs));
        }


        return view;
    }

}
