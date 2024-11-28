package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrganizerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizerFragment extends Fragment {

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