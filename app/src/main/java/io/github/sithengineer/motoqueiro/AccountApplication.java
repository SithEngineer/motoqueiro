package io.github.sithengineer.motoqueiro;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import timber.log.Timber;

class AccountApplication extends android.app.Application {

  // The authority for the sync adapter's content provider
  protected static final String AUTHORITY =
      "io.github.sithengineer.motoqueiro.datasync.provider";
  // An account type, in the form of a domain name
  protected static final String ACCOUNT_TYPE = "motoqueiro.sithengineer.github.io";
  // The account name
  protected static final String ACCOUNT = "dummyaccount";

  /**
   * @param context The application context.
   * @return Dummy account
   */
  private static Account createSyncAccount(Context context) {
    Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
    AccountManager accountManager =
        (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
    if (!accountManager.addAccountExplicitly(newAccount, null, null)) {
      Timber.e(new IllegalStateException("Unable to add dummy account to sync data."));
      return null;
    }
    return newAccount;
  }

  Account getMainUserAccount() {
    AccountManager am = AccountManager.get(this);
    Account[] accounts = am.getAccountsByType("com.google");
    if (accounts != null && accounts.length > 0) {
      return accounts[0];
    } else {
      return createSyncAccount(getApplicationContext());
    }
  }
}
