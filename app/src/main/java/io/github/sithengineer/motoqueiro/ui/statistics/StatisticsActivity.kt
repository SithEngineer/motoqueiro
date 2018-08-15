package io.github.sithengineer.motoqueiro.ui.statistics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.sithengineer.motoqueiro.R
import io.github.sithengineer.motoqueiro.ui.BaseActivity

class StatisticsActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_content_frame)

    val fragment = StatisticsFragment.newInstance()

    val transaction = supportFragmentManager.beginTransaction()
    transaction.addToBackStack("statistics_activity_fragment")
        .replace(R.id.content_frame, fragment)
        .commit()

  }

  companion object {

    fun getNavigationIntent(context: Context): Intent {
      val intent = Intent(context, StatisticsActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      return intent
    }
  }
}
