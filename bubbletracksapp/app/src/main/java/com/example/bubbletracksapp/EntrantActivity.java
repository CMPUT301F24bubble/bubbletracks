package com.example.bubbletracksapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * This is a class for the entrants' activities with events
 * @author Erza
 */
public class EntrantActivity extends AppCompatActivity {
    Event event = new Event();
    Boolean inWaitlist = false;
    Entrant entrant = new Entrant();
    private final ArrayList<Entrant> entrantArrayList = new ArrayList<>();

    /**
     * Initializes entrantActivity activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);



        Button waitlistButton = findViewById(R.id.join_waitlist_button);
        // Change button text depending if the entrant has joined the waitlist or not
        waitlistButton.setOnClickListener(view -> {
            showAlertDialog(waitlistButton, entrantArrayList);
        });
    }

    /**
     * This modifies whether the entrant wants to join or leave the waitlist for an event
     * @param waitlistButton
     *      adds/removes entrant from waitlist upon user click
     */
    private void showAlertDialog(Button waitlistButton, ArrayList<Entrant> entrantArrayList) {
        AlertDialog joinDialog;
        if (!inWaitlist) { // Entrant wants to join waitlist
            joinDialog = new AlertDialog.Builder(EntrantActivity.this)
                    .setTitle("Confirm Joining Waitlist")
                    .setMessage("Are you sure you want to join the waitlist for this event?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        /**
                         * Action to confirm joining the waitlist
                         * @param dialogInterface the dialog that received the click
                         * @param i the button that was clicked (ex.
                         *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                         *              of the item clicked
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            event.addToWaitList(entrant.getID());
                            waitlistButton.setText(R.string.leave_waitlist);
                            inWaitlist = true;
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        /**
                         * Action to cancel confirmation
                         * @param dialogInterface the dialog that received the click
                         * @param i the button that was clicked (ex.
                         *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                         *              of the item clicked
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
        }
        else { // Entrant wants to leave waitlist
            joinDialog = new AlertDialog.Builder(EntrantActivity.this)
                    .setTitle("Confirm Leaving Waitlist")
                    .setMessage("Are you sure you want to leave the waitlist for this event?")
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        /**
                         * Action to confirm leaving the waitlist
                         * @param dialogInterface the dialog that received the click
                         * @param i the button that was clicked (ex.
                         *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                         *              of the item clicked
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            event.deleteFromWaitList(entrant.getID());
                            waitlistButton.setText(R.string.join_waitlist);
                            inWaitlist = false;
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        /**
                         * Action to cancel confirmation
                         * @param dialogInterface the dialog that received the click
                         * @param i the button that was clicked (ex.
                         *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                         *              of the item clicked
                         */
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
        }
        joinDialog.show();
    }
}
