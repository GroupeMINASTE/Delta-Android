package fr.zabricraft.delta.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    // Properties
    private static final String database = "DeltaMathHelper";
    private static final String algorithms = "algorithms";
    private static final String local_id = "local_id";
    private static final String remote_id = "remote_id";
    private static final String name = "name";
    private static final String lines = "lines";
    private static final String owner = "owner";
    private static final String last_update = "last_update";
    private static final String icon = "icon";
    private static final String color = "color";

    // Static instance
    private static Database instance;

    // Initialize
    private Database(Context context) {
        super(context, database, null, 1);
    }

    public static synchronized Database getInstance(Context context) {
        // Use the application context
        if (instance == null) {
            instance = new Database(context.getApplicationContext());
        }
        return instance;
    }

    // Called when the database connection is being configured
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    // Called when the database is created (first time only)
    public void onCreate(SQLiteDatabase db) {
        // Initialize tables
        db.execSQL("CREATE TABLE " + algorithms + " (" +
                local_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                remote_id + " INTEGER," +
                name + " VARCHAR NOT NULL," +
                lines + " TEXT NOT NULL," +
                owner + " INTEGER NOT NULL," +
                last_update + " LONG NOT NULL," +
                icon + " VARCHAR NOT NULL DEFAULT '" + AlgorithmIcon.defaultIcon + "'," +
                color + " VARCHAR NOT NULL DEFAULT '" + AlgorithmIcon.defaultColor + "'" +
                ");");
    }

    // Called when the database needs to be upgraded
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing here, we have our own implementation
    }

    // Update database
    public void updateDatabase(int build_number) {
        if (build_number < 24) {
            // Add icon and color to table
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("ALTER TABLE " + algorithms + " ADD COLUMN " + icon + " VARCHAR NOT NULL DEFAULT '" + AlgorithmIcon.defaultIcon + "'");
            db.execSQL("ALTER TABLE " + algorithms + " ADD COLUMN " + color + " VARCHAR NOT NULL DEFAULT '" + AlgorithmIcon.defaultColor + "'");
        }
    }

    // Get algorithms
    public List<Algorithm> getAlgorithms() {
        // Initialize an array
        List<Algorithm> list = new ArrayList<>();

        // Create a SQL query
        String query = "SELECT * FROM " + algorithms + " ORDER BY " + last_update + " DESC";

        // Get the database
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Iterate data
        try {
            if (cursor.moveToFirst()) {
                do {
                    // Create algorithm in list
                    list.add(new AlgorithmParser(
                            cursor.getInt(cursor.getColumnIndex(local_id)),
                            cursor.getInt(cursor.getColumnIndex(remote_id)),
                            cursor.getInt(cursor.getColumnIndex(owner)) == 1,
                            cursor.getString(cursor.getColumnIndex(name)),
                            new Date(cursor.getLong(cursor.getColumnIndex(last_update))),
                            new AlgorithmIcon(cursor.getString(cursor.getColumnIndex(icon)),
                                    cursor.getString(cursor.getColumnIndex(color))),
                            cursor.getString(cursor.getColumnIndex(lines))
                    ).execute());
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DELTA", "Error while trying to get algorithms from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        // Return found algorithms
        return list;
    }

    // Get algorithm by id
    public Algorithm getAlgorithm(int id) {
        // Declare an algorithm
        Algorithm algorithm = null;

        // Create a SQL query
        String query = "SELECT * FROM " + algorithms + " WHERE " + local_id + " = ?";

        // Get the database
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        // Iterate data
        try {
            if (cursor.moveToFirst()) {
                do {
                    // Create algorithm in list
                    algorithm = new AlgorithmParser(
                            cursor.getInt(cursor.getColumnIndex(local_id)),
                            cursor.getInt(cursor.getColumnIndex(remote_id)),
                            cursor.getInt(cursor.getColumnIndex(owner)) == 1,
                            cursor.getString(cursor.getColumnIndex(name)),
                            new Date(cursor.getLong(cursor.getColumnIndex(last_update))),
                            new AlgorithmIcon(cursor.getString(cursor.getColumnIndex(icon)),
                                    cursor.getString(cursor.getColumnIndex(color))),
                            cursor.getString(cursor.getColumnIndex(lines))
                    ).execute();
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DELTA", "Error while trying to get algorithms from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        // Return found algorithms
        return algorithm;
    }

    // Add an algorithm into database
    public Algorithm addAlgorithm(Algorithm algorithm) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // Update last update
            algorithm.setLastUpdate(new Date());

            // Get data
            ContentValues values = new ContentValues();
            values.put(remote_id, algorithm.getRemoteId());
            values.put(name, algorithm.getName());
            values.put(lines, algorithm.toString());
            values.put(owner, algorithm.isOwner() ? 1 : 0);
            values.put(last_update, algorithm.getLastUpdate().getTime());
            values.put(icon, algorithm.getIcon().getIcon());
            values.put(color, algorithm.getIcon().getColor());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            long id = db.insertOrThrow(algorithms, null, values);
            db.setTransactionSuccessful();

            // Update id
            algorithm.setLocalId((int) id);
        } catch (Exception e) {
            Log.d("DELTA", "Error while trying to add algorithm to database");
        } finally {
            db.endTransaction();
        }

        // Return algorithm
        return algorithm;
    }

    // Update an algorithm
    public Algorithm updateAlgorithm(Algorithm algorithm) {
        // If id id 0
        if (algorithm.getLocalId() == 0) {
            // Insert the algorithm
            return addAlgorithm(algorithm);
        }

        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            // Update last update
            algorithm.setLastUpdate(new Date());

            // Get data
            ContentValues values = new ContentValues();
            values.put(remote_id, algorithm.getRemoteId());
            values.put(name, algorithm.getName());
            values.put(lines, algorithm.toString());
            values.put(owner, algorithm.isOwner() ? 1 : 0);
            values.put(last_update, algorithm.getLastUpdate().getTime());
            values.put(icon, algorithm.getIcon().getIcon());
            values.put(color, algorithm.getIcon().getColor());

            // Update row
            db.update(algorithms, values, local_id + " = ?", new String[]{String.valueOf(algorithm.getLocalId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("DELTA", "Error while trying to update algorithm");
        } finally {
            db.endTransaction();
        }

        // Return algorithm
        return algorithm;
    }

    // Delete an algorithm
    public void deleteAlgorithm(Algorithm algorithm) {
        // If id id 0
        if (algorithm.getLocalId() == 0) {
            return;
        }

        // Get database
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Delete data
            db.delete(algorithms, local_id + " = ?", new String[]{String.valueOf(algorithm.getLocalId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("DELTA", "Error while trying to delete algorithm");
        } finally {
            db.endTransaction();
        }
    }

}
