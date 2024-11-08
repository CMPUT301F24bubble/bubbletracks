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

/**
 * Adapter between entrant and entrant list
 * @author Chester
 */
public class EntrantListAdapter extends ArrayAdapter<Entrant> {
    /**
     * Initialize the adapter with the list of entrants
     * @param context context of adapter
     * @param entrants list of entrants
     */
    public EntrantListAdapter(Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
    }

    /**
     * get the view of the list of entrant names
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.name_list,
                    parent, false);
        } else {
            view = convertView;
        }
        Entrant entrant = getItem(position);

        TextView entrantName = view.findViewById(R.id.left_text_view);
        entrantName.setText(entrant.getNameAsString());


        return view;
    }
}
