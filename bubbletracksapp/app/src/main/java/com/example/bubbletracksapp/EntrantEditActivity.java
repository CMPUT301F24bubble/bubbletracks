package com.example.bubbletracksapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.FragmentFirstBinding;
import com.example.bubbletracksapp.databinding.ProfileManagementBinding;

public class EntrantEditActivity extends AppCompatActivity {
    /**
     * This class allows entrant to update their profile information.
     * INCOMPLETE:
     * There is currently no data validation.
     * There is no way to set the profile picture.
     * There is no true current user; data is not entered into the database.
     */

    private ProfileManagementBinding binding;
    EntrantDB db = new EntrantDB();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ProfileManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText entrantNameInput = binding.entrantNameInput;
        EditText entrantEmailInput = binding.entrantEmailInput;
        EditText entrantPhoneInput = binding.entrantPhoneInput;
        TextView deviceIDNote = binding.deviceIDNote;

        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Device ID not found");

        // Must update to actually get the current user lol
        Entrant currentUser = new Entrant(new String[]{"name1", "name2"},"a@mail","123","xxx");

        deviceIDNote.setText(ID);
        entrantNameInput.setText(currentUser.getNameAsString());
        entrantEmailInput.setText(currentUser.getEmail());
        entrantPhoneInput.setText(currentUser.getPhone());

        Button updateProfile = binding.profileUpdate;
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] newFullName = entrantNameInput.getText().toString().split(" ");
                String newFirst = newFullName[0], newLast = newFullName[1];
                String newEmail = entrantEmailInput.getText().toString();
                String newPhone = entrantPhoneInput.getText().toString();

                currentUser.setName(newFirst, newLast);
                currentUser.setPhone(newPhone);
                currentUser.setEmail(newEmail);

                db.updateEntrant(currentUser);

                Log.d("New user name:", currentUser.getNameAsString());

            }
        });
    }
}
