package jp.blogspot.jusoncode.weighttracker;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

class DisplayWeightsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Weight> weights;
    private Double first = 0.0;
    private Double second = 0.0;

    DisplayWeightsAdapter(ArrayList<Weight> weights){

        this.weights = weights;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weight_cardview,parent,false);

        return new RecyclerView.ViewHolder(view){

        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final String PLUS = "+";

        ////Doing this below as recycler view creates some empty cards off bottom of screen....
        Weight weight = weights.get(position);
        if(position <= weights.size() - 2) {
            Weight weightTwo = weights.get(position + 1);
            first = Double.valueOf(weight.weight);
            second = Double.valueOf(weightTwo.weight);
        }
        ///for last position there should be no difference in weight now.....
        else  if(position <= weights.size() - 1) {
            Weight weightTwo = weights.get(position);
            first = Double.valueOf(weight.weight);
            second = Double.valueOf(weightTwo.weight);
        }

        ////Alternate the colour of recycle cards...
        if(position % 2 == 0){
            LinearLayout linearLayout = (LinearLayout)holder.itemView.findViewById(R.id.linearLayoutInsideCard);
            linearLayout.setBackgroundColor(Color.LTGRAY);
        }

        TextView tv =(TextView) holder.itemView.findViewById(R.id.weightCardText);
        TextView tvTwo =(TextView) holder.itemView.findViewById(R.id.weightCardTextTwo);
        TextView tvThree =(TextView) holder.itemView.findViewById(R.id.weightCardTextThree);


        String sum = String.valueOf(first - second);
        tv.setText(weight.date);
        if(first - second < 0) {

            if (sum.length() > 4) {
                tvTwo.setText(String.valueOf(first - second).substring(0,5));
            } else {
                tvTwo.setText(String.valueOf(first - second));
            }
            tvTwo.setTextColor(Color.rgb(76,175,80));
        }else if (first - second > 0){
            if (sum.length() > 4) {
                tvTwo.setText(PLUS.concat(String.valueOf(first - second).substring(0,4)));
            } else {
                tvTwo.setText(PLUS.concat(String.valueOf(first - second)));
            }
            tvTwo.setTextColor(Color.RED);
        }else {
            if (sum.length() > 4) {
                tvTwo.setText(PLUS.concat(String.valueOf(first - second).substring(0,4)));
            } else {
                tvTwo.setText(PLUS.concat(String.valueOf(first - second)));
            }
            tvTwo.setTextColor(Color.DKGRAY);
        }
        tvThree.setText(weight.weight);

    }

    @Override
    public int getItemCount() {
        return  weights.size();
    }
}
