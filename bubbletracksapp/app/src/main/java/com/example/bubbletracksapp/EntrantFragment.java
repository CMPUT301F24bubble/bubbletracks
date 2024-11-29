package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntrantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntrantFragment extends Fragment {

    //private static final String ARG_PARAM1 = "param1";
    private Entrant currentUser;
    private TextView loadingText;
    private ConstraintLayout parentLayout;

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

        SharedPreferences localID = getContext().getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Not Found");
        EntrantDB entrantDB = new EntrantDB();

        // get the entrant from the database
        entrantDB.getEntrant(ID).thenAccept(user -> {
            if(user != null){
                currentUser = user;
                loadingText.setVisibility(View.GONE);
                parentLayout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Could not load profile.", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(getContext(), "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });

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
        Button profileButton = view.findViewById(R.id.button_profile);
        Button userEventsButton = view.findViewById(R.id.button_userEvents);
        loadingText = view.findViewById(R.id.loading_textview);
        parentLayout = view.findViewById(R.id.parent_layout);

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
    private void switchActivityButton(Button button, Intent intent){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });
    }
}