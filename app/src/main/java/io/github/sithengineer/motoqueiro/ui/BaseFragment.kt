package io.github.sithengineer.motoqueiro.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment<P : Presenter> : Fragment() {

  @get:LayoutRes
  protected abstract val viewId: Int

  abstract fun getPresenter(): P?

  open fun inject() {
    // optional method
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(viewId, container, false)
    loadArguments(arguments)
    inject()
    val presenter = getPresenter()
    presenter?.start()
    return view
  }

  open fun loadArguments(args: Bundle?) {
    // optional method
  }

  override fun onDestroyView() {
    val presenter = getPresenter()
    presenter?.stop()
    super.onDestroyView()
  }
}
