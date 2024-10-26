package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.bubbletracksapp.databinding.OrganizerWaitlistSampleBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrganizerEditActivity extends Fragment {
    // SHOUL BE ENTant INCOMPLETE
    List<String> waitList;
    List<String> invitedList;
    List<String> rejectedList;

    private OrganizerWaitlistSampleBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = OrganizerWaitlistSampleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // INCOMPLETE
//        ArrayList<String> test= getArguments().getStringArrayList("wait");
//        waitList.addAll(test);
//        invitedList.addAll(getArguments().getStringArrayList("invited"));
//        rejectedList.addAll(getArguments().getStringArrayList("cancelled"));


        binding.drawEntrants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nText = binding.numberToDraw;
                String nStr = nText.getText().toString();
                int n = Integer.parseInt(nStr);
                invitedList = drawEntrants(waitList, n);
            }
        });

        binding.backToWaitlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),OrganizerEditActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    // should return error if n is bigger than the size of waitlist INCOMPLETE
    public List<String> drawEntrants(List<String> waitlist, int n) {
        Collections.shuffle(waitlist);
        List<String> acceptedList = waitlist.subList(0, n);
        rejectedList = waitlist.subList(n, waitlist.size());

        return acceptedList;
    }

    // should return error if the list is empty INCOMPLETE
    public Entrant redrawEntrant() {
        Collections.shuffle(rejectedList);
        Entrant chosenEntrant = new Entrant();// rejectedList.get(0);INCOMPLETE
        rejectedList.remove(0);

        return chosenEntrant;
    }

    // should return error if n is bigger than the size of list INCOMPLETE
    public List<String> redrawEntrants(int n) {
        Collections.shuffle(rejectedList);
        List<String> chosenEntrants = rejectedList.subList(0, n);
        rejectedList = rejectedList.subList(n, rejectedList.size());

        return chosenEntrants;
    }

}
