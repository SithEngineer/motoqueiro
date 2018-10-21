package io.github.sithengineer.motoqueiro.ui.testing

import io.github.sithengineer.motoqueiro.R
import io.github.sithengineer.motoqueiro.ui.BaseFragment

@Deprecated("Not used")
class TestingFragment : BaseFragment<TestingPresenter>() {

  override fun getPresenter(): TestingPresenter? {
    return TestingPresenter()
  }

  override val viewId: Int
    get() = R.layout.fragment_testing
}
