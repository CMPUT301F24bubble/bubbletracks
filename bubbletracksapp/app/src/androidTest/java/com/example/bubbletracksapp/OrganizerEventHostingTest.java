package com.example.bubbletracksapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.widget.DatePicker;
import android.widget.TimePicker;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * this class tests the UI for OrganizerActivity class
 * incomplete - cannot test checkbox due to animation
 *              cannot test image picker and location
 *              as espresso does not provide a way to interact with those API
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class OrganizerEventHostingTest {

    @Rule
    public ActivityScenarioRule<OrganizerActivity> scenario = new ActivityScenarioRule<OrganizerActivity>(OrganizerActivity.class);

    /**
     * tests the input of event name
     */
    @Test
    public void testEventName(){

        // set the name
        onView(withId(R.id.textName)).perform(ViewActions.typeText("Dance Club"));

        // check if the correct name is reflected
        onView(withId(R.id.textName)).check(matches(withText("Dance Club")));
    }

    /**
     * tests the input of event date time
     */
    @Test
    public void testEventDateTime(){

        // click the button to open date time dialogue
        onView(withId(R.id.buttonDateTime)).perform(click());

        // set the date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 11, 30));

        // click ok
        onView(withId(android.R.id.button1)).perform(click());

        // set the time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(14, 0));

        // click ok
        onView(withId(android.R.id.button1)).perform(click());

        // check if the correct date time is reflected
        onView(withId(R.id.textDateTime)).check(matches(withText("2024-11-30 14:00")));
    }

    /**
     * tests the input of event description
     */
    @Test
    public void testEventDescription(){

        // set the description
        onView(withId(R.id.textDescription)).perform(ViewActions.typeText("Open Style Dance Classes"));

        // check if the correct description is reflected
        onView(withId(R.id.textDescription)).check(matches(withText("Open Style Dance Classes")));
    }

    /**
     * tests the input of event registration open date
     */
    @Test
    public void testEventRegistrationOpen(){

        // click the button to open date dialogue
        onView(withId(R.id.buttonRegistrationOpen)).perform(click());

        // set the date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 11, 15));

        // click ok
        onView(withId(android.R.id.button1)).perform(click());

        // check if the correct registration open is reflected
        onView(withId(R.id.textRegistrationOpen)).check(matches(withText("2024-11-15")));
    }

    /**
     * tests the input of event registration close
     */
    @Test
    public void testEventRegistrationClose(){

        // click the button to open date dialogue
        onView(withId(R.id.buttonRegistrationClose)).perform(click());

        // set the date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 11, 25));

        // click ok
        onView(withId(android.R.id.button1)).perform(click());

        // check if the correct registration open is reflected
        onView(withId(R.id.textRegistrationClose)).check(matches(withText("2024-11-25")));

    }

    /**
     * tests the input of event max capacity
     */
    @Test
    public void testEventMaxCapacity(){

        // set the capacity
        onView(withId(R.id.textMaxCapacity)).perform(ViewActions.typeText("30"));

        // check if the correct capacity is reflected
        onView(withId(R.id.textMaxCapacity)).check(matches(withText("30")));
    }

    /**
     * tests the input of event price
     */
    @Test
    public void testEventPrice(){

        // set the price
        onView(withId(R.id.textPrice)).perform(ViewActions.typeText("10"));

        // check if the correct price is reflected
        onView(withId(R.id.textPrice)).check(matches(withText("10")));
    }

    /**
     * tests the input of event wait list limit
     */
    @Test
    public void testEventWaitlistLimit(){

        // set the waitlist limit
        onView(withId(R.id.textWaitListLimit)).perform(ViewActions.typeText("10"));

        // check if the correct waitlist limit is reflected
        onView(withId(R.id.textWaitListLimit)).check(matches(withText("10")));
    }

}

