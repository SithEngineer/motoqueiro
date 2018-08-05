package io.github.sithengineer.motoqueiro.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import io.github.sithengineer.motoqueiro.data.RideDataSource;
import io.github.sithengineer.motoqueiro.data.model.GpsPoint;
import io.github.sithengineer.motoqueiro.data.model.HeartRatePoint;
import io.github.sithengineer.motoqueiro.data.model.Ride;
import io.github.sithengineer.motoqueiro.data.model.TriDimenPoint;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import rx.Scheduler;
import timber.log.Timber;

import static dagger.internal.Preconditions.checkNotNull;

public class RideLocalDataSource implements RideDataSource {

  private static final int SQL_TRUE = 1;
  private static final int SQL_FALSE = 0;

  @NonNull private final RidesDbHelper databaseHelper;
  private Scheduler scheduler;

  public RideLocalDataSource(@NonNull Context context, Scheduler scheduler) {
    this.scheduler = scheduler;
    checkNotNull(context, "context cannot be null");
    databaseHelper = new RidesDbHelper(context);
  }

  @Override public void saveGpsData(String rideId, List<GpsPoint> points) {
    checkNotNull(rideId);
    checkNotNull(points);
    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    try {
      for (GpsPoint p : points) {
        saveGpsData(rideId, p);
      }
      database.setTransactionSuccessful();
    } catch (Exception e) {
      Timber.e(e);
    } finally {
      database.endTransaction();
      database.close();
    }
  }

  @Override public long saveGpsData(String rideId, GpsPoint coords) {
    checkNotNull(rideId);
    checkNotNull(coords);

    ContentValues values = new ContentValues();
    values.put(RidePersistenceContract.GpsEntry.COLUMN_RIDE_ID, rideId);
    values.put(RidePersistenceContract.GpsEntry.COLUMN_LAT, coords.getLatitude());
    values.put(RidePersistenceContract.GpsEntry.COLUMN_LON, coords.getLongitude());
    values.put(RidePersistenceContract.GpsEntry.COLUMN_TIMESTAMP, coords.getTimestamp());

    return insert(values, RidePersistenceContract.GpsEntry.TABLE_NAME);
  }

  @Override public List<GpsPoint> getGpsData(String rideId) {
    final String[] columns = {
        RidePersistenceContract.GpsEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.GpsEntry.COLUMN_TIMESTAMP,
        RidePersistenceContract.GpsEntry.COLUMN_LAT, RidePersistenceContract.GpsEntry.COLUMN_LON
    };

    String selection =
        String.format("SELECT %s FROM %s WHERE %s LIKE ?", TextUtils.join(",", columns),
            RidePersistenceContract.GpsEntry.TABLE_NAME,
            RidePersistenceContract.GpsEntry.COLUMN_RIDE_ID);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    Cursor cursor = database.query(RidePersistenceContract.GpsEntry.TABLE_NAME, columns, selection,
        new String[] { rideId }, null, null, null);
    List<GpsPoint> result = new ArrayList<>(cursor != null ? cursor.getCount() : 0);
    while (cursor.moveToNext()) {
      result.add(Mapper.CURSOR_TO_GPS_POINT.call(cursor));
    }
    if (!cursor.isClosed()) {
      cursor.close();
    }
    database.close();
    return result;
  }

  @Override public void saveAccelerometerData(String rideId, List<TriDimenPoint> points) {
    checkNotNull(rideId);
    checkNotNull(points);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    try {
      for (TriDimenPoint p : points) {
        saveAccelerometerData(rideId, p);
      }
      database.setTransactionSuccessful();
    } catch (Exception e) {
      Timber.e(e);
    } finally {
      database.endTransaction();
      database.close();
    }
  }

