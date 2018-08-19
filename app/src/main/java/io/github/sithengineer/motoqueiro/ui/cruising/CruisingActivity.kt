package io.github.sithengineer.motoqueiro.ui.cruising

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.sithengineer.motoqueiro.R
import io.github.sithengineer.motoqueiro.ui.BaseActivity

class CruisingActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_content_frame)

    val fragment = CruisingFragment.newInstance()
    supportFragmentManager.beginTransaction()
        .add(R.id.content_frame, fragment)
        .commit()

  }

  override fun onBackPressed() {
    // does nothing for now...

    // super.onBackPressed();
    // new CruisingNavigator(this).goToStatistics(false);
  }

  companion object {

    fun getNavigationIntent(context: Context): Intent {
      val intent = Intent(context, CruisingActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      return intent
    }
  }
}
