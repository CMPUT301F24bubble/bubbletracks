package com.example.lotterypersonal;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // ATTRIBUTES
    private FirebaseFirestore db;
    private ListView eventListView;
    private ArrayList<Event> eventDataList;
    private EventAdapter adapter;



    // FIRESTORE DATABASE INSTANCE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);  // Initializes Firebase
        db = FirebaseFirestore.getInstance(); // Initialize Firestore instance
        setContentView(R.layout.activity_home); // Set the content view to activity_home.xml

        EdgeToEdge.enable(this); // Enable edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}