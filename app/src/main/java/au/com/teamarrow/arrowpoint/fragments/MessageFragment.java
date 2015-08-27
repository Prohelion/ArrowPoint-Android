package au.com.teamarrow.arrowpoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.arrowpoint.R;

import au.com.teamarrow.arrowpoint.utils.TextHelper;
import au.com.teamarrow.canbus.model.CarData;

/**
 * Created by Jason on 8/17/2015.
 */
public class MessageFragment extends UpdateablePlaceholderFragment{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private String sendMessage = null;
    private static boolean sendBtnPressed = false;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void Update(View fragmentView, CarData carData) {

        // **************** Messages ***************
        TextHelper textHelper = new TextHelper(fragmentView);

        textHelper.setText(R.id.txtReceivedMessages,carData.getMessages());

        if (sendBtnPressed) {
            EditText sendView = (EditText) fragmentView.findViewById(R.id.edtSend);
            sendMessage = sendView.getText().toString();
            carData.setSendMessage(sendMessage);
            sendView.setText("");
            sendBtnPressed = false;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_messages,
                container, false);


            final Button button = (Button) rootView.findViewById(R.id.btnSendMessage);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sendBtnPressed = true;
                }
            });
        return rootView;


    }
}
