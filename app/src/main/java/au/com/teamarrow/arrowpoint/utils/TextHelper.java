package au.com.teamarrow.arrowpoint.utils;

import android.app.Activity;
import android.app.Fragment;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by ctuesley on 5/05/2015.
 */
public class TextHelper {

    private Fragment myFragment = null;
    private Activity myActivity = null;

    public TextHelper(Activity activity) {
        myActivity = activity;
    }

    public TextHelper(Fragment fragment) {
        myFragment = fragment;
    }

    private void setText(int view, String text) {

        TextView textView;

        if ( myActivity != null) textView = (TextView) myActivity.findViewById(view);
        else textView = (TextView) myFragment.getView().findViewById(view);

        textView.setText(text);
    }

    private void setText(int view, Integer number) {

        TextView textView;

        if ( myActivity != null) textView = (TextView) myActivity.findViewById(view);
        else textView = (TextView) myFragment.getView().findViewById(view);

        textView.setText(number.toString());
    }

    private void setText(int view, Double number, String format) {
        NumberFormat formatterWithDecimal = new DecimalFormat(format);
        TextView textView;

        if ( myActivity != null) textView = (TextView) myActivity.findViewById(view);
        else textView = (TextView) myFragment.getView().findViewById(view);

        textView.setText(formatterWithDecimal.format(number));
    }

    private void setText(int view, Double number) {
        setText(view, number, "#0.00");
    }

    private void setProgressBar(int viewBar, int viewTxt, Integer value ) {

        ProgressBar progressBar;
        TextView progressText;

        if ( myActivity != null) progressBar = (ProgressBar) myActivity.findViewById(viewBar);
        else progressBar = (ProgressBar) myFragment.getView().findViewById(viewBar);

        progressBar.setProgress(value);
        setText(viewTxt, value + "%");
    }

    private void setProgressBar(int viewBar, Integer value ) {
        ProgressBar progressBar;

        if ( myActivity != null) progressBar = (ProgressBar) myActivity.findViewById(viewBar);
        else progressBar = (ProgressBar) myFragment.getView().findViewById(viewBar);

        progressBar.setProgress(value);
    }



}
