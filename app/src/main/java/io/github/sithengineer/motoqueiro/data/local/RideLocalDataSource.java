package io.github.sithengineer.motoqueiro.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;
import com.squareup.sqlbrite.SqlBrite;
import io.github.sithengineer.motoqueiro.data.RideDataSource;
import io.github.sithengineer.motoqueiro.data.model.GpsPoint;
import io.github.sithengineer.motoqueiro.data.model.HeartRatePoint;
import io.github.sithengineer.motoqueiro.data.model.Ride;
import io.github.sithengineer.motoqueiro.data.model.TriDimenPoint;
import java.util.Calendar;
import java.util.List;
import rx.Completable;
import rx.Observable;
import rx.Scheduler;
import rx.Single;
import timber.log.Timber;

import static dagger.internal.Preconditions.checkNotNull;

public class RideLocalDataSource implements RideDataSource {

  private static final int SQL_TRUE = 1;
  private static final int SQL_FALSE = 0;

  @NonNull private final BriteDatabase databaseHelper;

  public RideLocalDataSource(@NonNull Context context, @NonNull Scheduler scheduler) {
    checkNotNull(context, "context cannot be null");
    checkNotNull(scheduler, "scheduler cannot be null");
    RidesDbHelper dbHelper = new RidesDbHelper(context);
    SqlBrite sqlBrite = new SqlBrite.Builder().build();
    databaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, scheduler);
  }

  @Override public Completable saveGpsData(String rideId, List<GpsPoint> points) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(points);
      return databaseHelper.newTransaction();
    })
        .flatMapCompletable(transaction -> Observable.just(points)
            .flatMapIterable(list -> list)
            .doOnNext(p -> saveGpsData(rideId, p))
            .toList()
            .doOnCompleted(() -> {
              transaction.markSuccessful();
              transaction.end();
            })
            .doOnError(err -> {
              Timber.e(err);
              transaction.end();
            })
            .toCompletable());
  }

  @Override public Single<Long> saveGpsData(String rideId, GpsPoint coords) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(coords);

      ContentValues values = new ContentValues();
      values.put(RidePersistenceContract.GpsEntry.COLUMN_RIDE_ID, rideId);
      values.put(RidePersistenceContract.GpsEntry.COLUMN_LAT, coords.getLatitude());
      values.put(RidePersistenceContract.GpsEntry.COLUMN_LON, coords.getLongitude());
      values.put(RidePersistenceContract.GpsEntry.COLUMN_TIMESTAMP,
          coords.getTimestamp());

      return databaseHelper.insert(RidePersistenceContract.GpsEntry.TABLE_NAME, values,
          SQLiteDatabase.CONFLICT_REPLACE);
    });
  }

  @Override public Single<List<GpsPoint>> getGpsData(String rideId) {
    final String[] projection = {
        RidePersistenceContract.GpsEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.GpsEntry.COLUMN_TIMESTAMP,
        RidePersistenceContract.GpsEntry.COLUMN_LAT,
        RidePersistenceContract.GpsEntry.COLUMN_LON
    };
    String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
        TextUtils.join(",", projection), RidePersistenceContract.GpsEntry.TABLE_NAME,
        RidePersistenceContract.GpsEntry.COLUMN_RIDE_ID);
    return databaseHelper.createQuery(RidePersistenceContract.GpsEntry.TABLE_NAME, sql,
        rideId).mapToList(Mapper.CURSOR_TO_GPS_POINT).first().toSingle();
  }

  @Override
  public Completable saveAccelerometerData(String rideId, List<TriDimenPoint> points) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(points);
      return databaseHelper.newTransaction();
    })
        .flatMapCompletable(transaction -> Observable.just(points)
            .flatMapIterable(list -> list)
            .doOnNext(p -> saveAccelerometerData(rideId, p))
            .toList()
            .doOnCompleted(() -> {
              transaction.markSuccessful();
              transaction.end();
            })
            .doOnError(err -> {
              Timber.e(err);
              transaction.end();
            })
            .toCompletable());
  }

  @Override
  public Single<Long> saveAccelerometerData(String rideId, TriDimenPoint point) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(point);

      ContentValues values = new ContentValues();
      values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_RIDE_ID, rideId);
      values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_XX, point.getX());
      values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_YY, point.getY());
      values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_ZZ, point.getZ());
      values.put(RidePersistenceContract.AccelerometerEntry.COLUMN_TIMESTAMP,
          point.getTimestamp());

      return databaseHelper.insert(RidePersistenceContract.AccelerometerEntry.TABLE_NAME,
          values, SQLiteDatabase.CONFLICT_REPLACE);
    });
  }

  @Override public Single<List<TriDimenPoint>> getAccelerometerData(String rideId) {
    String[] projection = {
        RidePersistenceContract.AccelerometerEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.AccelerometerEntry.COLUMN_TIMESTAMP,
        RidePersistenceContract.AccelerometerEntry.COLUMN_XX,
        RidePersistenceContract.AccelerometerEntry.COLUMN_YY,
        RidePersistenceContract.AccelerometerEntry.COLUMN_ZZ
    };
    String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
        TextUtils.join(",", projection),
        RidePersistenceContract.AccelerometerEntry.TABLE_NAME,
        RidePersistenceContract.AccelerometerEntry.COLUMN_RIDE_ID);
    return databaseHelper.createQuery(
        RidePersistenceContract.AccelerometerEntry.TABLE_NAME, sql, rideId)
        .mapToList(Mapper.CURSOR_TO_ACCEL_POINT)
        .first()
        .toSingle();
  }

  @Override
  public Completable saveGyroscopeData(String rideId, List<TriDimenPoint> points) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(points);
      return databaseHelper.newTransaction();
    })
        .flatMapCompletable(transaction -> Observable.just(points)
            .flatMapIterable(list -> list)
            .doOnNext(p -> saveGyroscopeData(rideId, p))
            .toList()
            .doOnCompleted(() -> {
              transaction.markSuccessful();
              transaction.end();
            })
            .doOnError(err -> {
              Timber.e(err);
              transaction.end();
            })
            .toCompletable());
  }

  @Override public Single<Long> saveGyroscopeData(String rideId, TriDimenPoint point) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(point);

      ContentValues values = new ContentValues();
      values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_RIDE_ID, rideId);
      values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_XX, point.getX());
      values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_YY, point.getY());
      values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_ZZ, point.getZ());
      values.put(RidePersistenceContract.GyroscopeEntry.COLUMN_TIMESTAMP,
          point.getTimestamp());

      return databaseHelper.insert(RidePersistenceContract.GyroscopeEntry.TABLE_NAME,
          values, SQLiteDatabase.CONFLICT_REPLACE);
    });
  }

  @Override
  public Completable saveGravityData(String rideId, List<TriDimenPoint> points) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(points);
      return databaseHelper.newTransaction();
    })
        .flatMapCompletable(transaction -> Observable.just(points)
            .flatMapIterable(list -> list)
            .doOnNext(p -> saveGravityData(rideId, p))
            .toList()
            .doOnCompleted(() -> {
              transaction.markSuccessful();
              transaction.end();
            })
            .doOnError(err -> {
              Timber.e(err);
              transaction.end();
            })
            .toCompletable());
  }

  @Override public Single<Long> saveGravityData(String rideId, TriDimenPoint point) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(point);

      ContentValues values = new ContentValues();
      values.put(RidePersistenceContract.GravityEntry.COLUMN_RIDE_ID, rideId);
      values.put(RidePersistenceContract.GravityEntry.COLUMN_XX, point.getX());
      values.put(RidePersistenceContract.GravityEntry.COLUMN_YY, point.getY());
      values.put(RidePersistenceContract.GravityEntry.COLUMN_ZZ, point.getZ());
      values.put(RidePersistenceContract.GravityEntry.COLUMN_TIMESTAMP,
          point.getTimestamp());

      return databaseHelper.insert(RidePersistenceContract.GravityEntry.TABLE_NAME,
          values, SQLiteDatabase.CONFLICT_REPLACE);
    });
  }

  @Override public Single<List<TriDimenPoint>> getGravityData(String rideId) {
    final String[] projection = {
        RidePersistenceContract.GravityEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.GravityEntry.COLUMN_TIMESTAMP,
        RidePersistenceContract.GravityEntry.COLUMN_XX,
        RidePersistenceContract.GravityEntry.COLUMN_YY,
        RidePersistenceContract.GravityEntry.COLUMN_ZZ
    };
    String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
        TextUtils.join(",", projection), RidePersistenceContract.GravityEntry.TABLE_NAME,
        RidePersistenceContract.GravityEntry.COLUMN_RIDE_ID);
    return databaseHelper.createQuery(RidePersistenceContract.GravityEntry.TABLE_NAME,
        sql, rideId).mapToList(Mapper.CURSOR_TO_GRAVITY_POINT).first().toSingle();
  }

  @Override
  public Completable saveHeartRateData(String rideId, List<HeartRatePoint> points) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(points);
      return databaseHelper.newTransaction();
    })
        .flatMapCompletable(transaction -> Observable.just(points)
            .flatMapIterable(list -> list)
            .doOnNext(p -> saveHeartRateData(rideId, p))
            .toList()
            .doOnCompleted(() -> {
              transaction.markSuccessful();
              transaction.end();
            })
            .doOnError(err -> {
              Timber.e(err);
              transaction.end();
            })
            .toCompletable());
  }

  @Override public Single<Long> saveHeartRateData(String rideId, HeartRatePoint point) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(point);

      ContentValues values = new ContentValues();
      values.put(RidePersistenceContract.HeartRateEntry.COLUMN_RIDE_ID, rideId);
      values.put(RidePersistenceContract.HeartRateEntry.COLUMN_BPM, point.getHeartRate());
      values.put(RidePersistenceContract.HeartRateEntry.COLUMN_TIMESTAMP,
          point.getTimestamp());

      return databaseHelper.insert(RidePersistenceContract.HeartRateEntry.TABLE_NAME,
          values, SQLiteDatabase.CONFLICT_REPLACE);
    });
  }

  @Override public Single<List<HeartRatePoint>> getHeartRateData(String rideId) {
    final String[] projection = {
        RidePersistenceContract.HeartRateEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.HeartRateEntry.COLUMN_BPM,
        RidePersistenceContract.HeartRateEntry.COLUMN_TIMESTAMP
    };
    String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
        TextUtils.join(",", projection),
        RidePersistenceContract.HeartRateEntry.TABLE_NAME,
        RidePersistenceContract.HeartRateEntry.COLUMN_RIDE_ID);

    return databaseHelper.createQuery(RidePersistenceContract.HeartRateEntry.TABLE_NAME,
        sql, rideId).mapToList(Mapper.CURSOR_TO_HEART_RATE_POINT).first().toSingle();
  }

  @Override public Single<Long> saveRide(Ride ride) {
    return Single.just(databaseHelper.newTransaction())
        .flatMap(transaction -> Single.fromCallable(() -> {
          ContentValues values = new ContentValues();
          values.put(RidePersistenceContract.RideEntry._ID, ride.getId());
          values.put(RidePersistenceContract.RideEntry.COLUMN_NAME, ride.getName());
          values.put(RidePersistenceContract.RideEntry.COLUMN_COMPLETED,
              ride.isCompleted());
          values.put(RidePersistenceContract.RideEntry.COLUMN_START_TIMESTAMP,
              ride.getInitialTimestamp());
          values.put(RidePersistenceContract.RideEntry.COLUMN_SYNCED, false);
          values.put(RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP,
              ride.getFinalTimestamp());
          // save ride
          return databaseHelper.insert(RidePersistenceContract.RideEntry.TABLE_NAME,
              values, SQLiteDatabase.CONFLICT_REPLACE);
        }).flatMap(result ->
            // save all accel captures from ride part
            saveAccelerometerData(ride.getId(), ride.getAccelerometerCaptures())
                // save all gravity captures from ride part
                .andThen(saveGravityData(ride.getId(), ride.getGravityCaptures()))
                // save all gps captures from ride part
                .concatWith(saveGpsData(ride.getId(), ride.getGpsCoordinates()))
                // save all heart rate captures from ride part
                .concatWith(saveHeartRateData(ride.getId(), ride.getHeartRateCaptures()))
                .doOnCompleted(() -> {
                  transaction.markSuccessful();
                  transaction.end();
                })
                .doOnError(err -> {
                  Timber.e(err);
                  transaction.end();
                })
                .toSingleDefault(result)));
  }

  @Override public Single<List<TriDimenPoint>> getGyroscopeData(String rideId) {
    final String[] projection = {
        RidePersistenceContract.GyroscopeEntry.COLUMN_RIDE_ID,
        RidePersistenceContract.GyroscopeEntry.COLUMN_TIMESTAMP,
        RidePersistenceContract.GyroscopeEntry.COLUMN_XX,
        RidePersistenceContract.GyroscopeEntry.COLUMN_YY,
        RidePersistenceContract.GyroscopeEntry.COLUMN_ZZ
    };
    String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
        TextUtils.join(",", projection),
        RidePersistenceContract.GyroscopeEntry.TABLE_NAME,
        RidePersistenceContract.GyroscopeEntry.COLUMN_RIDE_ID);
    return databaseHelper.createQuery(RidePersistenceContract.GyroscopeEntry.TABLE_NAME,
        sql, rideId).mapToList(Mapper.CURSOR_TO_GYROSCOPE_POINT).first().toSingle();
  }

  @Override public Single<Ride> getRide(String rideId) {
    Single<List<GpsPoint>> gpsPointsObservable = getGpsData(rideId);
    Single<List<TriDimenPoint>> accelPointsObservable = getAccelerometerData(rideId);
    Single<List<TriDimenPoint>> gravityPointsObservable = getGravityData(rideId);
    Single<List<TriDimenPoint>> gyroPointsObservable = getGyroscopeData(rideId);
    Single<List<HeartRatePoint>> heartRatePointsObservable = getHeartRateData(rideId);

    final String[] projection = {
        RidePersistenceContract.RideEntry._ID,
        RidePersistenceContract.RideEntry.COLUMN_NAME,
        RidePersistenceContract.RideEntry.COLUMN_COMPLETED,
        RidePersistenceContract.RideEntry.COLUMN_SYNCED,
        RidePersistenceContract.RideEntry.COLUMN_START_TIMESTAMP,
        RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP
    };
    String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
        TextUtils.join(",", projection), RidePersistenceContract.RideEntry.TABLE_NAME,
        RidePersistenceContract.RideEntry._ID);

    QueryObservable ridePartQuery =
        databaseHelper.createQuery(RidePersistenceContract.RideEntry.TABLE_NAME, sql,
            rideId);

    // query.run() ? maybe there is a more lazy and efficient way to do this

    return ridePartQuery.flatMapSingle(query -> {
      Cursor cursor = query.run();
      if (cursor == null || !cursor.moveToFirst()) {
        return Single.error(new IllegalStateException("Unable to run database query."));
      }
      // (cursor, gpsPoints, heartRatePoints, accelPoints, gravityPoints, gyroscopePoints) -> {

      return Single.zip(gpsPointsObservable, heartRatePointsObservable,
          accelPointsObservable, gravityPointsObservable, gyroPointsObservable,
          (gpsPoints, heartRatePoints, accelPoints, gravityPoints, gyroPoints) -> Mapper.CURSOR_TO_RIDE
              .call(cursor, gpsPoints, heartRatePoints, accelPoints, gravityPoints,
                  gyroPoints));
    }).first().toSingle();
  }

  @Override public Single<Boolean> markCompleted(@NonNull final String rideId) {
    return Single.fromCallable(() -> {
      BriteDatabase.Transaction transaction = databaseHelper.newTransaction();
      int affectedLines = 0;
      try {
        ContentValues values = new ContentValues();
        values.put(RidePersistenceContract.RideEntry.COLUMN_COMPLETED, SQL_TRUE);

        final long timeInMillis = Calendar.getInstance().getTimeInMillis();
        values.put(RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP, timeInMillis);

        // update ride
        affectedLines =
            databaseHelper.update(RidePersistenceContract.RideEntry.TABLE_NAME, values,
                String.format("%s = ?", RidePersistenceContract.RideEntry._ID), rideId);
        transaction.markSuccessful();
      } catch (Exception e) {
        Timber.e(e);
      } finally {
        transaction.end();
      }
      return affectedLines > 0;
    });
  }

  @Override public Single<List<Ride>> getCompletedRides() {
    return getCompletedRideIds().flatMap(rideIds -> getRides(rideIds));
  }

  @Override public Single<Boolean> markSynced(@NonNull final String rideId) {
    return Single.fromCallable(() -> {
      BriteDatabase.Transaction transaction = databaseHelper.newTransaction();
      int affectedLines = 0;
      try {
        ContentValues values = new ContentValues();
        values.put(RidePersistenceContract.RideEntry.COLUMN_SYNCED, SQL_TRUE);
        // update ride
        affectedLines =
            databaseHelper.update(RidePersistenceContract.RideEntry.TABLE_NAME, values,
                String.format("%s = ?", RidePersistenceContract.RideEntry._ID), rideId);
        transaction.markSuccessful();
      } catch (Exception e) {
        Timber.e(e);
      } finally {
        transaction.end();
      }
      return affectedLines > 0;
    });
  }

  private Single<List<Ride>> getRides(List<String> rideIds) {
    return Observable.from(rideIds)
        .flatMap(rideId -> getRide(rideId).toObservable())
        .toList()
        .toSingle();
  }

  private Single<List<String>> getCompletedRideIds() {
    final String[] projection = {
        RidePersistenceContract.RideEntry._ID,
        RidePersistenceContract.RideEntry.COLUMN_COMPLETED
    };

    String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
        TextUtils.join(",", projection), RidePersistenceContract.RideEntry.TABLE_NAME,
        RidePersistenceContract.RideEntry.COLUMN_COMPLETED);

    return databaseHelper.createQuery(RidePersistenceContract.RideEntry.TABLE_NAME, sql,
        Integer.toString(SQL_TRUE))
        .mapToList(Mapper.CURSOR_TO_RIDE_ID)
        .first()
        .toSingle();
  }
}
