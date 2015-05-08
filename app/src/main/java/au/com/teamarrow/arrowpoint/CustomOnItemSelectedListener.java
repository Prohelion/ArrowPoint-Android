package au.com.teamarrow.arrowpoint;

import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


/**
 * Created by Admin on 2/05/2015.
 */

   public class CustomOnItemSelectedListener implements OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){


            Toast.makeText(parent.getContext(),
                    "Primary Data - " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
            //Run task to class that will display data during datagram receiver processing.
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    }

