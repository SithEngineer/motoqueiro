package io.github.sithengineer.motoqueiro.ui.learning

import io.github.sithengineer.motoqueiro.R
import io.github.sithengineer.motoqueiro.ui.BaseFragment

@Deprecated("Not used")
class LearningFragment : BaseFragment<LearningPresenter>() {

  override fun getPresenter(): LearningPresenter? = LearningPresenter()

  override val viewId: Int
    get() = R.layout.fragment_learning
}
