package com.example.bubbletracksapp;

import java.util.List;

/**
 * Organizer actions for entrants
 */
public class OrganizerEntrantListActivity {

    //To be changed to waitlist class INCOMPLETE
    List<Entrant> waitlist;
    List<Entrant> invitedList;
    List<Entrant> cancelledList;
    int maximumNumberOfEntrants;
    OrganizerEditActivity organizerEditActivity = new OrganizerEditActivity();


    //Called when an Entrant cancels or rejects invitation
    public void CancelledEntrant(Entrant entrant){
        invitedList.add(organizerEditActivity.redrawEntrant());
        cancelledList.add(entrant);
        UpdateListDisplay();
    }

    private void UpdateListDisplay() {
    }
}
