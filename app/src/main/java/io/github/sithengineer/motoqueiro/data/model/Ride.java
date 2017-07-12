package io.github.sithengineer.motoqueiro.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;

public class Ride {

  @Expose @SerializedName("id") private final String id;
  @Expose @SerializedName("tripName") private final String name;
  @Expose @SerializedName("initialTimestamp") private final long initialTimestamp;
  @Expose @SerializedName("finalTimestamp") private long finalTimestamp;
  @Expose @SerializedName("completed") private boolean completed;
  @Expose @SerializedName("accelerometerCapturesCount") private int
      accelerometerCapturesCount;
  @Expose @SerializedName("accelerometerCaptures") private List<TriDimenPoint>
      accelerometerCaptures;
  @Expose @SerializedName("gravityCapturesCount") private int gravityCapturesCount;
  @Expose @SerializedName("gravityCaptures") private List<TriDimenPoint> gravityCaptures;
  @Expose @SerializedName("gpsCoordinatesCount") private int gpsCoordinatesCount;
  @Expose @SerializedName("gpsCoordinates") private List<GpsPoint> gpsCoordinates;
  @Expose @SerializedName("heartRateCapturesCount") private int heartRateCapturesCount;
  @Expose @SerializedName("heartRateCaptures") private List<HeartRatePoint>
      heartRateCaptures;
  @Expose @SerializedName("gyroscopeCaptures") private List<TriDimenPoint>
      gyroscopeCaptures;
  @Expose @SerializedName("gyroscopeCapturesCount") private int gyroscopeCapturesCount;

  private boolean synced;

  public Ride(String id, String name, long initialTimestamp, long finalTimestamp,
      boolean completed, boolean synced) {
    this.id = id;
    this.name = name;
    this.initialTimestamp = initialTimestamp;
    this.finalTimestamp = finalTimestamp;
    this.completed = completed;
    this.synced = synced;
    this.accelerometerCaptures = new LinkedList<>();
    this.gravityCaptures = new LinkedList<>();
    this.gyroscopeCaptures = new LinkedList<>();
    this.gpsCoordinates = new LinkedList<>();
    this.heartRateCaptures = new LinkedList<>();
  }

  public String getId() {
    return id;
  }

  public long getInitialTimestamp() {
    return initialTimestamp;
  }

  public long getFinalTimestamp() {
    return finalTimestamp;
  }

  public void setFinalTimestamp(long finalTimestamp) {
    this.finalTimestamp = finalTimestamp;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public String getName() {
    return name;
  }

  public List<TriDimenPoint> getAccelerometerCaptures() {
    return accelerometerCaptures;
  }

  public void setAccelerometerCaptures(List<TriDimenPoint> accelerometerCaptures) {
    this.accelerometerCaptures = accelerometerCaptures;
    if (accelerometerCaptures != null && !accelerometerCaptures.isEmpty()) {
      accelerometerCapturesCount = accelerometerCaptures.size();
    }
  }

  public int getAccelerometerCapturesCount() {
    return accelerometerCapturesCount;
  }

  public void setAccelerometerCapturesCount(int accelerometerCapturesCount) {
    this.accelerometerCapturesCount = accelerometerCapturesCount;
  }

  public int getGravityCapturesCount() {
    return gravityCapturesCount;
  }

  public void setGravityCapturesCount(int gravityCapturesCount) {
    this.gravityCapturesCount = gravityCapturesCount;
  }

  public int getGpsCoordinatesCount() {
    return gpsCoordinatesCount;
  }

  public void setGpsCoordinatesCount(int gpsCoordinatesCount) {
    this.gpsCoordinatesCount = gpsCoordinatesCount;
  }

  public List<TriDimenPoint> getGyroscopeCaptures() {
    return gyroscopeCaptures;
  }

  public void setGyroscopeCaptures(List<TriDimenPoint> gyroscopeCaptures) {
    this.gyroscopeCaptures = gyroscopeCaptures;
    if (gyroscopeCaptures != null && !gyroscopeCaptures.isEmpty()) {
      gyroscopeCapturesCount = gyroscopeCaptures.size();
    }
  }

  public int getGyroscopeCapturesCount() {
    return gyroscopeCapturesCount;
  }

  public void setGyroscopeCapturesCount(int gyroscopeCapturesCount) {
    this.gyroscopeCapturesCount = gyroscopeCapturesCount;
  }

  public int getHeartRateCapturesCount() {
    return heartRateCapturesCount;
  }

  public void setHeartRateCapturesCount(int heartRateCapturesCount) {
    this.heartRateCapturesCount = heartRateCapturesCount;
  }

  public List<TriDimenPoint> getGravityCaptures() {
    return gravityCaptures;
  }

  public void setGravityCaptures(List<TriDimenPoint> gravityCaptures) {
    this.gravityCaptures = gravityCaptures;
    if (gravityCaptures != null && !gravityCaptures.isEmpty()) {
      gravityCapturesCount = gravityCaptures.size();
    }
  }

  public List<GpsPoint> getGpsCoordinates() {
    return gpsCoordinates;
  }

  public void setGpsCoordinates(List<GpsPoint> gpsCoordinates) {
    this.gpsCoordinates = gpsCoordinates;
    if (gpsCoordinates != null && !gpsCoordinates.isEmpty()) {
      gpsCoordinatesCount = gpsCoordinates.size();
    }
  }

  public List<HeartRatePoint> getHeartRateCaptures() {
    return heartRateCaptures;
  }

  public void setHeartRateCaptures(List<HeartRatePoint> heartRateCaptures) {
    this.heartRateCaptures = heartRateCaptures;
    if (heartRateCaptures != null && !heartRateCaptures.isEmpty()) {
      heartRateCapturesCount = heartRateCaptures.size();
    }
  }

  public void addGpsCoord(long timestamp, double lat, double lon) {
    gpsCoordinates.add(new GpsPoint(lat, lon, timestamp));
    gpsCoordinatesCount++;
  }

  public void addAccelerometerPoint(long timestamp, float x, float y, float z) {
    accelerometerCaptures.add(new TriDimenPoint(x, y, z, timestamp));
    accelerometerCapturesCount++;
  }

  public void addGravityPoint(long timestamp, float x, float y, float z) {
    gravityCaptures.add(new TriDimenPoint(x, y, z, timestamp));
    gravityCapturesCount++;
  }

  public void addGyroscopePoint(long timestamp, float x, float y, float z) {
    gyroscopeCaptures.add(new TriDimenPoint(x, y, z, timestamp));
    gyroscopeCapturesCount++;
  }

  public void addHeartRatePoint(long timestamp, int bpm) {
    heartRateCaptures.add(new HeartRatePoint(bpm, timestamp));
    heartRateCapturesCount++;
  }

  public void setEndTimestamp(long time) {
    finalTimestamp = time;
    completed = true;
  }

  public boolean isSynced() {
    return synced;
  }

  public void setSynced(boolean synced) {
    this.synced = synced;
  }
}
