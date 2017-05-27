package io.github.sithengineer.motoqueiro.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RidesDbHelper extends SQLiteOpenHelper {

  private static final int DB_VERSION = 1;
  private static final String DB_NAME = "rides.db";

  public RidesDbHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {

    final String createRideTable = "CREATE TABLE "
        + RidePersistenceContract.RideEntry.TABLE_NAME
        + " ("
        + RidePersistenceContract.RideEntry._ID
        + " TEXT PRIMARY KEY,"
        + RidePersistenceContract.RideEntry.COLUMN_NAME
        + " TEXT,"
        + RidePersistenceContract.RideEntry.COLUMN_COMPLETED
        + " INTEGER,"
        + RidePersistenceContract.RideEntry.COLUMN_SYNCED
        + " INTEGER,"
        + RidePersistenceContract.RideEntry.COLUMN_START_TIMESTAMP
        + " INTEGER,"
        + RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP
        + " INTEGER"
        + " );";
    db.execSQL(createRideTable);

    final String createAccelerometerTable = "CREATE TABLE "
        + RidePersistenceContract.AccelerometerEntry.TABLE_NAME
        + " ("
        + RidePersistenceContract.AccelerometerEntry._ID
        + " INTEGER PRIMARY KEY,"
        + RidePersistenceContract.AccelerometerEntry.COLUMN_XX
        + " REAL,"
        + RidePersistenceContract.AccelerometerEntry.COLUMN_YY
        + " REAL,"
        + RidePersistenceContract.AccelerometerEntry.COLUMN_ZZ
        + " REAL,"
        + RidePersistenceContract.AccelerometerEntry.COLUMN_TIMESTAMP
        + " INTEGER,"
        + RidePersistenceContract.AccelerometerEntry.COLUMN_RIDE_ID
        + " TEXT"
        + ", FOREIGN KEY("
        + RidePersistenceContract.AccelerometerEntry.COLUMN_RIDE_ID
        + ") REFERENCES "
        + RidePersistenceContract.RideEntry.TABLE_NAME
        + "("
        + RidePersistenceContract.RideEntry._ID
        + ")"
        + " );";
    db.execSQL(createAccelerometerTable);

    final String createGravityTable = "CREATE TABLE "
        + RidePersistenceContract.GravityEntry.TABLE_NAME
        + " ("
        + RidePersistenceContract.GravityEntry._ID
        + " INTEGER PRIMARY KEY,"
        + RidePersistenceContract.GravityEntry.COLUMN_XX
        + " REAL,"
        + RidePersistenceContract.GravityEntry.COLUMN_YY
        + " REAL,"
        + RidePersistenceContract.GravityEntry.COLUMN_ZZ
        + " REAL,"
        + RidePersistenceContract.GravityEntry.COLUMN_TIMESTAMP
        + " INTEGER,"
        + RidePersistenceContract.GravityEntry.COLUMN_RIDE_ID
        + " TEXT"
        + ", FOREIGN KEY("
        + RidePersistenceContract.GravityEntry.COLUMN_RIDE_ID
        + ") REFERENCES "
        + RidePersistenceContract.RideEntry.TABLE_NAME
        + "("
        + RidePersistenceContract.RideEntry._ID
        + ")"
        + " );";
    db.execSQL(createGravityTable);

    final String createGpsTable = "CREATE TABLE "
        + RidePersistenceContract.GpsEntry.TABLE_NAME
        + " ("
        + RidePersistenceContract.GpsEntry._ID
        + " INTEGER PRIMARY KEY,"
        + RidePersistenceContract.GpsEntry.COLUMN_LAT
        + " REAL,"
        + RidePersistenceContract.GpsEntry.COLUMN_LON
        + " REAL,"
        + RidePersistenceContract.GpsEntry.COLUMN_TIMESTAMP
        + " INTEGER,"
        + RidePersistenceContract.GpsEntry.COLUMN_RIDE_ID
        + " TEXT"
        + ", FOREIGN KEY("
        + RidePersistenceContract.GpsEntry.COLUMN_RIDE_ID
        + ") REFERENCES "
        + RidePersistenceContract.RideEntry.TABLE_NAME
        + "("
        + RidePersistenceContract.RideEntry._ID
        + ")"
        + " );";
    db.execSQL(createGpsTable);

    final String createHeartRateTable = "CREATE TABLE "
        + RidePersistenceContract.HeartRateEntry.TABLE_NAME
        + " ("
        + RidePersistenceContract.HeartRateEntry._ID
        + " INTEGER PRIMARY KEY,"
        + RidePersistenceContract.HeartRateEntry.COLUMN_BPM
        + " INTEGER,"
        + RidePersistenceContract.HeartRateEntry.COLUMN_TIMESTAMP
        + " INTEGER,"
        + RidePersistenceContract.HeartRateEntry.COLUMN_RIDE_ID
        + " TEXT"
        + ", FOREIGN KEY("
        + RidePersistenceContract.HeartRateEntry.COLUMN_RIDE_ID
        + ") REFERENCES "
        + RidePersistenceContract.RideEntry.TABLE_NAME
        + "("
        + RidePersistenceContract.RideEntry._ID
        + ")"
        + " );";
    db.execSQL(createHeartRateTable);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // does nothing
  }

  @Override public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // does nothing
  }
}
