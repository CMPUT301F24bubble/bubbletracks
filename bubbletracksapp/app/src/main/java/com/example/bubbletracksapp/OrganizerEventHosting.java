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

public class OrganizerEventHosting extends Fragment{
    private ListsBinding binding;

    EventDB eventDB = new EventDB();
    ArrayList<Event> hostedEvents = new ArrayList<>();

    Context context;
    public Entrant currentUser;


    ListView eventListView;
    EventHostListAdapter eventListAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ListsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        MainActivity mainActivity = (MainActivity)getActivity();
        //currentUser = mainActivity.currentUser;

        eventListView = binding.reusableListView;

        eventDB.getEventList(currentUser.getEventsOrganized()).thenAccept(events -> {
            if(events != null){
                hostedEvents = events;
                eventListAdapter = new EventHostListAdapter(this.getContext(), events);
                eventListView.setAdapter(eventListAdapter);

            } else {
                Toast.makeText(context, "No hosted events", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}