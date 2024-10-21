package com.example.bubbletracksapp;

import java.util.Collections;
import java.util.List;

public class OrganizerEditActivity {
    List<Entrant> rejectedList;

    // should return error if n is bigger than the size of waitlist INCOMPLETE
    public List<Entrant> drawEntrants(List<Entrant> waitlist, int n) {
        Collections.shuffle(waitlist);
        List<Entrant> acceptedList = waitlist.subList(0, n);
        rejectedList = waitlist.subList(n, waitlist.size());

        return acceptedList;
    }

    // should return error if the list is empty INCOMPLETE
    public Entrant redrawEntrant() {
        Collections.shuffle(rejectedList);
        Entrant chosenEntrant = rejectedList.get(0);
        rejectedList.remove(0);

        return chosenEntrant;
    }

    // should return error if n is bigger than the size of list INCOMPLETE
    public List<Entrant> redrawEntrants(int n) {
        Collections.shuffle(rejectedList);
        List<Entrant> chosenEntrants = rejectedList.subList(0, n);
        rejectedList = rejectedList.subList(n, rejectedList.size());

        return chosenEntrants;
    }

}
