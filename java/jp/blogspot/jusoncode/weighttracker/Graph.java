package jp.blogspot.jusoncode.weighttracker;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static jp.blogspot.jusoncode.weighttracker.HomeScreen.databaseAccess;
import static jp.blogspot.jusoncode.weighttracker.HomeScreen.displayBarNumber;

public class Graph extends Fragment {

    BarChart barChart;

    final ArrayList<Weight> listOfWeights = HomeScreen.databaseAccess.databaseList;
    private ArrayList<Float> weightsNew = new ArrayList<>();
    private float positionX;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        setRetainInstance(true);

        barChart = (BarChart)view.findViewById(R.id.chart);

        Collections.sort(listOfWeights);

        //With half a second wait graph seems to load fine, without this intermittent errors..
        SystemClock.sleep(500);
        loadGraph(displayBarNumber);


        return view;
    }


    private void loadGraph(int amount){
        weightsNew.clear();

        if(databaseAccess.databaseList.size() > 0) {
            ////Test with seven if ok , make for other sizes...
            if (databaseAccess.databaseList.size() >= 7 & amount == 7) {

                for (int pos = 6; pos >= 0; pos--) {

                    weightsNew.add(Float.valueOf(listOfWeights.get(pos).weight));
                }

            } else if (databaseAccess.databaseList.size() > 30 & amount == 30) {

                 for (int pos = 29; pos >= 0; pos--) {
                    weightsNew.add(Float.valueOf(listOfWeights.get(pos).weight));

                }


            } else if (databaseAccess.databaseList.size() > 90 & amount == 90) {

                for (int pos = 89; pos >= 0; pos--) {
                    weightsNew.add(Float.valueOf(listOfWeights.get(pos).weight));

                }

            } else {

                for (int pos = listOfWeights.size() - 1; pos >= 0; pos--) {

                    weightsNew.add(Float.valueOf(listOfWeights.get(pos).weight));

                }

            }

        }

        List<BarEntry> entries = new ArrayList<>();

        for(Float f : weightsNew){
            entries.add(new BarEntry(positionX + 1,f));
            positionX++;
        }

        ///set to zero else on rotate counts up.....
        positionX = 0;

        Description description = new Description();
        description.setEnabled(false);


        YAxis yAxis = barChart.getAxisRight();
        yAxis.setTextSize(12f);
        yAxis.setValueFormatter(new MyYAxisValueFormatter());

        YAxis yAxisL = barChart.getAxisLeft();
        yAxisL.setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        BarDataSet dataSet = new BarDataSet(entries,"Label");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(R.color.dark);

        BarData barData = new BarData(dataSet);

        barChart.setDescription(description);
        barChart.animateY(2000);
        barChart.setData(barData);

    }

    public void onPause(){
        super.onPause();
        weightsNew.clear();
    }

}
