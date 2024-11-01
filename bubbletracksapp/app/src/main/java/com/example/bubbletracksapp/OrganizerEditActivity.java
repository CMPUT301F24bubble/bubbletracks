package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.OrganizerWaitlistSampleBinding;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Allows the organizer to sample N entrants from the waitlist.
 * The Organizer needs to choose an N and then click on sample.
 * @author Chester
 */
public class OrganizerEditActivity extends AppCompatActivity {
    // SHOUL BE ENTant INCOMPLETE
    ArrayList<Entrant> waitList = new ArrayList<>();
    ArrayList<Entrant> invitedList = new ArrayList<>();
    ArrayList<Entrant> rejectedList = new ArrayList<>();
    ArrayList<Entrant> cancelledList = new ArrayList<>();

    ArrayList<String> waitListArray = new ArrayList<>();

    ListView waitlistListView;
    ArrayAdapter<String> waitlistAdapter;


    private OrganizerWaitlistSampleBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrganizerWaitlistSampleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // INCOMPLETE
        Intent in =  getIntent();
        if(in.getParcelableArrayListExtra("wait") != null) {
            waitList.addAll(in.getParcelableArrayListExtra("wait"));
        }
        if(in.getParcelableArrayListExtra("invited") != null) {
            invitedList.addAll(in.getParcelableArrayListExtra("invited"));
        }
        if(in.getParcelableArrayListExtra("rejected") != null) {
            rejectedList.addAll(in.getParcelableArrayListExtra("rejected"));
        }
        if(in.getParcelableArrayListExtra("cancelled") != null) {
            cancelledList.addAll(in.getParcelableArrayListExtra("cancelled"));
        }


        // TEMP FUNCTION TO ADD ENTANS, SHOULD BE CHANGED TO GET fom LAST INTENT INCOMPLETE
        if(in.getParcelableArrayListExtra("wait") == null) {
            Entrant en = new Entrant();
            en.setName("hola", " tata");
            waitList.add(en);

            en = new Entrant();
            en.setName("ches", " tata");
            waitList.add(en);

            en = new Entrant();
            en.setName("zoe", " tata");
            waitList.add(en);

            en = new Entrant();
            en.setName("sam", " tata");
            waitList.add(en);

            en = new Entrant();
            en.setName("eaaaa", " tata");
            cancelledList.add(en);
            en = new Entrant();
            en.setName("misete", " tata");
            cancelledList.add(en);

        }

        for (int i = 0; i < waitList.size(); i++) {
            waitListArray.add(waitList.get(i).getNameAsString());
        }

        waitlistListView = binding.waitlistSample;
        waitlistAdapter = new ArrayAdapter<String>(this.getApplicationContext(), R.layout.list_simple_view,  waitListArray);
        waitlistListView.setAdapter(waitlistAdapter);


        binding.drawEntrants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nText = binding.numberToDraw;
                String nStr = nText.getText().toString();
                int n = Integer.parseInt(nStr);
                drawEntrants(n);
                startListActivity();

            }
        });


        binding.backTo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Filled with going to the last activity. INCOMPLETE
            }
        });

    }


    // should return error if n is bigger than the size of waitlist INCOMPLETE
    // Assuming it is the fist time it is called INCOMEPLETE
    /**
     * Allows the organizer to draw n entrants from the waitlist.
     * @param n The number of entrants to sample.
     * @author Chester
     * @return true if successfully sampled entrants
     */
    public boolean drawEntrants(int n) {
        Collections.shuffle(waitList);
        invitedList.clear();
        rejectedList.clear();
        invitedList.addAll(waitList.subList(0, n));
        rejectedList.addAll(waitList.subList(n, waitList.size()));

        return true;
    }


    private void startListActivity() {
        Intent intent = new Intent(OrganizerEditActivity.this, OrganizerEntrantListActivity.class);
        intent.putParcelableArrayListExtra("wait", waitList);
        intent.putParcelableArrayListExtra("invited", invitedList);
        intent.putParcelableArrayListExtra("rejected", rejectedList);
        intent.putParcelableArrayListExtra("cancelled", cancelledList);
        startActivity(intent);
    }

}