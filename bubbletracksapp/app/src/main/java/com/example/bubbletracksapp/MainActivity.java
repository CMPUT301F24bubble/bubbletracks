package com.example.bubbletracksapp;

import static java.util.UUID.randomUUID;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.bubbletracksapp.databinding.HomescreenBinding;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

/**
 *Main Activity for the user.
 * @author Zoe
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private HomescreenBinding binding;
    public Entrant currentUser;
    private String currentDeviceID;

    /**
     * Set up creation of activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HomescreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EntrantDB db = new EntrantDB();

        currentDeviceID = getDeviceID();
        Log.d("DeviceID:",currentDeviceID);

        db.getEntrant(currentDeviceID).thenAccept(user -> {
            if(user != null){
                currentUser = user;
            } else {
                currentUser = new Entrant(currentDeviceID);
                db.addEntrant(currentUser);
                Log.d("Added new Entrant",currentUser.getID());}
        }).exceptionally(e -> {
            Toast.makeText(MainActivity.this, "Failed to load user: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });

        Button entrantButton = binding.buttonEntrant;
        Button organizerButton = binding.buttonOrganizer;
        Button adminButton = binding.buttonAdmin;

        organizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, OrganizerFragment.class, null)
                        .setReorderingAllowed(true)
                        //.addToBackStack("") // Having this on the backstack can be annoying.
                        .commit();
                }
            });

        entrantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, EntrantFragment.class, null)
                        .setReorderingAllowed(true)
                        //.addToBackStack("") // Having this on the backstack can be annoying.
                        .commit();
            }
        });

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, AdminFragment.class, null)
                        .setReorderingAllowed(true)
                        //.addToBackStack("") // Having this on the backstack can be annoying.
                        .commit();
            }
        });
    }

    /**
     * Create the activity involving the options menu
     * @param menu The options menu in which you place your items.
     *
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Identify what was clicked among the options menu
     * @param item The menu item that was selected.
     *
     * @return true
     */
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
     * This function checks to see if the user has a local ID stored.
     * If they don't it generates one for them.
     * Either way, it returns the ID.
     * @return device ID
     **/
    public String getDeviceID() {
        // The first two lines of this function can be used in any activity
        // To fetch the current device id. You can expect it to never be "Not Found" elsewhere.
        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Not Found");
        if (ID.equals("Not Found")) {
            ID = randomUUID().toString();
            SharedPreferences.Editor editor = localID.edit();
            editor.putString("ID", ID);
            editor.apply();
        };
        return ID;
    }
}