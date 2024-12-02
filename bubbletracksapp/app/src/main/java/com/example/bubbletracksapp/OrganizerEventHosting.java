package com.example.bubbletracksapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bubbletracksapp.databinding.EventHostingListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

/**
 * Hold event that organizer is hosting
 * @author Chester
 */
public class OrganizerEventHosting extends AppCompatActivity implements EventHostListAdapter.EventHostI {
    private EventHostingListBinding binding;

    private final EventDB eventDB = new EventDB();
    private ArrayList<Event> hostedEvents = new ArrayList<>();

    Entrant currentUser;

    private ListView eventListView;
    private EventHostListAdapter eventListAdapter;

    private ActivityResultLauncher<String> uploadImageLauncher;
    private Event newEventImage;
    private ImageView posterImage;

    /**
     * Set up creation of activity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = EventHostingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize activity launcher to upload new image
        uploadImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                uploadNewImage(uri);
            }
        });

        Intent in = getIntent();
        try {
            currentUser = in.getParcelableExtra("user");
        } catch (Exception e) {
            Log.e("OrganizerEventHosting", "User extra was not passed correctly");
            throw new RuntimeException(e);
        }

        eventListView = binding.reusableListView;

        eventListAdapter = new EventHostListAdapter(OrganizerEventHosting.this, hostedEvents, this);
        eventListView.setAdapter(eventListAdapter);

        eventDB.getEventList(currentUser.getEventsOrganized()).thenAccept(events -> {
            if (events != null) {
                hostedEvents.clear();
                hostedEvents.addAll(events);
                eventListAdapter.notifyDataSetChanged();

                Log.d("OrganizerEventHosting", "Hosted events loaded");
            } else {
                Toast.makeText(OrganizerEventHosting.this, "No hosted events", Toast.LENGTH_LONG).show();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Go back to the home screen
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerEventHosting.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Switches view to the view of the waitlist of entrants
     * @param event event being viewed
     */
    @Override
    public void viewWaitlist(Event event) {
        Intent intent = new Intent(OrganizerEventHosting.this, OrganizerEditActivity.class);
        intent.putExtra("event", event);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

    /**
     * Switches view to the view of editing the event
     * @param event event being edited
     */
    @Override
    public void editEvent(Event event) {
//        Intent intent = new Intent(OrganizerEventHosting.this, OrganizerEditEventActivity.class);
//        intent.putExtra("event", event);
//        intent.putExtra("user", currentUser);
//        startActivity(intent);
    }

    /**
     * Uploads new poster by launching activity launcher
     * @param event event for which the poster is updated
     */
    public void updatePoster(Event event, ImageView posterImage){
        newEventImage = event;
        this.posterImage = posterImage;
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        uploadImageLauncher.launch("image/*");
    }

    /**
     * Uploads the image and updates the event in the database
     * @param uri uri of the image to be stored
     */
    protected void uploadNewImage(Uri uri){

        // get the file path
        String filename = "posters/" + newEventImage.getId() + "poster.jpg";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filename);

        storageReference.putFile(uri)
                // upload file
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // get the download url of the image
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // update the download string in the event class and update the event
                                String downloadUrl = downloadUri.toString();
                                newEventImage.setImage(downloadUrl);
                                eventDB.updateEvent(newEventImage);
                                posterImage.setImageURI(uri);
                                Toast.makeText(OrganizerEventHosting.this, "Poster has been updated", Toast.LENGTH_SHORT).show();
                            }
                        // handle errors
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OrganizerEventHosting.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                // handle errors
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrganizerEventHosting.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}