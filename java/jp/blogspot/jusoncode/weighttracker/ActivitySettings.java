package jp.blogspot.jusoncode.weighttracker;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ActivitySettings extends AppCompatActivity {



    private RadioGroup positionOfGraph,colourOfBackground,notificationGroup,barGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final TextView licenseTextView = (TextView)findViewById(R.id.licenseText);
        final LinearLayout hidden = (LinearLayout)findViewById(R.id.overlappedSettingsView);
        final Button closeLicenseButton = (Button)findViewById(R.id.closeLicenseButton);
        colourOfBackground = (RadioGroup) findViewById(R.id.radioGroupColours);
        positionOfGraph = (RadioGroup) findViewById(R.id.radioGroupGraph);
        notificationGroup = (RadioGroup) findViewById(R.id.radioGroupNotifications);
        barGroup = (RadioGroup) findViewById(R.id.radioGroupDisplayNumberOfWeights);

        RadioButton grey = (RadioButton)findViewById(R.id.radioButtonGreen);
        RadioButton white = (RadioButton)findViewById(R.id.radioButtonWhite);
        RadioButton top = (RadioButton)findViewById(R.id.radioButtonTop);
        RadioButton bottom = (RadioButton)findViewById(R.id.radioButtonBottom);
        RadioButton notificationOn = (RadioButton)findViewById(R.id.radioButtonNotificationsOn);
        RadioButton notificationOff = (RadioButton)findViewById(R.id.radioButtonNotificationsOff);
        RadioButton seven = (RadioButton)findViewById(R.id.radioButtonSeven);
        RadioButton thirty = (RadioButton)findViewById(R.id.radioButtonThirty);
        RadioButton ninety = (RadioButton)findViewById(R.id.radioButtonNinety);
        RadioButton all = (RadioButton)findViewById(R.id.radioButtonAll);

        Button button = (Button)findViewById(R.id.applyButton);

        Intent intent = getIntent();
        boolean darkScreen = intent.getBooleanExtra(GlobalVariables.DARK,false);
        boolean topPos = intent.getBooleanExtra(GlobalVariables.TOP,false);
        boolean alarmCancelled = intent.getBooleanExtra(GlobalVariables.ALARM_CANCEL,false);
        int bars = intent.getIntExtra(GlobalVariables.BARS,7);
        if(darkScreen){
            this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.grey));
            grey.setChecked(true);
        }else{
            this.getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            white.setChecked(true);
        }

        if(topPos){
            top.setChecked(true);
        }else {
            bottom.setChecked(true);
        }

        if(alarmCancelled){
            notificationOff.setChecked(true);
        }else{
            notificationOn.setChecked(true);
        }

        if (bars == 7){
            seven.setChecked(true);
        }else if(bars == 30){
            thirty.setChecked(true);
        }else if(bars == 90){
            ninety.setChecked(true);
        }else{
            all.setChecked(true);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendIntent();
            }
        });

        licenseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidden.setVisibility(View.VISIBLE);
            }
        });

        closeLicenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidden.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void sendIntent(){
        final int colour = colourOfBackground.getCheckedRadioButtonId();
        final int position = positionOfGraph.getCheckedRadioButtonId();
        final int notifications = notificationGroup.getCheckedRadioButtonId();
        final int bars = barGroup.getCheckedRadioButtonId();
        Intent intent = new Intent(this,HomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(GlobalVariables.INTENT_SENT,true);
        if(colour == R.id.radioButtonWhite){
            intent.putExtra(GlobalVariables.DARK,false);
        }else if(colour == R.id.radioButtonGreen) {
            intent.putExtra(GlobalVariables.DARK, true);
        }
        if(position == R.id.radioButtonTop){
            intent.putExtra(GlobalVariables.POSITION,true);
        }else if(position == R.id.radioButtonBottom){
            intent.putExtra(GlobalVariables.POSITION,false);
        }
        if (notifications == R.id.radioButtonNotificationsOff) {
            intent.putExtra(GlobalVariables.ALARM_CANCEL, true);
        }
        if(bars == R.id.radioButtonSeven){
            intent.putExtra(GlobalVariables.BARS,7);
        }else  if(bars == R.id.radioButtonThirty){
            intent.putExtra(GlobalVariables.BARS,30);
        }else  if(bars == R.id.radioButtonNinety){
            intent.putExtra(GlobalVariables.BARS,90);
        }else  if(bars == R.id.radioButtonAll){
            intent.putExtra(GlobalVariables.BARS,360);
        }

        startActivity(intent);
        finish();
    }

}
