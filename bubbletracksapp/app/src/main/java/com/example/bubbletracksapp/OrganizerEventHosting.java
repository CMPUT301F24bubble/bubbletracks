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


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bubbletracksapp.databinding.ListsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        Date date = c.getTime();
        e.setDateTime(date);

        Entrant en = new Entrant();
        en.setName("hola", " tata");
        e.addToWaitList(en);

        en = new Entrant();
        en.setName("ches", " tata");
        e.addToWaitList(en);
        e.addToEnrolledList(en);

        en = new Entrant();
        en.setName("zoe", " tata");
        e.addToWaitList(en);

        en = new Entrant();
        en.setName("sam", " tata");
        e.addToWaitList(en);

        en = new Entrant();
        en.setName("eaaaa", " tata");
        e.addToCancelledList(en);
        en = new Entrant();
        en.setName("misete", " tata");
        e.addToCancelledList(en);

        hostedEvents.add(e);

        e = new Event();
        e.setName("eaaaa");
        c = Calendar.getInstance();
        date = c.getTime();
        e.setDateTime(date);
        hostedEvents.add(e);

        en.setName("hola", " tata");
        e.addToWaitList(en);

        en = new Entrant();
        en.setName("ches", " tata");
        e.addToWaitList(en);

        en = new Entrant();
        en.setName("zoe", " tata");
        e.addToWaitList(en);

        en = new Entrant();
        en.setName("sam", " tata");
        e.addToWaitList(en);

        en = new Entrant();
        en.setName("eaaaa", " tata");
        e.addToCancelledList(en);
        en = new Entrant();
        en.setName("misete", " tata");
        e.addToCancelledList(en);

        Log.d("TAG", view.getContext().toString());
        eventListView = binding.reusableListView;
        eventListAdapter = new EventHostListAdapter(this.getContext(), hostedEvents);
        eventListView.setAdapter(eventListAdapter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}