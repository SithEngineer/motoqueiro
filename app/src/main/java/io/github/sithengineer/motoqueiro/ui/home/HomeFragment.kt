package io.github.sithengineer.motoqueiro.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.github.sithengineer.motoqueiro.MotoqueiroApp
import io.github.sithengineer.motoqueiro.PermissionAuthority
import io.github.sithengineer.motoqueiro.R
import io.github.sithengineer.motoqueiro.ui.BaseFragment
import io.github.sithengineer.motoqueiro.ui.home.HomeContract.Presenter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.miband_address_input
import kotlinx.android.synthetic.main.fragment_home.miband_address_input_wrapper
import kotlinx.android.synthetic.main.fragment_home.phone_position_spinner
import kotlinx.android.synthetic.main.fragment_home.ride_name_input
import kotlinx.android.synthetic.main.fragment_home.start_ride
import javax.inject.Inject

class HomeFragment : BaseFragment<Presenter>(), HomeContract.View {

  @Inject
  internal lateinit var presenter: HomeContract.Presenter

  override fun getPresenter(): Presenter? = presenter

  public override val viewId: Int
    get() = R.layout.fragment_home

  override fun miBandAddressChanges(): Observable<String> =
      RxTextView.textChanges(miband_address_input).map { text -> text.toString() }

  override fun selectedDevicePosition(): Int = phone_position_spinner.selectedItemPosition

  override fun inject() {
    MotoqueiroApp[context!!]
        .createUiComponent()
        .with(HomeModule(this, HomeNavigator(activity!!), activity as PermissionAuthority))
        .inject(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = super.onCreateView(inflater, container, savedInstanceState)
    val adapter = ArrayAdapter.createFromResource(activity!!, R.array.home_wheres_phone_array,
        android.R.layout.simple_spinner_item)
    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
    phone_position_spinner.adapter = adapter
    return view
  }

  override fun handleStartRideClick(): Observable<Any> {
    return RxView.clicks(start_ride)
  }

  override fun sendToGpsSettings() {
    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
  }

  override fun showActivateGpsViewMessage() {
    Snackbar.make(start_ride, R.string.home_activate_gps, Snackbar.LENGTH_LONG).show()
  }

  override fun showGenericError() {
    Snackbar.make(start_ride, R.string.home_generic_error, Snackbar.LENGTH_LONG).show()
  }

  override fun showMiBandAddress(miBandAddress: String) {
    this.miband_address_input.setText(miBandAddress)
  }

  override fun showMiBandAddressError() {
    miband_address_input_wrapper.error = getString(R.string.home_view_heart_rate_band_address_error)
  }

  override fun cleanMiBandAddressError() {
    miband_address_input_wrapper.error = null
  }

  override fun showGivePermissionsMessage() {
    Snackbar.make(start_ride, R.string.home_need_all_permissions,
        Snackbar.LENGTH_LONG).show()
  }

  override fun getRideName(): String {
    return ride_name_input.text.toString()
  }

  companion object {

    fun newInstance(): HomeFragment {
      return HomeFragment()
    }
  }
}
