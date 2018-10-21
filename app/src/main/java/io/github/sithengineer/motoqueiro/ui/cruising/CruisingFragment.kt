package io.github.sithengineer.motoqueiro.ui.cruising

import com.jakewharton.rxbinding2.view.RxView
import io.github.sithengineer.motoqueiro.MotoqueiroApp
import io.github.sithengineer.motoqueiro.R
import io.github.sithengineer.motoqueiro.hardware.bluetooth.MiBandModule
import io.github.sithengineer.motoqueiro.ui.BaseFragment
import io.github.sithengineer.motoqueiro.ui.cruising.CruisingContract.Presenter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_capturing_data.stop_button
import javax.inject.Inject

class CruisingFragment : BaseFragment<Presenter>(), CruisingContract.View {

  override fun getPresenter(): Presenter? = presenter

  @Inject
  internal lateinit var presenter: CruisingContract.Presenter

  override val viewId: Int
    get() = R.layout.fragment_capturing_data

  override fun inject() {
    val context = context
    MotoqueiroApp[context!!]
        .createUiComponent()
        .with(CruisingModule(this, CruisingNavigatorImpl(activity!!)),
            MiBandModule(context))
        .inject(this)
  }

  override fun setStopButtonEnabled() {
    stop_button.isEnabled = true
  }

  override fun setStopButtonDisabled() {
    stop_button.isEnabled = false
  }

  override fun stopClick(): Observable<Any> {
    return RxView.clicks(stop_button)
  }

  companion object {

    fun newInstance(): CruisingFragment {
      return CruisingFragment()
    }
  }
}