  @Override public long saveAccelerometerData(String rideId, TriDimenPoint point) {
    checkNotNull(rideId);
    checkNotNull(point);

    ContentValues values = new ContentValues();
    values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_RIDE_ID, rideId);
    values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_XX, point.getX());
    values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_YY, point.getY());
    values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_ZZ, point.getZ());
    values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_TIMESTAMP, point.getTimestamp());

    return insert(values, RidePersistenceContract.AccelerometerEntry.TABLE_NAME);
  }

  @Override public List<TriDimenPoint> getAccelerometerData(String rideId) {
    String[] columns = {
        RidePersistenceContract.AccelerometerEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.AccelerometerEntry.COLUMN_TIMESTAMP,
        RidePersistenceContract.AccelerometerEntry.COLUMN_XX,
        RidePersistenceContract.AccelerometerEntry.COLUMN_YY,
        RidePersistenceContract.AccelerometerEntry.COLUMN_ZZ
    };
    String selection =
        String.format("SELECT %s FROM %s WHERE %s LIKE ?", TextUtils.join(",", columns),
            RidePersistenceContract.AccelerometerEntry.TABLE_NAME,
            RidePersistenceContract.AccelerometerEntry.COLUMN_RIDE_ID);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    Cursor cursor =
        database.query(RidePersistenceContract.AccelerometerEntry.TABLE_NAME, columns, selection,
            new String[] { rideId }, null, null, null);
    List<TriDimenPoint> result = new ArrayList<>(cursor != null ? cursor.getCount() : 0);
    while (cursor.moveToNext()) {
      result.add(Mapper.CURSOR_TO_ACCEL_POINT.call(cursor));
    }
    if (!cursor.isClosed()) {
      cursor.close();
    }
    database.close();
    return result;
  }

  @Override public void saveGyroscopeData(String rideId, List<TriDimenPoint> points) {
    checkNotNull(rideId);
    checkNotNull(points);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    try {
      for (TriDimenPoint p : points) {
        saveGyroscopeData(rideId, p);
      }
      database.setTransactionSuccessful();
    } catch (Exception e) {
      Timber.e(e);
    } finally {
      database.endTransaction();
      database.close();
    }
  }

  @Override public long saveGyroscopeData(String rideId, TriDimenPoint point) {
    checkNotNull(rideId);
    checkNotNull(point);

    ContentValues values = new ContentValues();
    values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_RIDE_ID, rideId);
    values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_XX, point.getX());
    values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_YY, point.getY());
    values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_ZZ, point.getZ());
    values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_TIMESTAMP, point.getTimestamp());

    return insert(values, RidePersistenceContract.GyroscopeEntry.TABLE_NAME);
  }

  @Override public void saveGravityData(String rideId, List<TriDimenPoint> points) {
    checkNotNull(rideId);
    checkNotNull(points);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    try {
      for (TriDimenPoint p : points) {
        saveGravityData(rideId, p);
      }
      database.setTransactionSuccessful();
    } catch (Exception e) {
      Timber.e(e);
    } finally {
      database.endTransaction();
      database.close();
    }
  }

  @Override public long saveGravityData(String rideId, TriDimenPoint point) {
    checkNotNull(rideId);
    checkNotNull(point);

    ContentValues values = new ContentValues();
    values.put(RidePersistenceContract.GravityEntry.COLUMN_RIDE_ID, rideId);
    values.put(RidePersistenceContract.GravityEntry.COLUMN_XX, point.getX());
    values.put(RidePersistenceContract.GravityEntry.COLUMN_YY, point.getY());
    values.put(RidePersistenceContract.GravityEntry.COLUMN_ZZ, point.getZ());
    values.put(RidePersistenceContract.GravityEntry.COLUMN_TIMESTAMP, point.getTimestamp());

    return insert(values, RidePersistenceContract.GravityEntry.TABLE_NAME);
  }

  @Override public List<TriDimenPoint> getGravityData(String rideId) {
    final String[] columns = {
        RidePersistenceContract.GravityEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.GravityEntry.COLUMN_TIMESTAMP,
        RidePersistenceContract.GravityEntry.COLUMN_XX,
        RidePersistenceContract.GravityEntry.COLUMN_YY,
        RidePersistenceContract.GravityEntry.COLUMN_ZZ
    };
    String selection =
        String.format("SELECT %s FROM %s WHERE %s LIKE ?", TextUtils.join(",", columns),
            RidePersistenceContract.GravityEntry.TABLE_NAME,
            RidePersistenceContract.GravityEntry.COLUMN_RIDE_ID);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    Cursor cursor =
        database.query(RidePersistenceContract.GravityEntry.TABLE_NAME, columns, selection,
            new String[] { rideId }, null, null, null);
    List<TriDimenPoint> result = new ArrayList<>(cursor != null ? cursor.getCount() : 0);
    while (cursor.moveToNext()) {
      result.add(Mapper.CURSOR_TO_GRAVITY_POINT.call(cursor));
    }
    if (!cursor.isClosed()) {
      cursor.close();
    }
    database.close();
    return result;
  }

  @Override public void saveHeartRateData(String rideId, List<HeartRatePoint> points) {
    checkNotNull(rideId);
    checkNotNull(points);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    try {
      for (HeartRatePoint p : points) {
        saveHeartRateData(rideId, p);
      }
      database.setTransactionSuccessful();
    } catch (Exception e) {
      Timber.e(e);
    } finally {
      database.endTransaction();
      database.close();
    }
  }

  @Override public long saveHeartRateData(String rideId, HeartRatePoint point) {
    checkNotNull(rideId);
    checkNotNull(point);

    ContentValues values = new ContentValues();
    values.put(RidePersistenceContract.HeartRateEntry.COLUMN_RIDE_ID, rideId);
    values.put(RidePersistenceContract.HeartRateEntry.COLUMN_BPM, point.getHeartRate());
    values.put(RidePersistenceContract.HeartRateEntry.COLUMN_TIMESTAMP, point.getTimestamp());

    return insert(values, RidePersistenceContract.HeartRateEntry.TABLE_NAME);
  }

  @Override public List<HeartRatePoint> getHeartRateData(String rideId) {
    final String[] columns = {
        RidePersistenceContract.HeartRateEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.HeartRateEntry.COLUMN_BPM,
        RidePersistenceContract.HeartRateEntry.COLUMN_TIMESTAMP
    };
    String selection =
        String.format("SELECT %s FROM %s WHERE %s LIKE ?", TextUtils.join(",", columns),
            RidePersistenceContract.HeartRateEntry.TABLE_NAME,
            RidePersistenceContract.HeartRateEntry.COLUMN_RIDE_ID);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    Cursor cursor =
        database.query(RidePersistenceContract.HeartRateEntry.TABLE_NAME, columns, selection,
            new String[] { rideId }, null, null, null);
    List<HeartRatePoint> result = new ArrayList<>(cursor != null ? cursor.getCount() : 0);
    while (cursor.moveToNext()) {
      result.add(Mapper.CURSOR_TO_HEART_RATE_POINT.call(cursor));
    }
    if (!cursor.isClosed()) {
      cursor.close();
    }
    database.close();
    return result;
  }

  @Override public long saveRide(Ride ride) {
    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    long insertedRideId = -1;
    try {

      ContentValues values = new ContentValues();
      values.put(RidePersistenceContract.RideEntry._ID, ride.getId());
      values.put(RidePersistenceContract.RideEntry.COLUMN_NAME, ride.getEvent());
      values.put(RidePersistenceContract.RideEntry.COLUMN_COMPLETED, ride.isCompleted());
      values.put(RidePersistenceContract.RideEntry.COLUMN_START_TIMESTAMP,
          ride.getInitialTimestamp());
      values.put(RidePersistenceContract.RideEntry.COLUMN_SYNCED, false);
      values.put(RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP, ride.getFinalTimestamp());

      insertedRideId =
          database.insertWithOnConflict(RidePersistenceContract.RideEntry.TABLE_NAME, null, values,
              SQLiteDatabase.CONFLICT_REPLACE);

      saveAccelerometerData(ride.getId(), ride.getAccelerometerCaptures());
      saveGravityData(ride.getId(), ride.getGravityCaptures());
      saveGpsData(ride.getId(), ride.getGpsCoordinates());
      saveHeartRateData(ride.getId(), ride.getHeartRateCaptures());

      database.setTransactionSuccessful();
    } catch (Exception ex) {
      Timber.e(ex);
    } finally {
      database.endTransaction();
      database.close();
    }
    return insertedRideId;
  }

  @Override public List<TriDimenPoint> getGyroscopeData(String rideId) {
    final String[] columns = {
        RidePersistenceContract.GyroscopeEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.GyroscopeEntry.COLUMN_TIMESTAMP,
        RidePersistenceContract.GyroscopeEntry.COLUMN_XX,
        RidePersistenceContract.GyroscopeEntry.COLUMN_YY,
        RidePersistenceContract.GyroscopeEntry.COLUMN_ZZ
    };
    String selection =
        String.format("SELECT %s FROM %s WHERE %s LIKE ?", TextUtils.join(",", columns),
            RidePersistenceContract.GyroscopeEntry.TABLE_NAME,
            RidePersistenceContract.GyroscopeEntry.COLUMN_RIDE_ID);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    Cursor cursor =
        database.query(RidePersistenceContract.GyroscopeEntry.TABLE_NAME, columns, selection,
            new String[] { rideId }, null, null, null);
    List<TriDimenPoint> result = new ArrayList<>(cursor != null ? cursor.getCount() : 0);
    while (cursor.moveToNext()) {
      result.add(Mapper.CURSOR_TO_GYROSCOPE_POINT.call(cursor));
    }
    if (!cursor.isClosed()) {
      cursor.close();
    }
    database.close();
    return result;
  }

  @Override public Ride getRide(String rideId) {
    List<GpsPoint> gpsPoints = getGpsData(rideId);
    List<TriDimenPoint> accelerometerPoints = getAccelerometerData(rideId);
    List<TriDimenPoint> gravityPoints = getGravityData(rideId);
    List<TriDimenPoint> gyroPoints = getGyroscopeData(rideId);
    List<HeartRatePoint> heartRatePoints = getHeartRateData(rideId);

    final String[] columns = {
        RidePersistenceContract.RideEntry._ID, RidePersistenceContract.RideEntry.COLUMN_NAME,
        RidePersistenceContract.RideEntry.COLUMN_COMPLETED,
        RidePersistenceContract.RideEntry.COLUMN_SYNCED,
        RidePersistenceContract.RideEntry.COLUMN_START_TIMESTAMP,
        RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP
    };

    String selection =
        String.format("SELECT %s FROM %s WHERE %s LIKE ?", TextUtils.join(",", columns),
            RidePersistenceContract.RideEntry.TABLE_NAME, RidePersistenceContract.RideEntry._ID);

    SQLiteDatabase database = databaseHelper.getReadableDatabase();
    Cursor cursor = database.query(RidePersistenceContract.RideEntry.TABLE_NAME, columns, selection,
        new String[0], null, null, null);
    Ride result = null;
    if (cursor.moveToNext()) {
      result = Mapper.CURSOR_TO_RIDE.call(cursor, gpsPoints, heartRatePoints, accelerometerPoints,
          gravityPoints, gyroPoints);
    }
    if (!cursor.isClosed()) {
      cursor.close();
    }
    database.close();

    if (result != null) {
      return result;
    }

    throw new IllegalStateException(String.format("No ride was found for id %s", rideId));
  }

  @Override public boolean markCompleted(@NonNull final String rideId) {
    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    int affectedLines = 0;
    try {
      ContentValues values = new ContentValues();
      values.put(RidePersistenceContract.RideEntry.COLUMN_COMPLETED, SQL_TRUE);

      final long timeInMillis = Calendar.getInstance().getTimeInMillis();
      values.put(RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP, timeInMillis);

      // update ride
      affectedLines = database.update(RidePersistenceContract.RideEntry.TABLE_NAME, values,
          String.format("%s = ?", RidePersistenceContract.RideEntry._ID), new String[] { rideId });
    } catch (Exception ex) {
      Timber.e(ex);
    } finally {
      database.endTransaction();
      database.close();
    }
    return affectedLines > 0;
  }

  @Override public List<Ride> getCompletedRides() {
    List<String> completedRidesIds = getCompletedRideIds();
    List<Ride> rides = new ArrayList<>(completedRidesIds.size());
    for (String rideId : completedRidesIds) {
      rides.add(getRide(rideId));
    }
    return rides;
  }

  @Override public boolean markSynced(@NonNull final String rideId) {
    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    int affectedLines = 0;
    try {
      ContentValues values = new ContentValues();
      values.put(RidePersistenceContract.RideEntry.COLUMN_SYNCED, SQL_TRUE);
      affectedLines = database.update(RidePersistenceContract.RideEntry.TABLE_NAME, values,
          String.format("%s = ?", RidePersistenceContract.RideEntry._ID), new String[] { rideId });
      database.setTransactionSuccessful();
    } catch (Exception ex) {
      Timber.e(ex);
    } finally {
      database.endTransaction();
      database.close();
    }
    return affectedLines > 0;
  }

  private long insert(ContentValues values, String tableName) {
    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    database.beginTransaction();
    long id = -1;
    try {
      id = database.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
      database.setTransactionSuccessful();
    } catch (Exception ex) {
      Timber.e(ex);
    } finally {
      database.endTransaction();
      database.close();
    }
    return id;
  }

  private List<String> getCompletedRideIds() {
    final String[] columns = {
        RidePersistenceContract.RideEntry._ID, RidePersistenceContract.RideEntry.COLUMN_COMPLETED
    };

    String selection =
        String.format("SELECT %s FROM %s WHERE %s LIKE ?", TextUtils.join(",", columns),
            RidePersistenceContract.RideEntry.TABLE_NAME,
            RidePersistenceContract.RideEntry.COLUMN_COMPLETED);

    SQLiteDatabase database = databaseHelper.getWritableDatabase();
    Cursor cursor =
        database.query(RidePersistenceContract.GyroscopeEntry.TABLE_NAME, columns, selection,
            new String[0], null, null, null);
    List<String> result = new ArrayList<>(cursor != null ? cursor.getCount() : 0);
    while (cursor.moveToNext()) {
      result.add(Mapper.CURSOR_TO_RIDE_ID.call(cursor));
    }
    if (!cursor.isClosed()) {
      cursor.close();
    }
    database.close();
    return result;
  }
}
