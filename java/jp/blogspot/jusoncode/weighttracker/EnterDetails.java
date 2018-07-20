package jp.blogspot.jusoncode.weighttracker;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EnterDetails extends Fragment{

    Button button;
    TextView numPickWeight,numPickHeight,numPickWeightTwo;
    RadioButton radioButtonOne, radioButtonTwo;
    RadioGroup radioGroup;
    EditText name,age;
    NumberPicker numberPickerHeightOne,numberPickerHeightTwo,numberPickerWeightNowOne,numberPickerWeightNowTwo,numberPickerTargetWeightOne,numberPickerTargetWeightTwo;

    ////talk  to activity
    CommunicationHere communicationHere;
    interface CommunicationHere{
       void detailsEntered();
    }
    ///Android dev uses activity be careful if change api...... deprecated activity version below only works on pre API 23
    ///context version works on api 23 and over, but now seems to call both methods in api 23 and over
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            communicationHere = (CommunicationHere) context;
        }catch (ClassCastException e){
           Toast.makeText(context,"Failed to attach CommunicationHere, report to developer",Toast.LENGTH_SHORT).show();
        }
    }

    @Deprecated
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            communicationHere = (CommunicationHere) activity;

        }catch (ClassCastException e){
            Toast.makeText(activity,"Failed to attach Communication, report to developer",Toast.LENGTH_SHORT).show();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter_details, container, false);

        setRetainInstance(true);

        button = (Button) view.findViewById(R.id.enterButton);
        name = (EditText) view.findViewById(R.id.editName);
        age = (EditText) view.findViewById(R.id.editAge);

        radioGroup =(RadioGroup)view.findViewById(R.id.enterDetailsRadioGroup);
        radioButtonOne = (RadioButton)view.findViewById(R.id.enterDetailsMale);
        radioButtonTwo = (RadioButton)view.findViewById(R.id.enterDetailsFemale);

        numberPickerHeightOne = (NumberPicker) view.findViewById(R.id.numberPickerHeightOne);
        numberPickerHeightTwo = (NumberPicker) view.findViewById(R.id.numberPickerHeightTwo);

        numberPickerWeightNowOne = (NumberPicker) view.findViewById(R.id.numberPickerWeightNowOne);
        numberPickerWeightNowTwo = (NumberPicker) view.findViewById(R.id.numberPickerWeightNowTwo);

        numberPickerTargetWeightOne = (NumberPicker) view.findViewById(R.id.numberPickerTargetWeightOne);
        numberPickerTargetWeightTwo = (NumberPicker) view.findViewById(R.id.numberPickerTargetWeightTwo);

        numPickWeight = (TextView) view.findViewById(R.id.numPickSelectWeight);
        numPickWeightTwo = (TextView) view.findViewById(R.id.numPickSelectWeightTwo);
        numPickHeight = (TextView) view.findViewById(R.id.numPickHeight);

        if (!HomeScreen.kilograms) {

                numberPickerWeightNowOne.setMaxValue(400);
                numberPickerWeightNowOne.setValue(300);
                numberPickerTargetWeightOne.setMaxValue(400);
                numberPickerTargetWeightOne.setValue(200);
                numPickWeight.setText(getString(R.string.lbs));
                numPickWeightTwo.setText(getString(R.string.lbs));
                numberPickerTargetWeightTwo.setMaxValue(15);
                numberPickerWeightNowTwo.setMaxValue(15);
                numberPickerHeightOne.setMaxValue(7);
                numberPickerHeightOne.setValue(5);
                numPickHeight.setText(getString(R.string.feetInch));
                numberPickerHeightTwo.setMaxValue(11);

        }else{

                numberPickerHeightOne.setMinValue(0);
                numberPickerHeightOne.setMaxValue(3);
                numberPickerHeightOne.setValue(1);

                numberPickerHeightTwo.setMinValue(0);
                numberPickerHeightTwo.setMaxValue(99);
                numberPickerHeightTwo.setValue(65);

                numberPickerWeightNowOne.setMinValue(0);
                numberPickerWeightNowOne.setMaxValue(300);
                numberPickerWeightNowOne.setValue(85);

                numberPickerWeightNowTwo.setMinValue(0);
                numberPickerWeightNowTwo.setMaxValue(99);
                numberPickerWeightNowTwo.setValue(0);

                numberPickerTargetWeightOne.setMinValue(0);
                numberPickerTargetWeightOne.setMaxValue(200);
                numberPickerTargetWeightOne.setValue(75);

                numberPickerTargetWeightTwo.setMinValue(0);
                numberPickerTargetWeightTwo.setMaxValue(99);
                numberPickerTargetWeightTwo.setValue(0);


        }


        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int a = radioGroup.getCheckedRadioButtonId();

                    if (a == R.id.enterDetailsMale) {

                        boolean missingEntry = false;
                        String[] editTextEntries = {name.getText().toString(), age.getText().toString()};
                        for (String text : editTextEntries) {
                            if (text == null) {
                                missingEntry = true;
                                break;
                            }
                        }
                        if (missingEntry) {
                            Toast.makeText(view.getContext(), getString(R.string.pleaseEnter), Toast.LENGTH_SHORT).show();
                        } else {
                            //Get the values from number pickers...
                            getDetails(view);

                            HomeScreen.sex = GlobalVariables.MALE;
                            HomeScreen.userSet = GlobalVariables.YES;
                        }

                        Toast.makeText(view.getContext(), getString(R.string.detailsEnter), Toast.LENGTH_SHORT).show();
                        communicationHere.detailsEntered();

                    } else if (a == R.id.enterDetailsFemale) {
                        boolean missingEntry = false;
                        String[] editTextEntries = {name.getText().toString(), age.getText().toString()};
                        for (String text : editTextEntries) {
                            if (text == null) {
                                missingEntry = true;
                                break;
                            }
                        }
                        if (missingEntry) {
                            Toast.makeText(view.getContext(), getString(R.string.pleaseEnter), Toast.LENGTH_SHORT).show();
                        } else {

                            //Get the values from number pickers...
                            getDetails(view);

                            HomeScreen.sex = GlobalVariables.FEMALE;
                            HomeScreen.userSet = GlobalVariables.YES;
                        }

                        Toast.makeText(view.getContext(), getString(R.string.detailsEnter), Toast.LENGTH_SHORT).show();
                        communicationHere.detailsEntered();

                    } else {
                        Toast.makeText(view.getContext(), getString(R.string.selectOne), Toast.LENGTH_SHORT).show();
                    }



                }
        });


        //////If setting new target or returning to this screen
        if (!HomeScreen.userName.equalsIgnoreCase("_")) {

                String[] splitWeight = HomeScreen.userWeight.split("\\.");
                final int weightFirst = Integer.parseInt(splitWeight[0]);
                numberPickerWeightNowOne.setValue(weightFirst);
                final int weightSecond = Integer.parseInt(splitWeight[1]);
                numberPickerWeightNowTwo.setValue(weightSecond);

                ///target weight, can use above values as weights are equal..
                numberPickerTargetWeightOne.setValue(weightFirst);
                numberPickerTargetWeightTwo.setValue(weightSecond);

                String[] splitHeight = HomeScreen.userHeight.split("\\.");
                final int heightFirst = Integer.parseInt(splitHeight[0]);
                numberPickerHeightOne.setValue(heightFirst);
                final int heightSecond = Integer.parseInt(splitHeight[1]);
                numberPickerHeightTwo.setValue(heightSecond);

                name.setText(HomeScreen.userName);
                age.setText(HomeScreen.userAge);

            }

        return view;
    }

    private void getDetails(View view){


        HomeScreen.userName = name.getText().toString();
        HomeScreen.userAge = age.getText().toString();

        StringBuilder heightSb =  new StringBuilder().append(numberPickerHeightOne.getValue()).append(".").append(numberPickerHeightTwo.getValue());
        HomeScreen.userHeight = heightSb.toString();


        StringBuilder weightSb =  new StringBuilder().append(numberPickerWeightNowOne.getValue()).append(".").append(numberPickerWeightNowTwo.getValue());
        HomeScreen.userWeight = weightSb.toString();
        //Prevent multiple entries when updating details after first target reached..
        if(HomeScreen.databaseAccess.databaseList.size() < 1) {
            String date = new SimpleDateFormat(GlobalVariables.DATE_STYLE, Locale.ENGLISH).format(new Date());
            Weight weightNew = new Weight(date, weightSb.toString(),HomeScreen.measurement);
            HomeScreen.databaseAccess.addWeight(weightNew, view);
            HomeScreen.databaseAccess.databaseList.add(weightNew);
        }
        StringBuilder idealWeightSb =  new StringBuilder().append(numberPickerTargetWeightOne.getValue()).append(".").append(numberPickerTargetWeightTwo.getValue());
        HomeScreen.userTargetWeight = idealWeightSb.toString();
    }

}
