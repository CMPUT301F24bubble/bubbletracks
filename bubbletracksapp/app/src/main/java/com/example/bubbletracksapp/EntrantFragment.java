package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntrantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntrantFragment extends Fragment {

    //private static final String ARG_PARAM1 = "param1";

    public EntrantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EntrantFragment.
     */
    public static EntrantFragment newInstance(String param1, String param2) {
        EntrantFragment fragment = new EntrantFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrant, container, false);

        Button scanButton = view.findViewById(R.id.button_scan);
        Button ticketsButton = view.findViewById(R.id.button_empty7);
        Button profileButton = view.findViewById(R.id.button_empty6);
        Button userEventsButton = view.findViewById(R.id.button_empty8);

        Intent scanIntent = new Intent(getActivity(), QRScanner.class);
        switchActivityButton(scanButton, scanIntent);

        Intent profileIntent = new Intent(getActivity(), EntrantEditActivity.class);
        switchActivityButton(profileButton, profileIntent);

        Intent userEventsIntent = new Intent(getActivity(), AppUserEventScreenGenerator.class);
        switchActivityButton(userEventsButton, userEventsIntent);

        // Inflate the layout for this fragment
        return view;
    }
    /**
     * Generalized code for buttons that use start(Activity()
     * @param button is the button that will be clicked
     * @param intent is the intent passed to startActivity()
     */
    public void switchActivityButton(Button button, Intent intent){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}