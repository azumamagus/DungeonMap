package externius.rdmg;


import android.content.pm.ActivityInfo;
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
public class DungeonTest {

    @Rule
    public final ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            MainActivityTest.takeScreenshot(description);
        }
    };

    @Test
    public void testDefault() {
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testDefault");
    }

    @Test
    public void testMediumSize() {
        onView(withId(R.id.dungeon_size)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testMediumSize");
    }

    @Test
    public void testLargeSize() {
        onView(withId(R.id.dungeon_size)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testLargeSize");
    }

    @Test
    public void testEasyDifficulty() {
        onView(withId(R.id.dungeon_difficulty)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testEasyDifficulty");
    }

    @Test
    public void testHardDifficulty() {
        onView(withId(R.id.dungeon_difficulty)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testHardDifficulty");
    }

    @Test
    public void testDeadlyDifficulty() {
        onView(withId(R.id.dungeon_difficulty)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testDeadlyDifficulty");
    }

    @Test
    public void testPartyLevel() {
        onView(withId(R.id.party_level)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testPartyLevel");
    }

    @Test
    public void testPartySize() {
        onView(withId(R.id.party_size)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testPartySize");
    }

    @Test
    public void testTreasureValue() {
        onView(withId(R.id.treasure_value)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testTreasureValue");
    }

    @Test
    public void testItemsMaxRarity() {
        onView(withId(R.id.theme)).perform(scrollTo());
        onView(withId(R.id.items_rarity)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testItemsMaxRarity");
    }

    @Test
    public void testRoomDensity() {
        onView(withId(R.id.theme)).perform(scrollTo());
        onView(withId(R.id.room_density)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testRoomDensity");
    }

    @Test
    public void testRoomSizeMedium() {
        onView(withId(R.id.theme)).perform(scrollTo());
        onView(withId(R.id.room_size)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testRoomSizeMedium");
    }

    @Test
    public void testRoomSizeLarge() {
        onView(withId(R.id.theme)).perform(scrollTo());
        onView(withId(R.id.room_size)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testRoomSizeLarge");
    }

    @Test
    public void testMonsterType() {
        onView(withId(R.id.load_button)).perform(scrollTo());
        onView(withId(R.id.monster_type)).perform(click());
        onData(anything()).atPosition(3).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testMonsterType");
    }

    @Test
    public void testWithOutTraps() {
        onView(withId(R.id.load_button)).perform(scrollTo());
        onView(withId(R.id.traps)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testWithOutTraps");
    }

    @Test
    public void testWithoutDeadEnds() {
        onView(withId(R.id.load_button)).perform(scrollTo());
        onView(withId(R.id.dead_end)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testWithoutDeadEnds");
    }

    @Test
    public void testTheme() {
        mainActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        onView(withId(R.id.load_button)).perform(scrollTo());
        onView(withId(R.id.theme)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        MainActivityTest.generateDungeon();
        MainActivityTest.checkDungeonUI(this.getClass().getName(), "testTheme");
    }
}
