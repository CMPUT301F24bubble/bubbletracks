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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrganizerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganizerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganizerFragment newInstance(String param1, String param2) {
        OrganizerFragment fragment = new OrganizerFragment();
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
        Button createEventButton = view.findViewById(R.id.button_eventEdit);

        Intent createEventIntent = new Intent(getActivity(), OrganizerActivity.class);
        switchActivityButton(createEventButton, createEventIntent);

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