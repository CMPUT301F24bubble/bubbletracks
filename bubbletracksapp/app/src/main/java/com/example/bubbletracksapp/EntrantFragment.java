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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EntrantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntrantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntrantFragment newInstance(String param1, String param2) {
        EntrantFragment fragment = new EntrantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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