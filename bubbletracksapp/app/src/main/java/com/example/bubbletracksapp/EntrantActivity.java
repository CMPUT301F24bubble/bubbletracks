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

public class EntrantActivity extends AppCompatActivity {
    Entrant entrant;
    Boolean inWaitlist = false;
    ArrayList<Entrant> waitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        Button waitlistButton = findViewById(R.id.join_waitlist_button);
        // Change button text depending if the entrant has joined the waitlist or not
        waitlistButton.setOnClickListener(view -> {
            showAlertDialog(waitlistButton);
        });
    }

    private void showAlertDialog(Button waitlistButton) {
        if (!inWaitlist) {

            AlertDialog joinDialog = new AlertDialog.Builder(EntrantActivity.this)
                    .setTitle("Confirm Joining Waitlist")
                    .setMessage("Are you sure you want to join the waitlist for this event?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            waitlistButton.setText(R.string.leave_waitlist);
                            inWaitlist = true;
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            joinDialog.show();
        }
        else {
            AlertDialog joinDialog = new AlertDialog.Builder(EntrantActivity.this)
                    .setTitle("Confirm Leaving Waitlist")
                    .setMessage("Are you sure you want to leave the waitlist for this event?")
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            waitlistButton.setText(R.string.join_waitlist);
                            inWaitlist = false;
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            joinDialog.show();
        }
    }
}


    /*    // Confirmation dialog
        AlertDialog.Builder builder = getBuilder(waitlistButton);
        waitlistButton.setOnClickListener(v -> {
            AlertDialog dialog = builder.create();
            dialog.show();
            Toast.makeText(EntrantActivity.this, "Waitlist button clicked!", Toast.LENGTH_SHORT).show();
        });

    }
    // Confirmation screen
    private AlertDialog.Builder getBuilder(ExtendedFloatingActionButton waitlistButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons.
        if (!inWaitlist) {
            builder.setMessage(R.string.join_confirmation);
            builder.setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User confirms add to waitlist
                    // waitList.add(entrant);
                    // add entrant to waitlist database

                    inWaitlist = true;
                    waitlistButton.setText(R.string.leave_waitlist);
                    // change colour as well
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User goes back to screen without being added to waitlist
                }
            });
        }
        else {
            builder.setMessage(R.string.leave_confirmation);
            builder.setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User confirms remove from waitlist
                    // waitList.remove(entrant);
                    // remove entrant from waitlist database

                    inWaitlist = false;
                    waitlistButton.setText(R.string.join_waitlist);
                    // change colour as well
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User goes back to screen without being added to waitlist
                }
            });
        }
        return builder;
    }


}
*/