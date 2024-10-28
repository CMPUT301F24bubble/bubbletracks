package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.OrganizerWaitlistBinding;

import java.util.ArrayList;

/**
 * Organizer actions for entrants in waitlists. Allows the organizer to see who
 * is in the waitlist. And after sampling it shows who is invited and who
 * is not invited.
 * For people that were invited but rejected the invitation, it allows the organizer to
 * redraw them and replace them with someone from the waitlist.
 * @author Chester
 */
public class OrganizerEntrantListActivity extends AppCompatActivity {

    private OrganizerWaitlistBinding binding;

    //To be changed to waitlist class INCOMPLETE
    ArrayList<Entrant> waitList= new ArrayList<>();
    ArrayList<Entrant> invitedList= new ArrayList<>();
    ArrayList<Entrant> rejectedList= new ArrayList<>();
    int maximumNumberOfEntrants;
    OrganizerEditActivity organizerEditActivity;

    ListView waitlistListView;
    ArrayAdapter<String> waitlistAdapter;

    ListView invitedListView;
    ArrayAdapter<String> invitedAdapter;

    ListView cancelledListView;
    ArrayAdapter<String> cancelledAdapter;


    ArrayList<String> waitListArray = new ArrayList<>();
    ArrayList<String> invitedListArray = new ArrayList<>();
    ArrayList<String> cancelledListArray = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = OrganizerWaitlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // INCOMPLETE
        Intent in =  getIntent();
        if(in.getParcelableArrayListExtra("wait") != null) {
            waitList.addAll(in.getParcelableArrayListExtra("wait"));
        }
        if(in.getParcelableArrayListExtra("invited") != null) {
            invitedList.addAll(in.getParcelableArrayListExtra("invited"));
        }
        if(in.getParcelableArrayListExtra("cancelled") != null) {
            rejectedList.addAll(in.getParcelableArrayListExtra("cancelled"));
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
            en.setName("day", " tata");
            invitedList.add(en);

            en = new Entrant();
            en.setName("lupe", " tata");
            rejectedList.add(en);

        }



        //This is a simple way of making the lists, should be changed to fit the aesthetic INCOMPLETE
        for (int i = 0; i < waitList.size(); i++) {
            waitListArray.add(waitList.get(i).getNameAsString());
        }
        for (int i = 0; i < invitedList.size(); i++) {
            invitedListArray.add(invitedList.get(i).getNameAsString());
        }
        for (int i = 0; i < rejectedList.size(); i++) {
            cancelledListArray.add(rejectedList.get(i).getNameAsString());
        }

        waitlistListView = binding.waitlistList;
        waitlistAdapter = new ArrayAdapter<String>(this.getApplicationContext(), R.layout.list_simple_view,  waitListArray);
        waitlistListView.setAdapter(waitlistAdapter);

        invitedListView = binding.invitedList;
        invitedAdapter = new ArrayAdapter<String>(this.getApplicationContext(), R.layout.list_simple_view,  invitedListArray);
        invitedListView.setAdapter(invitedAdapter);

        cancelledListView = binding.rejectedList;
        cancelledAdapter = new ArrayAdapter<String>(this.getApplicationContext(), R.layout.list_simple_view,  cancelledListArray);
        cancelledListView.setAdapter(cancelledAdapter);
        Log.d("TAG", "onViewCreated: ");

        binding.toSampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerEntrantListActivity.this, OrganizerEditActivity.class);
                intent.putParcelableArrayListExtra("wait", waitList);
                intent.putParcelableArrayListExtra("invited", invitedList);
                intent.putParcelableArrayListExtra("cancelled", rejectedList);

                startActivity(intent);
            }
        });
    }



    //Called when an Entrant cancels or rejects invitation
    public void CancelledEntrant(Entrant entrant){
        invitedList.remove(entrant);
        waitList.remove(entrant);
        invitedList.add(organizerEditActivity.redrawEntrant());
        rejectedList.add(entrant);
        UpdateListDisplay();
    }

    /**
     * Updates the list view displays.
     * @author Chester
     */
    private void UpdateListDisplay() {
        waitlistAdapter.notifyDataSetChanged();
        invitedAdapter.notifyDataSetChanged();
        cancelledAdapter.notifyDataSetChanged();
    }
}
