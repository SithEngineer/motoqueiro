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
import io.github.sithengineer.motoqueiro.data.model.RidePart;
import io.github.sithengineer.motoqueiro.data.model.TriDimenPoint;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import rx.Completable;
import rx.Observable;
import rx.Scheduler;
import rx.Single;
import timber.log.Timber;

import static dagger.internal.Preconditions.checkNotNull;

public class RideLocalDataSource implements RideDataSource {

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
            .doOnCompleted(() -> transaction.markSuccessful())
            .doAfterTerminate(() -> transaction.end())
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

  @Override public Observable<List<GpsPoint>> getGpsData(String rideId) {
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
        rideId).mapToList(Mapper.CURSOR_TO_GPS_POINT);
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
            .doOnCompleted(() -> transaction.markSuccessful())
            .doAfterTerminate(() -> transaction.end())
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

  @Override public Observable<List<TriDimenPoint>> getAccelerometerData(String rideId) {
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
        .mapToList(Mapper.CURSOR_TO_ACCEL_POINT);
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
            .doOnCompleted(() -> transaction.markSuccessful())
            .doAfterTerminate(() -> transaction.end())
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

  @Override public Observable<List<TriDimenPoint>> getGravityData(String rideId) {
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
        sql, rideId).mapToList(Mapper.CURSOR_TO_GRAVITY_POINT);
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
            .doOnCompleted(() -> transaction.markSuccessful())
            .doAfterTerminate(() -> transaction.end())
            .toCompletable());
  }

  @Override public Single<Long> saveHeartRateData(String rideId, HeartRatePoint point) {
    return Single.fromCallable(() -> {
      checkNotNull(rideId);
      checkNotNull(point);

      ContentValues values = new ContentValues();
      values.put(RidePersistenceContract.HeartRateEntry.COLUMN_RIDE_ID, rideId);
      values.put(RidePersistenceContract.HeartRateEntry.COLUMN_BPM,
          point.getBeatsPerMinute());
      values.put(RidePersistenceContract.HeartRateEntry.COLUMN_TIMESTAMP,
          point.getTimestamp());

      return databaseHelper.insert(RidePersistenceContract.HeartRateEntry.TABLE_NAME,
          values, SQLiteDatabase.CONFLICT_REPLACE);
    });
  }

  @Override public Observable<List<HeartRatePoint>> getHeartRateData(String rideId) {
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
        sql, rideId).mapToList(Mapper.CURSOR_TO_HEART_RATE_POINT);
  }

  @Override public Completable saveRide(RidePart ride) {
    return Completable.fromCallable(() -> checkNotNull(ride))
        .andThen(Single.just(databaseHelper.newTransaction()))
        .flatMapCompletable(transaction -> Completable.fromAction(() -> {
          ContentValues values = new ContentValues();
          values.put(RidePersistenceContract.RideEntry._ID, ride.getId());
          values.put(RidePersistenceContract.RideEntry.COLUMN_NAME, ride.getId());
          values.put(RidePersistenceContract.RideEntry.COLUMN_START_TIMESTAMP,
              ride.getInitialTimestamp());
          // save ride
          databaseHelper.insert(RidePersistenceContract.RideEntry.TABLE_NAME, values,
              SQLiteDatabase.CONFLICT_REPLACE);
        })
            // save all accel captures from ride part
            .andThen(saveAccelerometerData(ride.getId(), ride.getAccelerometerCaptures()))
            // save all gravity captures from ride part
            .andThen(saveGravityData(ride.getId(), ride.getGravityCaptures()))
            // save all gps captures from ride part
            .andThen(saveGpsData(ride.getId(), ride.getGpsCoordinates()))
            // save all heart rate captures from ride part
            .andThen(saveHeartRateData(ride.getId(), ride.getHeartRateCaptures()))
            .doOnError(ex -> Timber.e(ex))
            .doOnCompleted(() -> transaction.markSuccessful())
            .doAfterTerminate(() -> transaction.end()));
  }

  @Override public Observable<RidePart> getRide(String rideId) {
    Observable<List<GpsPoint>> gpsPointsObservable = getGpsData(rideId);
    Observable<List<TriDimenPoint>> accelPointsObservable = getAccelerometerData(rideId);
    Observable<List<TriDimenPoint>> gravityPointsObservable = getGravityData(rideId);
    Observable<List<HeartRatePoint>> heartRatePointsObservable = getHeartRateData(rideId);

    final String[] projection = {
        RidePersistenceContract.RideEntry._ID,
        RidePersistenceContract.RideEntry.COLUMN_NAME,
        RidePersistenceContract.RideEntry.COLUMN_START_TIMESTAMP,
        RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP
    };
    String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
        TextUtils.join(",", projection), RidePersistenceContract.RideEntry.TABLE_NAME,
        RidePersistenceContract.RideEntry._ID);

    QueryObservable ridePartQuery =
        databaseHelper.createQuery(RidePersistenceContract.HeartRateEntry.TABLE_NAME, sql,
            rideId);

    // query.run() ? maybe there is a more lazy and efficient way to do this

    return ridePartQuery.flatMap(query -> {
      Cursor cursor = query.run();
      if (cursor == null || !cursor.moveToFirst()) {
        return Observable.error(
            new IllegalStateException("Unable to run database query."));
      }
      return Observable.zip(gpsPointsObservable, accelPointsObservable,
          gravityPointsObservable, heartRatePointsObservable,
          (gpsPoints, accelPoints, gravityPoints, heartRatePoints) -> Mapper.CURSOR_TO_RIDE
              .call(cursor, gpsPoints, heartRatePoints, accelPoints, gravityPoints));
    });
  }

  @Override public Completable markCompleted(String rideId) {
    return Completable.fromCallable(() -> checkNotNull(rideId))
        .andThen(Single.just(databaseHelper.newTransaction()))
        .flatMapCompletable(transaction -> Completable.fromAction(() -> {
          ContentValues values = new ContentValues();
          values.put(RidePersistenceContract.RideEntry._ID, rideId);
          values.put(RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP,
              new Date().getTime());
          values.put(RidePersistenceContract.RideEntry.COLUMN_COMPLETED, 1);
          // save ride
          databaseHelper.insert(RidePersistenceContract.RideEntry.TABLE_NAME, values,
              SQLiteDatabase.CONFLICT_REPLACE);
        })
            .doOnError(ex -> Timber.e(ex))
            .doOnCompleted(() -> transaction.markSuccessful())
            .doAfterTerminate(() -> transaction.end()));
  }

  @Override public Single<List<RidePart>> getCompletedRides() {

    final String[] projection = {
        RidePersistenceContract.RideEntry._ID,
        RidePersistenceContract.RideEntry.COLUMN_NAME,
        RidePersistenceContract.RideEntry.COLUMN_START_TIMESTAMP,
        RidePersistenceContract.RideEntry.COLUMN_END_TIMESTAMP
    };
    String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
        TextUtils.join(",", projection), RidePersistenceContract.RideEntry.TABLE_NAME,
        RidePersistenceContract.RideEntry._ID);

    // todo

    return Single.just(Collections.EMPTY_LIST);
  }

  @Override public Completable markSynced(String rideId) {
    return Completable.fromCallable(() -> checkNotNull(rideId))
        .andThen(Single.just(databaseHelper.newTransaction()))
        .flatMapCompletable(transaction -> Completable.fromAction(() -> {
          ContentValues values = new ContentValues();
          values.put(RidePersistenceContract.RideEntry._ID, rideId);
          values.put(RidePersistenceContract.RideEntry.COLUMN_SYNCED, 1);
          // save ride
          databaseHelper.insert(RidePersistenceContract.RideEntry.TABLE_NAME, values,
              SQLiteDatabase.CONFLICT_REPLACE);
        })
            .doOnError(ex -> Timber.e(ex))
            .doOnCompleted(() -> transaction.markSuccessful())
            .doAfterTerminate(() -> transaction.end()));
  }
}
