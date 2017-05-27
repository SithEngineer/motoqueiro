package io.github.sithengineer.motoqueiro.data.local;

import android.provider.BaseColumns;

public final class RidePersistenceContract {
  private RidePersistenceContract() {
  }

  public static abstract class RideEntry implements BaseColumns {
    public static final String TABLE_NAME = "ride";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COMPLETED = "completed";
    public static final String COLUMN_SYNCED = "synced";
    public static final String COLUMN_START_TIMESTAMP = "start_timestamp";
    public static final String COLUMN_END_TIMESTAMP = "end_timestamp";
  }

  public static abstract class AccelerometerEntry implements BaseColumns {
    public static final String TABLE_NAME = "accelerometer";
    public static final String COLUMN_XX = "xx";
    public static final String COLUMN_YY = "yy";
    public static final String COLUMN_ZZ = "zz";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_RIDE_ID = "ride_id";
  }

  // this and "AccelerometerEntry" could be one table with one more selector property
  // for 'future proofing' and fun purposes we used two different tables
  public static abstract class GravityEntry implements BaseColumns {
    public static final String TABLE_NAME = "gravity";
    public static final String COLUMN_XX = "xx";
    public static final String COLUMN_YY = "yy";
    public static final String COLUMN_ZZ = "zz";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_RIDE_ID = "ride_id";
  }

  public static abstract class GpsEntry implements BaseColumns {
    public static final String TABLE_NAME = "gps";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_RIDE_ID = "ride_id";
  }

  public static abstract class HeartRateEntry implements BaseColumns {
    public static final String TABLE_NAME = "heart_rate";
    public static final String COLUMN_BPM = "bpm";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_RIDE_ID = "ride_id";
  }
}
