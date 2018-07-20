package jp.blogspot.jusoncode.weighttracker;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class TutorialTwo extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder bulider = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();


        bulider.setView(layoutInflater.inflate(R.layout.dialog_tutorial_two,null));
        bulider.setPositiveButton(getString(R.string.next), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TutorialThree tutorial = new TutorialThree();
                tutorial.show(getActivity().getSupportFragmentManager(),"tutorialThree");
            }

        });

        bulider.setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Tutorial tutorial = new Tutorial();
                tutorial.show(getActivity().getSupportFragmentManager(),"tutorial");
            }

        });



        return bulider.create();
    }

}
