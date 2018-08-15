package io.github.sithengineer.motoqueiro.data.sync

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.Context

class DummyAccountCreator {

  fun addAccount(context: Context): Account? {
    val accountManager = context.getSystemService(Activity.ACCOUNT_SERVICE) as AccountManager
    val account = Account(ACCOUNT, ACCOUNT_TYPE)
    return if (accountManager.addAccountExplicitly(account, null, null)) {
      // account was added
      account
    } else {
      // account already exists or an error has occurred
      null
    }
  }

  companion object {
    private const val AUTHORITY = "io.github.sithengineer.motoqueiro.data.sync"
    private const val ACCOUNT_TYPE = ""
    private const val ACCOUNT = "dummyaccount"
  }
}
