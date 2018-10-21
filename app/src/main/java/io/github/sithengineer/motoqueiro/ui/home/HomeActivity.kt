package io.github.sithengineer.motoqueiro.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.github.sithengineer.motoqueiro.R
import io.github.sithengineer.motoqueiro.ui.BaseActivity

class HomeActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_content_frame)

    val fragment = HomeFragment.newInstance()

    supportFragmentManager.beginTransaction()
        .add(R.id.content_frame, fragment)
        .commit()
  }

  companion object {

    fun getNavigationIntent(context: Context): Intent {
      val intent = Intent(context, HomeActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      return intent
    }
  }
}
