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
 * Use the {@link AdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminFragment extends Fragment {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        Button browseFacilitiesButton = view.findViewById(R.id.button_admin_facilities);
        Button browseProfilesButton = view.findViewById(R.id.button_admin_profiles);

        Intent adminProfileIntent = new Intent(getActivity(), AdminProfileViews.class);
        switchActivityButton(browseProfilesButton, adminProfileIntent);

        Intent adminFacilityIntent = new Intent(getActivity(), AdminFacilityViews.class);
        switchActivityButton(browseFacilitiesButton, adminFacilityIntent);

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