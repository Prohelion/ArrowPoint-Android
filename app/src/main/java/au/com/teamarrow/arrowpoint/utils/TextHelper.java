package au.com.teamarrow.arrowpoint.utils;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arrowpoint.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by ctuesley on 5/05/2015.
 */
public class TextHelper {

    private View myView = null;
    private int highlightColour = Color.RED;
    private int normalColour  = Color.WHITE;

    public TextHelper(View myView) {
        this.myView = myView;
    }

    public void setText(int view, String text) {

        TextView textView = (TextView) myView.findViewById(view);
        if (textView == null) return;

        textView.setText(text);
    }

    public void setText(int view, Integer number) {

        TextView textView = (TextView) myView.findViewById(view);
        if (textView == null) return;

        textView.setText(number.toString());
    }

    public void setText(int view, Double number, String format) {
        NumberFormat formatterWithDecimal = new DecimalFormat(format);
        TextView textView = (TextView) myView.findViewById(view);
        if (textView == null) return;

        textView.setText(formatterWithDecimal.format(number));
    }

    public void setText(int view, Double number) {

        setText(view, number, "#0.00");
    }

    public void setProgressBar(int viewBar, int viewTxt, Integer value ) {

        ProgressBar progressBar = (ProgressBar) myView.findViewById(viewBar);
        if (progressBar == null) return;

        progressBar.setProgress(value);
        setText(viewTxt, value + "%");
    }

    public void setProgressBar(int viewBar, Integer value ) {

        ProgressBar progressBar = (ProgressBar) myView.findViewById(viewBar);
        if (progressBar == null) return;

        progressBar.setProgress(value);
    }

    public void setFlicker(int view, boolean isOn ) {

        TextView textView = (TextView) myView.findViewById(view);
        if (textView == null) return;

        if  (!isOn) {
            textView.setVisibility(View.INVISIBLE);
        }else {
            Animation anim = new AlphaAnimation(0, 1);
            anim.setDuration(50); //You can manage the blinking time with this parameter
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            textView.startAnimation(anim);
        }
    }

    public void setVisibility(int view, boolean isVisible ) {

        TextView textView = (TextView) myView.findViewById(view);
        if (textView == null) return;

        if  (isVisible) {
            textView.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    public void setImageVisibility(int view, boolean isVisible ) {

        ImageView imageView = (ImageView) myView.findViewById(view);
        if (imageView == null) return;

        if  (isVisible) {
            imageView.setVisibility(View.VISIBLE);
        }else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    public void setColourStatus(int view, boolean status) {

        TextView textView = (TextView) myView.findViewById(view);
        if (textView == null) return;

        // Run this once and once only to get the origional colour
        //if (defaultTextColour == 0) defaultTextColour = textView.getCurrentTextColor();

        if ( status ) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(highlightColour);

        } else {
            textView.setTypeface(Typeface.DEFAULT);
            textView.setTextColor(normalColour);
        }

    }

    public void setColour(int view, int colour) {

        TextView textView = (TextView) myView.findViewById(view);
        if (textView == null) return;

        textView.setTextColor(colour);
    }

}