package io.github.sithengineer.motoqueiro.data.local;

public class User {
  private final String id;
  private final boolean isLoggedIn;

  public User(String id, boolean isLoggedIn) {
    this.id = id;
    this.isLoggedIn = isLoggedIn;
  }

  public String getId() {
    return id;
  }

  public boolean isLoggedIn() {
    return isLoggedIn;
  }
}
