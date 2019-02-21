package com.prohelion.arrowpoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.arrowpoint.R;

import com.prohelion.arrowpoint.utils.TextHelper;
import com.prohelion.canbus.model.CarData;

/**
 * Created by Jason on 8/17/2015.
 */
public class MessageFragment extends UpdateablePlaceholderFragment{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static boolean sendBtnPressed = false;

    private String typedMessage = null;
    private String reciever = null;
    private String message = null;
    private Spinner reciever_spinner;
    private Spinner message_spinner;

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
            typedMessage = sendView.getText().toString();

            switch (reciever_spinner.getSelectedItemPosition()){
                case 0: reciever = "Team";
                    break;
                case 1: reciever = "Arrow1";
                    break;
                case 2: reciever = "Lead";
                    break;
                case 3: reciever = "Chase";
                    break;
            }

            switch (message_spinner.getSelectedItemPosition()){
                case 0: message = typedMessage;
                    break;
                case 1: message = "Comms check?";
                    break;
                case 2: message = "Lost comms";
                    break;
                case 3: message = "Target = "+ typedMessage;
                    break;
                case 4: message = "BOX BOX BOX";
                    break;
            }

            carData.setSendMessage("-d "+ reciever + ", " + message);

            sendView.setText("");
            sendBtnPressed = false;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_messages,
                container, false);

            reciever_spinner = (Spinner) rootView.findViewById(R.id.spnReciever);

            message_spinner = (Spinner) rootView.findViewById(R.id.spnMessage);

            final Button button = (Button) rootView.findViewById(R.id.btnSendMessage);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sendBtnPressed = true;
                }
            });


        return rootView;


    }
}
