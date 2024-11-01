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


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.bubbletracksapp.databinding.FragmentFirstBinding;
import com.example.bubbletracksapp.databinding.ListsBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class OrganizerEventHosting extends Fragment{
    private ListsBinding binding;

    ArrayList<Event> hostedEvents = new ArrayList<>();

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

        Event e = new Event();
        e.setName("Nathacks");
        Calendar c = Calendar.getInstance();
        e.setDate(c);
        hostedEvents.add(e);

        e = new Event();
        e.setName("eaaaa");
        c = Calendar.getInstance();
        e.setDate(c);
        hostedEvents.add(e);

        Log.d("TAG", view.getContext().toString());
        eventListView = binding.reusableListView;
        eventListAdapter = new EventHostListAdapter(this.getContext(), hostedEvents);
        eventListView.setAdapter(eventListAdapter);
        //        waitlistListView = binding.waitlistList;
        //        waitlistAdapter = new EntrantListAdapter(this, waitList);
        //        waitlistListView.setAdapter(waitlistAdapter);
        //
        //

//        binding.reusableListView.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}