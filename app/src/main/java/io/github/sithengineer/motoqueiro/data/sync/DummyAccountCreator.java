package io.github.sithengineer.motoqueiro.data.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

public class DummyAccountCreator {

  private static final String AUTHORITY = "io.github.sithengineer.motoqueiro.data.sync";
  private static final String ACCOUNT_TYPE = "";
  private static final String ACCOUNT = "dummyaccount";

  public void addAccount(@NonNull Context context) {
    AccountManager accountManager =
        (AccountManager) context.getSystemService(Activity.ACCOUNT_SERVICE);
    Account account = new Account(ACCOUNT, ACCOUNT_TYPE);
    if (accountManager.addAccountExplicitly(account, null, null)) {
      // account was added
    } else {
      // account already exists or an error has occurred
    }
  }
}
