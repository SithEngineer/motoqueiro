package io.github.sithengineer.motoqueiro.app;

import android.content.SharedPreferences;

public class Preferences {
  private static final String MI_BAND_ADDRESS = "mi_band_address";
  private static final String RIDE_ID = "ride_id";

  private final SharedPreferences preferences;

  public Preferences(SharedPreferences preferences) {
    this.preferences = preferences;
  }

  public String getMiBandAddressOrDefault() {
    return preferences.getString(MI_BAND_ADDRESS, "");
  }

  public void setMiBandAddress(String miBandAddress) {
    preferences.edit().putString(MI_BAND_ADDRESS, miBandAddress).apply();
  }

  public String getRideId() {
    return preferences.getString(RIDE_ID, null);
  }

  public void setRideId(String rideId) {
    preferences.edit().putString(RIDE_ID, rideId).apply();
  }
}
