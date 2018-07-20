package jp.blogspot.jusoncode.weighttracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import static jp.blogspot.jusoncode.weighttracker.R.id.frameLayoutOne;


public class WelcomeScreen extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_screen, container, false);
        setRetainInstance(true);


        Button button = (Button)view.findViewById(R.id.welcomeButton);
        final RadioButton radioButtonTwo = (RadioButton)view.findViewById(R.id.weightChoiceLbs);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioButtonTwo.isChecked()){
                    HomeScreen.kilograms = false;
                    HomeScreen.measurement = "lbs";
                }else {
                    HomeScreen.measurement = "kg";
                }
                EnterDetails enterDetails = new EnterDetails();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(frameLayoutOne, enterDetails).commit();

            }
        });

        return view;
    }

}