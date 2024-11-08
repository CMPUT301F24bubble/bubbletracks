package com.example.bubbletracksapp;

import static java.util.UUID.randomUUID;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.bubbletracksapp.databinding.HomescreenBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bubbletracksapp.databinding.HomescreenBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private HomescreenBinding binding;
    public Entrant currentUser;
    private String currentDeviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HomescreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentDeviceID = getDeviceID();
        Log.d("DeviceID:",currentDeviceID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * This function is not implemented. It just returns a random string every time.
     * https://firebase.google.com/docs/projects/manage-installations#java_2
     **/
    public String getDeviceID() {
        return randomUUID().toString();
        //private final Result get(FirebaseInstallations.getInstance().getId());
    }
}