package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.bubbletracksapp.databinding.OrganizerWaitlistBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Organizer actions for entrants
 */
public class OrganizerEntrantListActivity extends Fragment {

    private OrganizerWaitlistBinding binding;

    //To be changed to waitlist class INCOMPLETE
    List<Entrant> waitList;
    List<Entrant> invitedList;
    List<Entrant> cancelledList;
    int maximumNumberOfEntrants;
    OrganizerEditActivity organizerEditActivity;

    ListView waitlistListView;
    ArrayAdapter<String> waitlistAdapter;

    ListView invitedListView;
    ArrayAdapter<String> invitedAdapter;

    ListView cancelledListView;
    ArrayAdapter<String> cancelledAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = OrganizerWaitlistBinding.inflate(inflater, container, false);

        Log.d("TAG", "aaaa: ");

        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        waitList = new ArrayList<Entrant>();
        invitedList = new ArrayList<Entrant>();
        cancelledList = new ArrayList<Entrant>();


        Entrant en = new Entrant();
        en.setName("hola", " tata");
        waitList.add(en);

        en = new Entrant();
        en.setName("ches", " tata");
        waitList.add(en);

        en = new Entrant();
        en.setName("day", " tata");
        invitedList.add(en);

        en = new Entrant();
        en.setName("lupe", " tata");
        cancelledList.add(en);


        ArrayList<String> waitListArray = new ArrayList<>();
        for (int i = 0; i < waitList.size(); i++) {
            waitListArray.add(waitList.get(i).getNameAsString());
        }

        waitlistListView = binding.waitlistList;
        waitlistAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.list_simple_view,  waitListArray);
        waitlistListView.setAdapter(waitlistAdapter);
        Log.d("TAG", "onViewCreated: ");

        ArrayList<String> invitedListArray = new ArrayList<>();
        for (int i = 0; i < invitedList.size(); i++) {
            invitedListArray.add(invitedList.get(i).getNameAsString());
        }

        invitedListView = binding.invitedList;
        invitedAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.list_simple_view,  invitedListArray);
        invitedListView.setAdapter(invitedAdapter);
        Log.d("TAG", "onViewCreated: ");


        ArrayList<String> cancelledListArray = new ArrayList<>();
        for (int i = 0; i < cancelledList.size(); i++) {
            cancelledListArray.add(cancelledList.get(i).getNameAsString());
        }

        cancelledListView = binding.rejectedList;
        cancelledAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.list_simple_view,  cancelledListArray);
        cancelledListView.setAdapter(cancelledAdapter);
        Log.d("TAG", "onViewCreated: ");

        binding.toSampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bundle bundle = new Bundle();
//                ArrayList<String> waitListArray = new ArrayList<>();
//                for (int i = 0; i < waitList.size(); i++) {
//                    waitListArray.add(waitList.get(i).getNameAsString());
//                }
                bundle.putStringArrayList("wait", waitListArray);
                bundle.putStringArrayList("invited", invitedListArray);
                bundle.putStringArrayList("cancelled", cancelledListArray);

                Intent intent = new Intent(getActivity(), OrganizerEditActivity.class);
                startActivity(intent);


            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    //Called when an Entrant cancels or rejects invitation
    public void CancelledEntrant(Entrant entrant){
        invitedList.remove(entrant);
        waitList.remove(entrant);
        invitedList.add(organizerEditActivity.redrawEntrant());
        cancelledList.add(entrant);
        UpdateListDisplay();
    }

    private void UpdateListDisplay() {
        waitlistAdapter.notifyDataSetChanged();
        invitedAdapter.notifyDataSetChanged();
        cancelledAdapter.notifyDataSetChanged();
    }
}
