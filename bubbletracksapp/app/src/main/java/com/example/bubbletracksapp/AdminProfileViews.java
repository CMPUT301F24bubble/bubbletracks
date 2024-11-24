package com.example.bubbletracksapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bubbletracksapp.databinding.ListsBinding;

import java.util.ArrayList;

public class AdminProfileViews extends Fragment {
    private ListsBinding binding;

    EntrantDB entrantDB = new EntrantDB();
    ArrayList<Entrant> entrantsProfiles = new ArrayList<>();

    Context context;
    ListView profileListView;
    AdminEntrantListAdapter profileAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = ListsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

        profileListView = binding.reusableListView;

        // Fetch profiles using getAllEntrants()
        entrantDB.getAllEntrants().thenAccept(entrants -> {
            if (entrants != null && !entrants.isEmpty()) {
                entrantsProfiles = entrants;

                // Set up adapter
                profileAdapter = new AdminEntrantListAdapter(context, entrantsProfiles);
                profileListView.setAdapter(profileAdapter);
            } else {
                Toast.makeText(context, "No user profiles found", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Log.e("ProfileManagement", "Error fetching profiles", e);
            Toast.makeText(context, "Failed to fetch profiles", Toast.LENGTH_LONG).show();
            return null;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


