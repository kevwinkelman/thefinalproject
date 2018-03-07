package kevinwinkelman.androidlist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import android.content.ContentValues;

/**
 * Created by kevinwinkelman on 3/4/18.
 */

public class DatabaseManager {

    private static final String TAG = "DBAdapater";

    //names
    public static final String KEY_ROWID = "_id";
    public static final String KEY_ACTIVITY = "activity";
    public static final String KEY_HOURS = "hours";
    public static final String KEY_MINUTES = "minutes";

    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_ACTIVITY, KEY_HOURS, KEY_MINUTES};

    public static final String DATABASE_NAME = "activityDB";
    public static final String DATABASE_TABLE = "activitiesToDo";
    public static final int DATABASE_VERSION = 2;



    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE
            + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ACTIVITY + " TEXT NOT NULL, "
            + KEY_HOURS + " TEXT, "
            + KEY_MINUTES + " TEXT"
            + ");";

    private final Context context;
    private SQLiteDatabase db;
    private DatabaseHelper myDBHelper;

    public DatabaseManager(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    public DatabaseManager open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }

    //inserts the values of the activitys name, string hours and the string minutes

    public long insertRow(String activity, String hours, String minutes) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ACTIVITY, activity);
        initialValues.put(KEY_HOURS, hours);
        initialValues.put(KEY_MINUTES,minutes);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //this method is use in the onclicklonger event to delete single rows if selected
    //this deletes one single row based on the row id

    public boolean deleteRow (long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;

    }

    //this method deletes all the rows where the cursor gets all the rows and deletes the while going from the first to the lastc

    public void deleteAll( ) {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }



    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
            if (c != null) {
                c.moveToFirst();
            }
            return c;
        }


        public Cursor getRow (long rowId) {
        String where = KEY_ROWID + "=" + rowId;
            Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                    where, null, null, null, null, null);
            if( c != null) {
                c.moveToFirst();
            }

            return c;
    }

    public boolean updateRow (long rowId, String activity, String hours, String minutes) {
            String where = KEY_ROWID + "=" + rowId;
            ContentValues newValues = new ContentValues();
            newValues.put(KEY_ACTIVITY, activity);
            newValues.put(KEY_HOURS, hours);
            newValues.put(KEY_MINUTES, minutes);

            return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
            DatabaseHelper (Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase _db) {
                _db.execSQL(DATABASE_CREATE_SQL);
            }

            @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {

                _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

                onCreate(_db);
            }
    }


    }








