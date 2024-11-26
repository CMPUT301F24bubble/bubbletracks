package com.example.bubbletracksapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.bubbletracksapp.databinding.OrganizerEventHostingBinding;
//
//public class OrganizerEventHosting extends AppCompatActivity {
//    private OrganizerEventHostingBinding binding;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = OrganizerEventHostingBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//
//
//        binding.waitlistButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(OrganizerEventHosting.this, OrganizerEditActivity.class);
//                intent.putParcelableArrayListExtra("wait", waitList);
//                intent.putParcelableArrayListExtra("invited", invitedList);
//                intent.putParcelableArrayListExtra("rejected", rejectedList);
//                intent.putParcelableArrayListExtra("cancelled", cancelledList);
//
//                startActivity(intent);
//            }
//        });
//
//    }
//}


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.bubbletracksapp.databinding.ListsBinding;
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
public class OrganizerEventHosting extends Fragment{
    private ListsBinding binding;

    EventDB eventDB = new EventDB();
    ArrayList<Event> hostedEvents = new ArrayList<>();

    Context context;
    public Entrant currentUser;


    ListView eventListView;
    EventHostListAdapter eventListAdapter;

    private ActivityResultLauncher<String> uploadImageLauncher;
    private Event newEventImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the ActivityResultLauncher here
        uploadImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                uploadNewImage(uri);
            }
        });
    }

    /**
     * Initialize the layout of the organizer user interface
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return binding of the layout
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ListsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    /**
     * Additional view creations
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        MainActivity mainActivity = (MainActivity)getActivity();
        currentUser = mainActivity.currentUser;

        eventListView = binding.reusableListView;

        eventDB.getEventList(currentUser.getEventsOrganized()).thenAccept(events -> {
            if(events != null){
                Log.d("AHHHHHHHHHHHHHHHHHHHHHHHHHHHH", "events is not null");
                hostedEvents = events;
                eventListAdapter = new EventHostListAdapter(this.getContext(), hostedEvents, this);
                eventListView.setAdapter(eventListAdapter);

            } else {
                Toast.makeText(context, "No hosted events", Toast.LENGTH_LONG).show();
                Log.d("AHHHHHHHHHHHHHHHHHHHHHHHHHHHH", "this will catch my attention");
            }
        });

    }

    /**
     * destroy the view created
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updatePoster(Event event){

        newEventImage = event;
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        uploadImageLauncher.launch("image/*");
    }

    protected void uploadNewImage(Uri uri){
        String filename = "posters/" + System.currentTimeMillis() + ".jpg";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filename);

        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        // get the download url of the image
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                            @Override
                            public void onSuccess(Uri uri) {

                                // store the download string in the event class
                                String downloadUrl = uri.toString();
                                newEventImage.setImage(downloadUrl);

                                eventDB.updateEvent(newEventImage);

                                Toast.makeText(getContext(), "Poster has been updated", Toast.LENGTH_SHORT).show();

                            }

                            // handle errors
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    // handle errors
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}