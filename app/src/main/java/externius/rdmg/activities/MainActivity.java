package externius.rdmg.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Objects;

import externius.rdmg.R;
import externius.rdmg.database.DBOpenHelper;
import externius.rdmg.database.DungeonsProvider;
import externius.rdmg.helpers.MultiSelectMonster;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Spinner spinnerDifficulty;
    private Spinner spinnerPartyLevel;
    private Spinner spinnerPartySize;
    private Spinner spinnerTreasureValue;
    private Spinner spinnerItemsRarity;
    private Spinner spinnerSize;
    private Spinner spinnerRoomDens;
    private Spinner spinnerRoomSize;
    private Spinner spinnerTraps;
    private Spinner spinnerCorridors;
    private MultiSelectMonster spinnerMonsterType;
    private Spinner spinnerDeadEnds;
    private Spinner spinnerTheme;
    private Dialog loadDialog;
    private CursorAdapter cursorAdapter;
    private String filter;
    private Uri uri;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSpinners();
        setDefaultValues();
        addListeners();
        initDB();
        if (savedInstanceState != null) {
            spinnerMonsterType.setAllText(savedInstanceState.getString(DBOpenHelper.MONSTER_TYPE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DBOpenHelper.MONSTER_TYPE, spinnerMonsterType.getAllText());
    }

    @NonNull
    private AdapterView.OnItemSelectedListener getCorridorItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (Objects.equals(spinnerCorridors.getSelectedItem().toString(), "No")) {
                    spinnerTraps.setEnabled(false);
                    spinnerRoomDens.setEnabled(false);
                    spinnerDeadEnds.setEnabled(false);
                } else {
                    spinnerTraps.setEnabled(true);
                    spinnerRoomDens.setEnabled(true);
                    spinnerDeadEnds.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    private void initDB() {
        String[] from = {DBOpenHelper.DUNGEON_NAME};
        int[] to = {android.R.id.text1};
        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
        getLoaderManager().initLoader(0, null, this);
    }

    private void addListeners() {
        spinnerCorridors.setOnItemSelectedListener(getCorridorItemSelectedListener());
        final Button generateButton = findViewById(R.id.generate_button);
        generateButton.setOnClickListener(v -> generateDungeon());
        final Button loadButton = findViewById(R.id.load_button);
        loadButton.setOnClickListener(loadListener());
    }

    private void setDefaultValues() {
        spinnerDifficulty.setSelection(1);
        spinnerPartySize.setSelection(3);
        spinnerTraps.setSelection(1);
        spinnerTreasureValue.setSelection(1);
        spinnerItemsRarity.setSelection(1);
    }

    private void getSpinners() {
        spinnerDifficulty = findViewById(R.id.dungeon_difficulty);
        spinnerPartyLevel = findViewById(R.id.party_level);
        spinnerPartySize = findViewById(R.id.party_size);
        spinnerTreasureValue = findViewById(R.id.treasure_value);
        spinnerItemsRarity = findViewById(R.id.items_rarity);
        spinnerSize = findViewById(R.id.dungeon_size);
        spinnerRoomDens = findViewById(R.id.room_density);
        spinnerRoomSize = findViewById(R.id.room_size);
        spinnerTraps = findViewById(R.id.traps);
        spinnerCorridors = findViewById(R.id.corridors);
        spinnerMonsterType = findViewById(R.id.monster_type);
        spinnerDeadEnds = findViewById(R.id.dead_end);
        spinnerTheme = findViewById(R.id.theme);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadDialog != null) {
            loadDialog.dismiss();
        }
        if (spinnerMonsterType.getDialog() != null) {
            spinnerMonsterType.getDialog().dismiss();
        }
    }

    @NonNull
    private View.OnClickListener loadListener() {
        return view -> showLoadDialog();
    }

    private void showLoadDialog() {
        loadDialog = new Dialog(this, R.style.Dialog);
        loadDialog.setContentView(R.layout.load_dungeons_popup);
        loadDialog.setTitle("Select saved dungeon");
        Button closePopupBtn;
        Button deleteAllBtn;
        closePopupBtn = loadDialog.findViewById(R.id.popup_close_button);
        deleteAllBtn = loadDialog.findViewById(R.id.delete_all_dungeon_button);
        setButtonStyle(closePopupBtn);
        setButtonStyle(deleteAllBtn);
        getDataFromDB(loadDialog);
        // close the dialog window
        closePopupBtn.setOnClickListener(view -> loadDialog.dismiss());
        // delete all records
        deleteAllBtn.setOnClickListener(view -> deleteAllData());
        loadDialog.show();
    }

    private void setButtonStyle(Button button) {
        button.setBackgroundColor(getResources().getColor(R.color.primaryAccent));
        button.setTextColor(Color.WHITE);
    }

    private void deleteAllData() {
        DialogInterface.OnClickListener dialogClickListener =
                (dialog, button) -> {
                    if (button == DialogInterface.BUTTON_POSITIVE) {
                        getContentResolver().delete(DungeonsProvider.CONTENT_URI, null, null);
                        restartLoader();
                        Toast.makeText(MainActivity.this,
                                getString(R.string.all_deleted),
                                Toast.LENGTH_SHORT).show();
                    }
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (list.getCount() > 0) {
            builder.setMessage(getString(R.string.delete_confirmation_text))
                    .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                    .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                    .show();
        }
    }

    private void getDataFromDB(Dialog dialog) {
        restartLoader();
        list = dialog.findViewById(R.id.load_dungeon_list);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener((adapterView, view, i, l) -> getSelectedDungeon(l));
        list.setOnItemLongClickListener((adapterView, view, i, l) -> {
            deleteSingleDungeon(l);
            return true;
        });
    }

    private void deleteSingleDungeon(long id) {
        getUriAndFilter(id);
        DialogInterface.OnClickListener dialogClickListener =
                (dialog, button) -> {
                    if (button == DialogInterface.BUTTON_POSITIVE) {
                        getContentResolver().delete(DungeonsProvider.CONTENT_URI, filter, null);
                        restartLoader();
                        Toast.makeText(MainActivity.this,
                                getString(R.string.selected_deleted),
                                Toast.LENGTH_SHORT).show();
                    }
                };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_confirmation_single_text))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void getSelectedDungeon(long id) {
        getUriAndFilter(id);
        Cursor cursor = getContentResolver().query(uri, DBOpenHelper.ALL_COLUMNS, filter, null, null);
        loadDungeon(cursor);
    }

    private void getUriAndFilter(long id) {
        uri = Uri.parse(DungeonsProvider.CONTENT_URI + "/" + id);
        filter = DBOpenHelper.DUNGEON_ID + "=" + uri.getLastPathSegment();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void loadDungeon(Cursor cursor) {
        Intent dungeon = new Intent(this, DungeonActivity.class);
        Bundle extras = new Bundle();
        cursor.moveToFirst();
        for (String column : DBOpenHelper.TO_BUNDLE) {
            extras.putString(column, cursor.getString(cursor.getColumnIndex(column)));
        }
        cursor.close();
        extras.putString("URI", uri.toString());
        extras.putString("FILTER", filter);
        extras.putString("THEME", spinnerTheme.getSelectedItem().toString());
        dungeon.putExtras(extras);
        setSpinners(extras);
        startActivity(dungeon);
        loadDialog.dismiss();
    }

    private void setSpinners(Bundle extras) {
        spinnerDifficulty.setSelection(getIndex(spinnerDifficulty, extras.getString(DBOpenHelper.DUNGEON_DIFFICULTY)));
        spinnerPartyLevel.setSelection(getIndex(spinnerPartyLevel, extras.getString(DBOpenHelper.PARTY_LEVEL)));
        spinnerPartySize.setSelection(getIndex(spinnerPartySize, extras.getString(DBOpenHelper.PARTY_SIZE)));
        spinnerTreasureValue.setSelection(getIndex(spinnerTreasureValue, extras.getString(DBOpenHelper.TREASURE_VALUE)));
        spinnerItemsRarity.setSelection(getIndex(spinnerItemsRarity, extras.getString(DBOpenHelper.ITEMS_RARITY)));
        spinnerSize.setSelection(getIndex(spinnerSize, extras.getString(DBOpenHelper.DUNGEON_SIZE)));
        spinnerRoomDens.setSelection(getIndex(spinnerRoomDens, extras.getString(DBOpenHelper.ROOM_DENSITY)));
        spinnerRoomSize.setSelection(getIndex(spinnerRoomSize, extras.getString(DBOpenHelper.ROOM_SIZE)));
        spinnerTraps.setSelection(getIndex(spinnerTraps, extras.getString(DBOpenHelper.TRAPS)));
        spinnerCorridors.setSelection(getIndex(spinnerCorridors, extras.getString(DBOpenHelper.CORRIDORS)));
        spinnerMonsterType.setAllText(extras.getString(DBOpenHelper.MONSTER_TYPE));
        spinnerDeadEnds.setSelection(getIndex(spinnerDeadEnds, extras.getString(DBOpenHelper.DEAD_ENDS)));
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void generateDungeon() {
        Intent dungeon = new Intent(this, DungeonActivity.class);
        Bundle extras = new Bundle();
        extras.putString(DBOpenHelper.LOADED_DUNGEON, null);
        extras.putString(DBOpenHelper.LOADED_ROOM_DESCRIPTION, null);
        extras.putString(DBOpenHelper.LOADED_TRAP_DESCRIPTION, null);
        extras.putString(DBOpenHelper.DUNGEON_DIFFICULTY, spinnerDifficulty.getSelectedItem().toString());
        extras.putString(DBOpenHelper.PARTY_LEVEL, spinnerPartyLevel.getSelectedItem().toString());
        extras.putString(DBOpenHelper.PARTY_SIZE, spinnerPartySize.getSelectedItem().toString());
        extras.putString(DBOpenHelper.TREASURE_VALUE, spinnerTreasureValue.getSelectedItem().toString());
        extras.putString(DBOpenHelper.ITEMS_RARITY, spinnerItemsRarity.getSelectedItem().toString());
        extras.putString(DBOpenHelper.DUNGEON_SIZE, spinnerSize.getSelectedItem().toString());
        extras.putString(DBOpenHelper.ROOM_DENSITY, spinnerRoomDens.getSelectedItem().toString());
        extras.putString(DBOpenHelper.ROOM_SIZE, spinnerRoomSize.getSelectedItem().toString());
        extras.putString(DBOpenHelper.TRAPS, spinnerTraps.getSelectedItem().toString());
        extras.putString(DBOpenHelper.CORRIDORS, spinnerCorridors.getSelectedItem().toString());
        extras.putString(DBOpenHelper.MONSTER_TYPE, spinnerMonsterType.getAllText());
        extras.putString(DBOpenHelper.DEAD_ENDS, spinnerDeadEnds.getSelectedItem().toString());
        extras.putString("THEME", spinnerTheme.getSelectedItem().toString());
        dungeon.putExtras(extras);
        startActivity(dungeon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, DungeonsProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
