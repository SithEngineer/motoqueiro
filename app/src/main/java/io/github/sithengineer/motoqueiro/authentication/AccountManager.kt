package io.github.sithengineer.motoqueiro.authentication

import android.accounts.Account
import io.github.sithengineer.motoqueiro.data.local.entity.User
import timber.log.Timber

class AccountManager(private val androidAccountManager: android.accounts.AccountManager) {

  private val mainUserAccount: Account?
    get() {
      val accounts = androidAccountManager.getAccountsByType("com.google")
      return if (accounts != null && accounts.isNotEmpty()) {
        accounts[0]
      } else {
        createSyncAccount()
      }
    }

  val userSync: User
    get() {
      try {
        val accountName = mainUserAccount!!.name

        return User(isLoggedIn = true, name = accountName)
      } catch (e: Exception) {
        Timber.e(e)
      }

      return User(isLoggedIn = false, name = "")
    }

  /**
   * @return Dummy account
   */
  private fun createSyncAccount(): Account? {
    val newAccount = Account(ACCOUNT, ACCOUNT_TYPE)
    if (!androidAccountManager.addAccountExplicitly(newAccount, null, null)) {
      Timber.e(IllegalStateException("Unable to add dummy account to sync data."))
      return null
    }
    return newAccount
  }

  companion object {
    // The authority for the sync adapter's content provider
    private const val AUTHORITY = "io.github.sithengineer.motoqueiro.datasync.provider"
    // An account type, in the form of a domain name
    private const val ACCOUNT_TYPE = "motoqueiro.sithengineer.github.io"
    // The account name
    private const val ACCOUNT = "dummyaccount"
  }
}
