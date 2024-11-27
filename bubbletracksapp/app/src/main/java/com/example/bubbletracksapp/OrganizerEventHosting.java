package com.example.bubbletracksapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.bubbletracksapp.databinding.OrganizerEventHostingBinding;
//
//public class OrganizerEventHosting extends AppCompatActivity {
//    private OrganizerEventHostingBinding binding;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = OrganizerEventHostingBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//
//
//        binding.waitlistButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(OrganizerEventHosting.this, OrganizerEditActivity.class);
//                intent.putParcelableArrayListExtra("wait", waitList);
//                intent.putParcelableArrayListExtra("invited", invitedList);
//                intent.putParcelableArrayListExtra("rejected", rejectedList);
//                intent.putParcelableArrayListExtra("cancelled", cancelledList);
//
//                startActivity(intent);
//            }
//        });
//
//    }
//}


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bubbletracksapp.databinding.ListsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Hold event that organizer is hosting
 * @author Chester
 */
public class OrganizerEventHosting extends Fragment{
    private ListsBinding binding;

    EventDB eventDB = new EventDB();
    ArrayList<Event> hostedEvents = new ArrayList<>();

    Context context;
    public Entrant currentUser;

    ListView eventListView;
    EventHostListAdapter eventListAdapter;

    /**
     * Initialize the layout of the organizer user interface
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return binding of the layout
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ListsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    /**
     * Additional view creations
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        MainActivity mainActivity = (MainActivity)getActivity();
        currentUser = mainActivity.currentUser;

        eventListView = binding.reusableListView;

        eventDB.getEventList(currentUser.getEventsOrganized()).thenAccept(events -> {
            if(events != null){
                Log.d("Events", "Events is not null");
                hostedEvents = events;
                eventListAdapter = new EventHostListAdapter(this.getContext(), hostedEvents);
                eventListView.setAdapter(eventListAdapter);

            } else {
                Toast.makeText(context, "No hosted events", Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * destroy the view created
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}