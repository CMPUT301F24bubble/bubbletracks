package com.example.bubbletracksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class InvitedListAdapter extends ArrayAdapter<Entrant> {
    public InvitedListAdapter(Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.text_text_list,
                    parent, false);
        } else {
            view = convertView;
        }
        Entrant entrant = getItem(position);

        TextView entrantName = view.findViewById(R.id.left_text_view);
        entrantName.setText(entrant.getNameAsString());

        TextView entrantState = view.findViewById(R.id.right_text_view);
        // SHOULD BE CHANGED TO STATE WHETHER THE ENTRANT JOINED THE EVENT
        entrantState.setText(entrant.getNameAsString());

        return view;
    }
}
