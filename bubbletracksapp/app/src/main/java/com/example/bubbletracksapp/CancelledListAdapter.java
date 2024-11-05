package com.example.bubbletracksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CancelledListAdapter extends ArrayAdapter<Entrant>{
    interface CancelledEntrantI {
        void redrawCancelledEntrant(Entrant entrant);
    }
    private CancelledEntrantI listener;

    public CancelledListAdapter(Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
        if (context instanceof CancelledEntrantI) {
            listener = (CancelledEntrantI) context;
        } else {
            throw new RuntimeException(context + " must implement CancelledEntrantI");
        }

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.name_button_list,
                    parent, false);
        } else {
            view = convertView;
        }
        Entrant entrant = getItem(position);
        TextView entrantName = view.findViewById(R.id.left_text_view);
        entrantName.setText(entrant.getNameAsString());

        Button redrawEntrant = view.findViewById(R.id.right_button_view);
        redrawEntrant.setText("Redraw");

        redrawEntrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.redrawCancelledEntrant(entrant);
                redrawEntrant.setEnabled(false);
            }
        });


        return view;

    }
}
