package externius.rdmg;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import externius.rdmg.activities.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsAnything.anything;

@RunWith(AndroidJUnit4.class)
public class DungeonWithoutCorridorsTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            MainActivityTest.takeScreenshot(description);
        }
    };


    @Test
    public void testDefault() {
        onView(withId(R.id.generate_button)).perform(scrollTo());
        onView(withId(R.id.corridors)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.generate_button)).perform(scrollTo(), click());
        onView(withId(R.id.dungeon_activity_generate_button)).perform(scrollTo(), click());
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testDefault");
    }

    @Test
    public void testMedium() {
        onView(withId(R.id.dungeon_size)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.generate_button)).perform(scrollTo());
        onView(withId(R.id.room_size)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.corridors)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.generate_button)).perform(scrollTo(), click());
        onView(withId(R.id.dungeon_activity_generate_button)).perform(scrollTo(), click());
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testMedium");
    }

    @Test
    public void testLarge() {
        onView(withId(R.id.dungeon_size)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.generate_button)).perform(scrollTo());
        onView(withId(R.id.room_size)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.corridors)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.generate_button)).perform(scrollTo(), click());
        onView(withId(R.id.dungeon_activity_generate_button)).perform(scrollTo(), click());
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testLarge");
    }
}
