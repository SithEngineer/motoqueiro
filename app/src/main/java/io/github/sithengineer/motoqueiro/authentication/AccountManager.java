package io.github.sithengineer.motoqueiro.authentication;

import android.accounts.Account;
import io.github.sithengineer.motoqueiro.data.local.User;
import rx.Observable;
import timber.log.Timber;

public class AccountManager {

  // The authority for the sync adapter's content provider
  protected static final String AUTHORITY =
      "io.github.sithengineer.motoqueiro.datasync.provider";
  // An account type, in the form of a domain name
  protected static final String ACCOUNT_TYPE = "motoqueiro.sithengineer.github.io";
  // The account name
  protected static final String ACCOUNT = "dummyaccount";
  private final android.accounts.AccountManager androidAccountManager;

  public AccountManager(android.accounts.AccountManager androidAccountManager) {
    this.androidAccountManager = androidAccountManager;
  }

  /**
   * @return Dummy account
   */
  private Account createSyncAccount() {
    Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
    if (!androidAccountManager.addAccountExplicitly(newAccount, null, null)) {
      Timber.e(new IllegalStateException("Unable to add dummy account to sync data."));
      return null;
    }
    return newAccount;
  }

  Account getMainUserAccount() {
    Account[] accounts = androidAccountManager.getAccountsByType("com.google");
    if (accounts != null && accounts.length > 0) {
      return accounts[0];
    } else {
      return createSyncAccount();
    }
  }

  public User getUserSync() {
    try {
      return new User(getMainUserAccount().name, true);
    } catch (Exception e) {
      Timber.e(e);
    }
    return new User("", false);
  }

  public Observable<User> getUser() {
    // todo
    return Observable.just(getUserSync());
  }
}
