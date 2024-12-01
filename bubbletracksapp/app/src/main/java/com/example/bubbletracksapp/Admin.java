package com.example.bubbletracksapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin class to manage administrative actions like deleting events and profiles
 * from Firestore and handling related updates for both events and profiles.
 * @author Gwen
 */
public class Admin {

    private Context context;

    /**
     * Constructor to initialize the Admin class with the provided context.
     *
     * @param context The context where the Admin actions will take place (e.g., Activity or Application context).
     */
    public Admin(Context context){
        this.context = context;
    }

    /**
     * Deletes an event from the "events" collection and removes references to the event
     * from the corresponding lists in the "entrants" collection.
     *
     * @param context The context where the deletion is being performed.
     * @param event The event that needs to be deleted.
     */
    public void deleteEvent(Context context, Event event) {
        if (event == null||event.getId()==null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventId = event.getId();
        WriteBatch batch = db.batch();

        DocumentReference eventRef = db.collection("events").document(eventId);
        batch.delete(eventRef);

        // subcategories to check in entrants' data
        List<String> subcategories = Arrays.asList("enrolled", "invited", "organized", "waitlist");

        // event reference from entrants' lists
        db.collection("entrants")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot entrantDoc : querySnapshot.getDocuments()) {
                        DocumentReference entrantRef = entrantDoc.getReference();
                        Map<String, Object> entrantData = entrantDoc.getData();

                        if (entrantData != null) {
                            for (String subcategory : subcategories) {
                                if (entrantData.containsKey(subcategory)) {
                                    List<String> eventList = (List<String>) entrantData.get(subcategory);
                                    if (eventList != null && eventList.contains(eventId)) {
                                        eventList.remove(eventId);
                                        batch.update(entrantRef, subcategory, eventList);
                                    }
                                }
                            }
                        }
                    }

                    // remove event reference from facility
                    eventRef.get()
                            .addOnSuccessListener(eventSnapshot -> {
                                if (eventSnapshot.exists()) {
                                    String facilityId = eventSnapshot.getString("facility");
                                    if (facilityId != null) {
                                        DocumentReference facilityRef = db.collection("facilities").document(facilityId);

                                        facilityRef.get()
                                                .addOnSuccessListener(facilitySnapshot -> {
                                                    if (facilitySnapshot.exists()) {
                                                        List<String> facilityEvents = (List<String>) facilitySnapshot.get("events");
                                                        if (facilityEvents != null && facilityEvents.contains(eventId)) {
                                                            facilityEvents.remove(eventId);
                                                            batch.update(facilityRef, "events", facilityEvents);
                                                        }
                                                    }

                                                    batch.commit()
                                                            .addOnSuccessListener(aVoid -> {
                                                                Toast.makeText(context, "Event and references deleted successfully", Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.e("DeleteEvent", "Error deleting event or updating references: ", e);
                                                                Toast.makeText(context, "Failed to delete event. Try again.", Toast.LENGTH_SHORT).show();
                                                            });
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e("DeleteEvent", "Error fetching facility data: ", e);
                                                    Toast.makeText(context, "Failed to fetch facility data. Try again.", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        // facility
                                        batch.commit()
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(context, "Event and references deleted successfully", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e("DeleteEvent", "Error deleting event or updating references: ", e);
                                                    Toast.makeText(context, "Failed to delete event. Try again.", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("DeleteEvent", "Error fetching event data: ", e);
                                Toast.makeText(context, "Failed to fetch event data. Try again.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteEvent", "Error fetching entrant data: ", e);
                    Toast.makeText(context, "Failed to fetch related data. Try again.", Toast.LENGTH_SHORT).show();
                });
    }


    /**
     * Deletes an entrant's profile from the "entrants" collection and removes any
     * references to the profile from the corresponding lists in the "events" collection.
     * Deletes associated facility, which deletes organized events
     *
     * @param context The context where the deletion is being performed.
     * @param entrantToDelete The entrant object whose profile needs to be deleted.
     */
    public void deleteEntrant(Context context, Entrant entrantToDelete) {
        if (entrantToDelete == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String profileId = entrantToDelete.getID();
        WriteBatch batch = db.batch();

        DocumentReference profileRef = db.collection("entrants").document(profileId);

        // the associated facility if present
        String facilityId = entrantToDelete.getFacility();
        if (facilityId != null && !facilityId.isEmpty()) {
            // Get a reference to the facility document in Firestore
            DocumentReference facilityRef = db.collection("facilities").document(facilityId);

            // Fetch the document asynchronously
            facilityRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Convert the document snapshot to a Facility object
                    Facility facility = documentSnapshot.toObject(Facility.class);

                    // If a Facility object is retrieved, delete it using FacilityDB
                    if (facility != null) {
                        FacilityDB facilityDB = new FacilityDB();
                        facilityDB.deleteFacility(facility); // Delete the facility
                    } else {
                        Log.e("DeleteFacility", "Failed to convert document to Facility object");
                    }
                } else {
                    Log.e("DeleteFacility", "Facility document not found");
                }
            }).addOnFailureListener(e -> {
                Log.e("DeleteFacility", "Error fetching facility document: ", e);
            });
        }
//        List<String> subcategories = Arrays.asList("cancelled","enrolled", "invited", "rejected", "wait");
//        // delete entrant references in events
//        db.collection("events")
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    for (DocumentSnapshot eventDoc : querySnapshot.getDocuments()) {
//                        DocumentReference eventRef = eventDoc.getReference();
//                        Map<String, Object>eventData = eventDoc.getData();
//
//                        if (eventData != null) {
//                            for (String subcategory : subcategories) {
//                                if (eventData.containsKey(subcategory)) {
//                                    List<String> eventList = (List<String>) eventData.get(subcategory);
//                                    if (eventList != null && eventList.contains(profileId)) {
//                                        eventList.remove(profileId);
//                                        batch.update(eventRef, subcategory, eventList);
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    // Commit the batch after removing references from events
//                    batch.commit()
//                            .addOnSuccessListener(aVoid -> {
//                                Log.d("RemoveEntrantFromEvents", "Successfully removed profileId from event subcategories.");
//                            })
//                            .addOnFailureListener(e -> {
//                                Log.e("RemoveEntrantFromEvents", "Error removing profileId from event subcategories.", e);
//                            });
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("RemoveEntrantFromEvents", "Error fetching event documents: ", e);
//                });

        profileRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> entrantData = documentSnapshot.getData();

                        if (entrantData != null && entrantData.containsKey("organized")) {
                            List<String> organizedEventIds = (List<String>) entrantData.get("organized");

                            if (organizedEventIds != null && !organizedEventIds.isEmpty()) {
                                // get each event and delete it
                                for (String eventId : organizedEventIds) {
                                    DocumentReference eventRef = db.collection("events").document(eventId);

                                    eventRef.get()
                                            .addOnSuccessListener(eventdocumentSnapshot -> {
                                                if (eventdocumentSnapshot.exists()) {
                                                    Event event = eventdocumentSnapshot.toObject(Event.class);
                                                    if (event != null) {
                                                        deleteEvent(context, event);
                                                    } else {
                                                        Log.e("EventID to object", "Event conversion failed for eventId: " + eventId);
                                                    }
                                                } else {
                                                    Log.e("EventID to object", "Event document does not exist for eventId: " + eventId);
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("EventID to object", "Error fetching event document for eventId: " + eventId, e);
                                            });
                                }

                            }
                        }

                        batch.delete(profileRef);

                        batch.commit()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(context, "Entrant and their events deleted successfully", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("DeleteEntrant", "Error deleting entrant: ", e);
                                    Toast.makeText(context, "Failed to delete entrant. Try again.", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteEntrant", "Error fetching entrant data: ", e);
                    Toast.makeText(context, "Failed to fetch entrant details. Try again.", Toast.LENGTH_SHORT).show();
                });

    }



    /**
     * Deletes a facility from the "facilities"
     * Deletes all events associated with the facility.
     * Removes facility reference from the organizer's document.
     * Updates the organizer's role to "entrant".
     *
     * @param context The context where the deletion is being performed.
     * @param facilityRef The DocumentReference to the facility that needs to be deleted.
     */
    public void deleteFacility(Context context, DocumentReference facilityRef) {
        if (facilityRef == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        facilityRef.get().addOnSuccessListener(facilityDoc -> {
            if (facilityDoc.exists()) {
                Map<String, Object> facilityData = facilityDoc.getData();

                if (facilityData != null) {
                    List<String> eventIds = (List<String>) facilityData.get("events");
                    String organizerId = (String) facilityData.get("organizer");

                    // delete events in facility
                    if (eventIds != null && !eventIds.isEmpty()) {
                        for (String eventId : eventIds) {
                            DocumentReference eventRef = db.collection("events").document(eventId);
                            eventRef.get()
                                    .addOnSuccessListener(eventDocumentSnapshot -> {
                                        if (eventDocumentSnapshot.exists()) {
                                            Event event = eventDocumentSnapshot.toObject(Event.class);
                                            if (event != null) {
                                                event.setId(eventDocumentSnapshot.getId());
                                                deleteEvent(context, event);
                                                Log.e("deleting good", "nice");
                                            } else {
                                                Log.e("EventID to object", "Event object failed for eventId: " + eventId);
                                            }
                                        } else {
                                            Log.e("EventID to object", "Event document does not exist for eventId: " + eventId);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("EventID to object", "Error fetching event document for eventId: " + eventId, e);
                                    });
                        }

                    }

                    if (organizerId != null) {
                        DocumentReference organizerRef = db.collection("entrants").document(organizerId);
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("facility", "");
                        updates.put("role", "entrant");
                        organizerRef.update(updates);
                    }
                }

                facilityRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Convert the document snapshot to a Facility object
                        Facility facility = documentSnapshot.toObject(Facility.class);

                        // If a Facility object is retrieved, delete it using FacilityDB
                        if (facility != null) {
                            FacilityDB facilityDB = new FacilityDB();
                            facilityDB.deleteFacility(facility); // Delete the facility
                        } else {
                            Log.e("DeleteFacility", "Failed to convert document to Facility object");
                        }
                    } else {
                        Log.e("DeleteFacility", "Facility document not found");
                    }
                }).addOnFailureListener(e -> {
                    Log.e("DeleteFacility", "Error fetching facility document: ", e);
                });


        } else {
                Toast.makeText(context, "Facility not found!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("DeleteFacility", "Error fetching facility data: ", e);
            Toast.makeText(context, "Failed to fetch facility data. Try again.", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Removes The hash QRCode data
     * @param context The context this deletion occurs
     * @param event The event who's QR code is to be deleted
     */
    public void removeHashData(Context context, Event event) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(event.getId());

        // Replace the QRCode field with an empty string
        eventRef.update("QRCode", "")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "QrCode removed successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("RemoveHashData", "Error removing QrCode: ", e);
                    Toast.makeText(context, "Failed to remove QrCode. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }




} // remove this bracket later when implemented profile pictures



//
//    //**Profiles Picture**//
////Replace pfp with default image?
////Make sure there is a pfp getter and setter
////Make sure there actually is a pfp in the database?
//    public void resetProfileImage(int position) {
//        Entrant profile = getItem(position);
//        if (profile == null) {
//            return;
//        }
//
//        new AlertDialog.Builder(getContext())
//                .setTitle("Confirm Profile Image Reset")
//                .setMessage("Are you sure you want to reset this profile image? It will be replaced with the default image.")
//                .setPositiveButton("Reset Image", (dialog, which) -> {
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    String profileId = profile.getID();  // Profile ID to update
//
//                    WriteBatch batch = db.batch();
//
//                    DocumentReference profileRef = db.collection("entrants").document(profileId);
//
//                    String defaultImageUrl = "bubbletracksapp/app/src/main/res/drawable/pfp_placeholder.png"; // Adjust the URL if needed
//                    profile.setPfp(defaultImageUrl);  // Change to default pfp
//
//                    batch.update(profileRef, "pfp", profile.getPfp()); // Change "pfp" to whatever the picture is named in the firebase
//
//                    batch.commit()
//                            .addOnSuccessListener(aVoid -> {
//                                // No need to remove from adapter, just refresh the UI
//                                notifyDataSetChanged();
//                                Toast.makeText(getContext(), "Profile image reset to default successfully", Toast.LENGTH_SHORT).show();
//                            })
//                            .addOnFailureListener(e -> {
//                                Log.e("ResetProfileImage", "Error resetting profile image: ", e);
//                                Toast.makeText(getContext(), "Failed to reset profile image. Try again.", Toast.LENGTH_SHORT).show();
//                            });
//
//                    dialog.dismiss();
//                })
//                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
//                .create()
//                .show();
//    }
//
//}