package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

    private Entrant currentUser;
    private TextView loadingText;
    private ConstraintLayout parentLayout;

    public AdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminFragment.
     */
    public static AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
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
            Toast.makeText(getContext(), "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        Button browseFacilitiesButton = view.findViewById(R.id.button_admin_facilities);
        Button browseProfilesButton = view.findViewById(R.id.button_admin_profiles);
        Button browseEventsButton = view.findViewById(R.id.button_eventEdit);
        loadingText = view.findViewById(R.id.loading_textview);
        parentLayout = view.findViewById(R.id.parent_layout);

        Intent adminProfileIntent = new Intent(getActivity(), AdminProfileViews.class);
        switchActivityButton(browseProfilesButton, adminProfileIntent);

        Intent adminFacilityIntent = new Intent(getActivity(), AdminFacilityViews.class);
        switchActivityButton(browseFacilitiesButton, adminFacilityIntent);

        Intent adminEventsIntent = new Intent(getActivity(), BrowseEventsScreenGenerator.class);
        switchActivityButton(browseEventsButton, adminEventsIntent);

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