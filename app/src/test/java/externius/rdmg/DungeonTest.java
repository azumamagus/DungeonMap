package externius.rdmg;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.List;

import externius.rdmg.core.Dungeon;
import externius.rdmg.helpers.Utils;
import externius.rdmg.models.DungeonTile;
import externius.rdmg.models.Textures;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertTrue;


public class DungeonTest {

    private final Dungeon dungeon = new Dungeon(800, 800, 15, 10, 15, 0, true);

    @Rule
    public final TestName name = new TestName();

    @Before
    public void setup() {
        Utils.setDungeonDifficulty(1);
        Utils.setPartySize(4);
        Utils.setPartyLevel(1);
        Utils.setJson(UtilsTest.readJSON());
        Utils.setMonsterType("any");
        dungeon.init();
    }

    @Test
    public void testInit() {
        System.out.println(name.getMethodName());
        DrawTestDungeon.draw(dungeon.getDungeonTiles());
        assertTrue(dungeon.getRoomDescription().isEmpty());
    }

    @Test
    public void testGenerateRoom() {
        dungeon.generateRoom();
        System.out.println(name.getMethodName());
        DrawTestDungeon.draw(dungeon.getDungeonTiles());
        assertTrue(dungeon.getRoomDescription().size() == 2);
        System.out.println();
    }

    @Test
    public void testAddEntryPoint() {
        dungeon.generateRoom();
        dungeon.addEntryPoint();
        System.out.println(name.getMethodName());
        DrawTestDungeon.draw(dungeon.getDungeonTiles());
        List<DungeonTile> dungeonList = UtilsTest.twoDArrayToList(dungeon.getDungeonTiles());
        assertThat(dungeonList, hasItem(Matchers.<DungeonTile>hasProperty("texture", equalTo(Textures.ENTRY))));
        System.out.println();
    }

    @Test
    public void testGenerateCorridors() {
        dungeon.generateRoom();
        dungeon.addEntryPoint();
        dungeon.generateCorridors();
        System.out.println(name.getMethodName());
        DrawTestDungeon.draw(dungeon.getDungeonTiles());
        List<DungeonTile> dungeonList = UtilsTest.twoDArrayToList(dungeon.getDungeonTiles());
        assertThat(dungeonList, hasItem(Matchers.<DungeonTile>hasProperty("texture", equalTo(Textures.CORRIDOR))));
        System.out.println();
    }

    @Test
    public void testAddDeadEnds() {
        dungeon.generateRoom();
        dungeon.addEntryPoint();
        dungeon.generateCorridors();
        dungeon.addDeadEnds();
        System.out.println(name.getMethodName());
        DrawTestDungeon.draw(dungeon.getDungeonTiles());
        List<DungeonTile> dungeonList = UtilsTest.twoDArrayToList(dungeon.getDungeonTiles());
        assertThat(dungeonList, hasItem(Matchers.<DungeonTile>hasProperty("texture", equalTo(Textures.CORRIDOR))));
        System.out.println();
    }

}
