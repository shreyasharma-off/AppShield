package com.example.appshield.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.appshield.models.ScanRecord;
import com.example.appshield.utils.DateHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper manages the local SQLite database for AppShield.
 *
 * Only scan SUMMARIES are stored — not per-app details.
 * One row = one completed scan session.
 *
 * Table: scan_history
 *   id               INTEGER PRIMARY KEY AUTOINCREMENT
 *   scan_date        TEXT
 *   total_apps       INTEGER
 *   high_risk_count  INTEGER
 *   medium_risk_count INTEGER
 *   low_risk_count   INTEGER
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // ── Database constants ────────────────────────────────────────────────────

    private static final String DB_NAME    = "appshield.db";
    private static final int    DB_VERSION = 1;

    // ── Table & column constants ──────────────────────────────────────────────

    static final String TABLE_SCAN_HISTORY    = "scan_history";
    static final String COL_ID                = "id";
    static final String COL_SCAN_DATE         = "scan_date";
    static final String COL_TOTAL_APPS        = "total_apps";
    static final String COL_HIGH_RISK_COUNT   = "high_risk_count";
    static final String COL_MEDIUM_RISK_COUNT = "medium_risk_count";
    static final String COL_LOW_RISK_COUNT    = "low_risk_count";

    // ── Constructor ───────────────────────────────────────────────────────────

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // ── SQLiteOpenHelper callbacks ────────────────────────────────────────────

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the scan_history table on first install.
        String createTable = "CREATE TABLE " + TABLE_SCAN_HISTORY + " ("
                + COL_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_SCAN_DATE         + " TEXT, "
                + COL_TOTAL_APPS        + " INTEGER, "
                + COL_HIGH_RISK_COUNT   + " INTEGER, "
                + COL_MEDIUM_RISK_COUNT + " INTEGER, "
                + COL_LOW_RISK_COUNT    + " INTEGER"
                + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For v1 there is nothing to migrate.
        // Future versions: apply ALTER TABLE statements here instead of dropping.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCAN_HISTORY);
        onCreate(db);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Inserts a scan summary into the database.
     *
     * If the record was created with the short constructor (no date set),
     * the current date/time is automatically stamped using DateHelper.now().
     *
     * @param record A ScanRecord (short or full constructor).
     * @return The row id of the newly inserted row, or -1 on error.
     *
     * Called by teammates after a scan:
     *   new DatabaseHelper(context).insertScan(new ScanRecord(total, high, medium, low));
     */
    public long insertScan(ScanRecord record) {
        SQLiteDatabase db = getWritableDatabase();

        // Use the stored date if available, otherwise stamp with now().
        String date = (record.getScanDate() != null && !record.getScanDate().isEmpty())
                ? record.getScanDate()
                : DateHelper.now();

        ContentValues values = new ContentValues();
        values.put(COL_SCAN_DATE,         date);
        values.put(COL_TOTAL_APPS,        record.getTotalApps());
        values.put(COL_HIGH_RISK_COUNT,   record.getHighRiskCount());
        values.put(COL_MEDIUM_RISK_COUNT, record.getMediumRiskCount());
        values.put(COL_LOW_RISK_COUNT,    record.getLowRiskCount());

        long rowId = db.insert(TABLE_SCAN_HISTORY, null, values);
        db.close();
        return rowId;
    }

    /**
     * Returns all scan records, newest first (ORDER BY id DESC).
     * Cursors are closed before returning.
     *
     * @return List of ScanRecord objects; empty list if no history.
     */
    public List<ScanRecord> getAllScans() {
        List<ScanRecord> scans = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        // ORDER BY id DESC → newest inserted row appears first
        Cursor cursor = db.query(
                TABLE_SCAN_HISTORY,         // table
                null,                       // all columns
                null,                       // no WHERE
                null,                       // no WHERE args
                null,                       // no GROUP BY
                null,                       // no HAVING
                COL_ID + " DESC"            // ORDER BY id DESC
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long   id      = cursor.getLong  (cursor.getColumnIndexOrThrow(COL_ID));
                String date    = cursor.getString(cursor.getColumnIndexOrThrow(COL_SCAN_DATE));
                int    total   = cursor.getInt   (cursor.getColumnIndexOrThrow(COL_TOTAL_APPS));
                int    high    = cursor.getInt   (cursor.getColumnIndexOrThrow(COL_HIGH_RISK_COUNT));
                int    medium  = cursor.getInt   (cursor.getColumnIndexOrThrow(COL_MEDIUM_RISK_COUNT));
                int    low     = cursor.getInt   (cursor.getColumnIndexOrThrow(COL_LOW_RISK_COUNT));

                scans.add(new ScanRecord(id, date, total, high, medium, low));
            }
            cursor.close(); // always close to avoid resource leaks
        }

        db.close();
        return scans;
    }

    /**
     * Deletes all rows from scan_history.
     * Called when the user confirms "Clear History" in ScanHistoryFragment.
     */
    public void clearHistory() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SCAN_HISTORY, null, null);
        db.close();
    }
}
