package com.example.appshield.models;

/**
 * ScanRecord represents a single scan summary stored in the database.
 * It does NOT store per-app results — only the overall counts for one scan session.
 *
 * Two constructors:
 *   - Full constructor  (id, scanDate, ...) : used when reading rows back from SQLite.
 *   - Short constructor (totalApps, high, medium, low) : used by whoever triggers a scan
 *     (Shreya / Ankit). The date is filled in by DatabaseHelper when the record is inserted.
 */
public class ScanRecord {

    // ── Fields ────────────────────────────────────────────────────────────────

    private long   id;              // SQLite row id (auto-assigned on insert)
    private String scanDate;        // Human-readable date string, e.g. "19 Sep 2026, 05:30 PM"
    private int    totalApps;       // Total number of apps scanned
    private int    highRiskCount;   // Apps classified as High risk
    private int    mediumRiskCount; // Apps classified as Medium risk
    private int    lowRiskCount;    // Apps classified as Low risk

    // ── Full constructor (reading from DB) ────────────────────────────────────

    /**
     * Use this when loading a record FROM the database.
     * All fields, including the id and pre-stored date, are provided.
     */
    public ScanRecord(long id,
                      String scanDate,
                      int totalApps,
                      int highRiskCount,
                      int mediumRiskCount,
                      int lowRiskCount) {
        this.id              = id;
        this.scanDate        = scanDate;
        this.totalApps       = totalApps;
        this.highRiskCount   = highRiskCount;
        this.mediumRiskCount = mediumRiskCount;
        this.lowRiskCount    = lowRiskCount;
    }

    // ── Short constructor (after a scan) ──────────────────────────────────────

    /**
     * Use this right after a scan finishes to create a record to save.
     * The scanDate will be filled in by DatabaseHelper.insertScan() via DateHelper.now().
     *
     * Example (call this from Shreya's / Ankit's scan-complete callback):
     *   new DatabaseHelper(context).insertScan(new ScanRecord(total, high, medium, low));
     */
    public ScanRecord(int totalApps,
                      int highRiskCount,
                      int mediumRiskCount,
                      int lowRiskCount) {
        this.id              = 0;   // not yet assigned
        this.scanDate        = null; // DatabaseHelper will set this
        this.totalApps       = totalApps;
        this.highRiskCount   = highRiskCount;
        this.mediumRiskCount = mediumRiskCount;
        this.lowRiskCount    = lowRiskCount;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public long getId() {
        return id;
    }

    public String getScanDate() {
        return scanDate;
    }

    public int getTotalApps() {
        return totalApps;
    }

    public int getHighRiskCount() {
        return highRiskCount;
    }

    public int getMediumRiskCount() {
        return mediumRiskCount;
    }

    public int getLowRiskCount() {
        return lowRiskCount;
    }
}
