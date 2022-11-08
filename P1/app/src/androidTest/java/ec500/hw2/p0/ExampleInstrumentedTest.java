package ec500.hw2.p0;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static final String STRING_TO_BE_TYPED = "25";
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void changeText_Activity() {
        // Type text and then press the button.
        onView(withId(R.id.edtFontSize))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.btnChangeSize)).perform(click());
    }

    @Test
    public void btnPause_newActivity() {
        // Type text and then press the button.
        onView(withId(R.id.btnPause)).perform(click());
        onView(withId(R.id.btnPause)).perform(click());
    }

    @Test
    public void btnTest_newActivity() {
        // Type text and then press the button.
        onView(withId(R.id.btnTest)).perform(click());
        onView(withId(R.id.btnTest)).perform(click());
    }

    @Test
    public void btnReset_newActivity() {
        // Type text and then press the button.
        onView(withId(R.id.btnReset)).perform(click());
        onView(withId(R.id.btnReset)).perform(click());
    }

}