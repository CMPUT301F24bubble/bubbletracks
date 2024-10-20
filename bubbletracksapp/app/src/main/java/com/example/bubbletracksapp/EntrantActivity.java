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

public class EntrantActivity extends AppCompatActivity {
    Entrant entrant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrant_main);

        AlertDialog.Builder builder = getBuilder();
        // Create the AlertDialog.

        ExtendedFloatingActionButton fab = findViewById(R.id.join_waitlist);
        fab.setOnClickListener(v -> {
            AlertDialog dialog = builder.create();
            dialog.show();
            Toast.makeText(EntrantActivity.this, "Waitlist button clicked!", Toast.LENGTH_SHORT).show();
        });

    }

    private AlertDialog.Builder getBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons.
        builder.setPositiveButton(R.string.Enter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User taps OK button.
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancels the dialog.
            }
        });
        return builder;
    }


}
