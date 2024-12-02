package com.example.bubbletracksapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class tests the UI for Organizer Facility Activity Test
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerFacilityActivityTest {

    @Rule
    public ActivityScenarioRule<OrganizerFacilityActivity> scenario = new ActivityScenarioRule<OrganizerFacilityActivity>(OrganizerFacilityActivity.class);

    /**
     * tests the input of facility name
     */
    @Test
    public void testFacilityName() {

        // set the name
        onView(withId(R.id.facility_name_edit)).perform(ViewActions.typeText("Swimming Pool"));

        // check if the correct name is reflected
        onView(withId(R.id.facility_name_edit)).check(matches(withText("Swimming Pool")));
    }
}
