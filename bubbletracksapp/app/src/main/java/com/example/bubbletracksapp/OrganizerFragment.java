package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrganizerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizerFragment extends Fragment {

    private Entrant currentUser;
    private TextView loadingText;
    private ConstraintLayout parentLayout;

    public OrganizerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OrganizerFragment.
     */
    public static OrganizerFragment newInstance(String param1, String param2) {
        OrganizerFragment fragment = new OrganizerFragment();
        Bundle args = new Bundle();
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
            Log.d("why this error", e.getMessage());
            return null;
        });

    }

    /**
     * Buttons are managed here.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer, container, false);

        Button facilityButton = view.findViewById(R.id.button_facility);
        Button hostedEventsButton = view.findViewById(R.id.button_host);
        loadingText = view.findViewById(R.id.loading_textview);
        parentLayout = view.findViewById(R.id.parent_layout);

        /*Intent manageFacilityIntent = new Intent(getActivity(), OrganizerManageActivity.class);
        if(!MainActivity.currentUser.getFacility().isEmpty()){
            manageFacilityIntent.putExtra("id", user.getFacility());
            switchActivityButton(createFacilityButton, manageFacilityIntent);
            createFacilityButton.setText("MANAGE FACILITY");
        } else {
            switchActivityButton(createFacilityButton, createFacilityIntent);
        }*/

        Intent hostedEventsIntent = new Intent(getActivity(), OrganizerEventHosting.class);
        switchActivityButton(hostedEventsButton, hostedEventsIntent);

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
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });
    }
}