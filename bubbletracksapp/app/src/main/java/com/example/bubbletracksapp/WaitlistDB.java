package com.example.bubbletracksapp;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class WaitlistDB {
    FirebaseFirestore db;
    CollectionReference waitlistRef;

    public WaitlistDB() {
        db = FirebaseFirestore.getInstance();
        waitlistRef = db.collection("waitlists");
    }

    public void addEntrant(Entrant entrant)
    {
        String docID = entrant.getID();

        waitlistRef.document(docID);
        // to continue retrieving entrant document to add to waitlist
    }
}
