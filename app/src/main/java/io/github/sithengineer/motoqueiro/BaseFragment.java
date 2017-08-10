package io.github.sithengineer.motoqueiro;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends Presenter> extends Fragment {

  private Unbinder unBinder;

  @LayoutRes protected abstract int getViewId();

  public void inject() {
    // optional method
  }

  @Nullable public P getPresenter() {
    return null;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(getViewId(), container, false);
    unBinder = ButterKnife.bind(this, view);
    loadArguments(getArguments());
    inject();
    P presenter = getPresenter();
    if (presenter != null) {
      presenter.start();
    }
    return view;
  }

  protected void loadArguments(@Nullable Bundle args) {
    // optional method
  }

  @Override public void onDestroyView() {
    if (unBinder != null) {
      unBinder.unbind();
    }
    P presenter = getPresenter();
    if (presenter != null) {
      presenter.stop();
    }
    super.onDestroyView();
  }
}
