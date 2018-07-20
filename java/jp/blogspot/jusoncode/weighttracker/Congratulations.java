package jp.blogspot.jusoncode.weighttracker;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.widget.FrameLayout;

import static jp.blogspot.jusoncode.weighttracker.R.id.frameLayoutOne;

public class Congratulations extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder bulider = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();


        bulider.setView(layoutInflater.inflate(R.layout.dialog_congratulations,null)).setPositiveButton(getString(R.string.newTarget), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EnterDetails enterDetails = new EnterDetails();
                FrameLayout frameLayoutTwo =(FrameLayout)getActivity().findViewById(R.id.frameLayoutTwo);
                frameLayoutTwo.removeAllViews();
                getActivity().getFragmentManager().beginTransaction().replace(frameLayoutOne,enterDetails).commit();
            }
        });

        bulider.setNegativeButton(getString(R.string.closeApp), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                getActivity().finish();
            }

        });

        return bulider.create();
    }


}