package io.github.sithengineer.motoqueiro.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;

public class RidePart {

  @Expose @SerializedName("id") private final String id;
  @Expose @SerializedName("tripName") private final String name;
  @Expose @SerializedName("initialTimestamp") private final long initialTimestamp;
  @Expose @SerializedName("finalTimestamp") private long finalTimestamp;
  @Expose @SerializedName("completed") private boolean completed;
  @Expose @SerializedName("accelerometerCaptures") private List<TriDimenPoint> accelerometerCaptures;
  @Expose @SerializedName("gravityCaptures") private List<TriDimenPoint> gravityCaptures;
  @Expose @SerializedName("gpsCoordinates") private List<GpsPoint> gpsCoordinates;
  @Expose @SerializedName("heartRateCaptures") private List<HeartRatePoint> heartRateCaptures;
  private boolean synced;

  public RidePart(String id, String name, long initialTimestamp, long finalTimestamp,
      boolean completed, boolean synced) {
    this.id = id;
    this.name = name;
    this.initialTimestamp = initialTimestamp;
    this.finalTimestamp = finalTimestamp;
    this.completed = completed;
    this.synced = synced;
    this.accelerometerCaptures = new LinkedList<>();
    this.gravityCaptures = new LinkedList<>();
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

  public boolean isCompleted() {
    return completed;
  }

  public String getName() {
    return name;
  }

  public List<TriDimenPoint> getAccelerometerCaptures() {
    return accelerometerCaptures;
  }

  public void setAccelerometerCaptures(List<TriDimenPoint> accelerometerCaptures) {
    this.accelerometerCaptures = accelerometerCaptures;
  }

  public List<TriDimenPoint> getGravityCaptures() {
    return gravityCaptures;
  }

  public void setGravityCaptures(List<TriDimenPoint> gravityCaptures) {
    this.gravityCaptures = gravityCaptures;
  }

  public List<GpsPoint> getGpsCoordinates() {
    return gpsCoordinates;
  }

  public void setGpsCoordinates(List<GpsPoint> gpsCoordinates) {
    this.gpsCoordinates = gpsCoordinates;
  }

  public List<HeartRatePoint> getHeartRateCaptures() {
    return heartRateCaptures;
  }

  public void setHeartRateCaptures(List<HeartRatePoint> heartRateCaptures) {
    this.heartRateCaptures = heartRateCaptures;
  }

  public void addGpsCoord(long timestamp, double lat, double lon) {
    gpsCoordinates.add(new GpsPoint(lat, lon, timestamp));
  }

  public void addAccelerometerPoint(long timestamp, float x, float y, float z) {
    accelerometerCaptures.add(new TriDimenPoint(x, y, z, timestamp));
  }

  public void addGravityPoint(long timestamp, float x, float y, float z) {
    gravityCaptures.add(new TriDimenPoint(x, y, z, timestamp));
  }

  public void addHeartRatePoint(long timestamp, int bpm) {
    heartRateCaptures.add(new HeartRatePoint(bpm, timestamp));
  }

  public void setEndTimestamp(long time) {
    finalTimestamp = time;
    completed = true;
  }

  public void setFinalTimestamp(long finalTimestamp) {
    this.finalTimestamp = finalTimestamp;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public boolean isSynced() {
    return synced;
  }

  public void setSynced(boolean synced) {
    this.synced = synced;
  }
}
