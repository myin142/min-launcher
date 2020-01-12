package myin.phone;

import android.content.Context;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import it.cosenonjaviste.daggermock.DaggerMockRule;
import myin.phone.views.home.HomeActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest extends DaggerMockRule<HomeActivity> {

    @Rule
    private ActivityScenarioRule<HomeActivity> scenarioRule = new ActivityScenarioRule<>(HomeActivity.class);

    public HomeActivityTest() {
        super(HomeActivity.class);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Espresso.onView(ViewMatchers.withId(R.id.apps_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));

        assertEquals("myin.phone", appContext.getPackageName());
    }
}
