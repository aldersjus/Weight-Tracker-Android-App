package jp.blogspot.jusoncode.weighttracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.Toast;

public class TutorialThree extends DialogFragment {

    final int DELAY_BEFORE_LAUNCHING_NEXT_TUTORIAL = 500;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder bulider = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        bulider.setView(layoutInflater.inflate(R.layout.dialog_tutorial_three,null));
        bulider.setPositiveButton(getString(R.string.gotIt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),getString(R.string.letsStart),Toast.LENGTH_SHORT).show();
            }

        });

        bulider.setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TutorialTwo tutorial = new TutorialTwo();
                tutorial.show(getActivity().getSupportFragmentManager(),"tutorialTwo");
            }

        });

        SystemClock.sleep(DELAY_BEFORE_LAUNCHING_NEXT_TUTORIAL);

        return bulider.create();
    }
}
