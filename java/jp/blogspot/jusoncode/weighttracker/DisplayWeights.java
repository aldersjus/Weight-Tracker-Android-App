package jp.blogspot.jusoncode.weighttracker;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;

public class DisplayWeights  extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_weights, container, false);

        setRetainInstance(true);

        final  int screenOrientation = getActivity().getResources().getConfiguration().orientation;

        if(!HomeScreen.databaseAccess.databaseList.isEmpty()){

            RecyclerView recycler = (RecyclerView)view.findViewById(R.id.recyclerViewDisplayWeights);
            recycler.setHasFixedSize(true);
            RecyclerView.LayoutManager lessonLayoutManager;
            if(HomeScreen.tablet & screenOrientation == Configuration.ORIENTATION_LANDSCAPE){
                lessonLayoutManager = new GridLayoutManager(view.getContext(), 2);
            }else{
                lessonLayoutManager = new LinearLayoutManager(view.getContext());
            }
            recycler.setLayoutManager(lessonLayoutManager);

            Collections.sort(HomeScreen.databaseAccess.databaseList);
            DisplayWeightsAdapter displayWeightsAdapter = new DisplayWeightsAdapter(HomeScreen.databaseAccess.databaseList);
            recycler.setAdapter(displayWeightsAdapter);


        }else{
            Toast.makeText(getActivity(),getString(R.string.noWeightsAddedYet),Toast.LENGTH_SHORT).show();
        }

        return view;
    }

}
