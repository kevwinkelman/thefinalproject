package kevinwinkelman.androidlist;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    DatabaseManager db;
    EditText nameEditText;
    EditText hoursEditText;
    EditText minutesEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        nameEditText = (EditText)findViewById(R.id.input_name);
        hoursEditText = (EditText)findViewById(R.id.input_hours);
        minutesEditText = (EditText)findViewById(R.id.input_minutes);

        openDB();
        populateData();
        listViewItemClick();
        listLongClick();

    }

    private void openDB() {
        db = new DatabaseManager(this);
        db.open();
    }

    public void onClick_Add (View view) {
        if(!TextUtils.isEmpty(nameEditText.getText().toString())){
            db.insertRow(nameEditText.getText().toString(), hoursEditText.getText().toString(), minutesEditText.getText().toString());
        }

        populateData();
    }

    private void populateData() {
        Cursor cursor = db.getAllRows();
        String[] fromFieldNames = new String[] {
                DatabaseManager.KEY_ROWID, DatabaseManager.KEY_ACTIVITY, DatabaseManager. KEY_HOURS, DatabaseManager.KEY_MINUTES};

        int [] toViewIDs = new int[] {R.id.number, R.id.name, R.id.hours, R.id.minutes};

        SimpleCursorAdapter myCursorAdapter;

        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);
        ListView myList = (ListView) findViewById(R.id.list_item);
        myList.setAdapter(myCursorAdapter);



        }

        private void updateActivity(long id) {

        Cursor cursor = db.getRow(id);

        if (cursor.moveToFirst()){
            String activity_ = nameEditText.getText().toString();
            String hours_ = hoursEditText.getText().toString();
            String minutes_ = minutesEditText.getText().toString();
            db.updateRow(id, activity_, hours_, minutes_);
        }


        cursor.close();

        }

        private void listViewItemClick() {
        ListView list = (ListView) findViewById(R.id.list_item);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateActivity(id);
                populateData();
            }


        });

        }

        public void deleteSingleActivity (View view) {
        db.deleteAll();
        populateData();

        }

        private void listLongClick() {
        ListView myList = (ListView) findViewById(R.id.list_item);
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                db.deleteRow(id);
                populateData();

                return false;

            }
        });

        }




    }

