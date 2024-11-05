package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.LotteryMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    ArrayList<Entrant> enrolledList = new ArrayList<>();

    ArrayList<String> waitListArray = new ArrayList<>();

    ListView waitlistListView;
    ArrayAdapter<String> waitlistAdapter;


    private LotteryMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LotteryMainBinding.inflate(getLayoutInflater());
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
        if(in.getParcelableArrayListExtra("enrolled") != null) {
            enrolledList.addAll(in.getParcelableArrayListExtra("enrolled"));
        }


        for (int i = 0; i < waitList.size(); i++) {
            waitListArray.add(waitList.get(i).getNameAsString());
        }

        waitlistListView = binding.reusableListView;
        waitlistAdapter = new ArrayAdapter<String>(this.getApplicationContext(), R.layout.list_simple_view,  waitListArray);
        waitlistListView.setAdapter(waitlistAdapter);


        Spinner nSpinner = binding.waitlistChooseCount;
        List<String> spinList = new ArrayList<String>();
        for (int i=1; i<=waitList.size(); i++){
            spinList.add(String.valueOf(i));
        }
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nSpinner.setAdapter(spinAdapter);

        binding.chooseFromWaitlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner nSpin = binding.waitlistChooseCount;
                String nStr = nSpin.getSelectedItem().toString();
                int n = Integer.parseInt(nStr);
                drawEntrants(n);
                startListActivity();
            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
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
        enrolledList.clear();
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
        intent.putParcelableArrayListExtra("enrolled", enrolledList);
        startActivity(intent);
    }

}
