package io.github.sithengineer.motoqueiro.ui.statistics

import com.jakewharton.rxbinding2.view.RxView
import io.github.sithengineer.motoqueiro.MotoqueiroApp
import io.github.sithengineer.motoqueiro.R
import io.github.sithengineer.motoqueiro.ui.BaseFragment
import io.github.sithengineer.motoqueiro.ui.statistics.StatisticsContract.Presenter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_statistics.goHomeButton
import javax.inject.Inject

class StatisticsFragment : BaseFragment<Presenter>(), StatisticsContract.View {

  @Inject
  internal lateinit var presenter: StatisticsContract.Presenter

  override fun getPresenter(): Presenter? = presenter

  override val viewId: Int
    get() = R.layout.fragment_statistics

  override fun inject() {
    MotoqueiroApp[context!!].createUiComponent()
        .with(StatisticsModule(this, StatisticsNavigator(activity!!)))
        .inject(this)
  }

  override fun goHomeClick(): Observable<Any> {
    return RxView.clicks(goHomeButton)
  }

  companion object {

    fun newInstance(): StatisticsFragment {
      return StatisticsFragment()
    }
  }
}
