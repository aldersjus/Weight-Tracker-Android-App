package jp.blogspot.jusoncode.weighttracker;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeightTrackerCustomViewGrid extends GridLayout {

    public WeightTrackerCustomViewGrid(Context context){
        super(context);
    }

    public WeightTrackerCustomViewGrid(Context context, AttributeSet attrs) {
        super(context, attrs);

        ///OBTAIN A LIST OF PARAMETERS FROM ATTRS AND STYLEABLE
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.WeightTrackerCustomViewGrid);

        Drawable image;
        String weight;
        String text;

        try{

            ////TRY TO USE OBTAINED VALUES TO APPEND TO VARIABLES
            text = typedArray.getString(R.styleable.WeightTrackerCustomViewGrid_text);
            weight = typedArray.getString(R.styleable.WeightTrackerCustomViewGrid_weight);

            image = typedArray.getDrawable(R.styleable.WeightTrackerCustomViewGrid_image);

        }finally{

            typedArray.recycle();
        }

        ///INFLATE THE CUSTOM VIEW, GET THE IDS...
        ///SET ABOVE VARIABLES FOR THE VIEWS BELOW...
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.custom_view_grid, this,true);

        TextView textView = (TextView)findViewById(R.id.customTextOne);
        textView.setText(text);

        TextView textViewTwo = (TextView)findViewById(R.id.customTextTwo);
        textViewTwo.setText(weight);

        ImageView iv = (ImageView)findViewById(R.id.customImageView);
        iv.setBackground(image);


    }


    public WeightTrackerCustomViewGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

    }
}