package io.github.sithengineer.motoqueiro.ui.cruising;

import dagger.Module;
import dagger.Provides;
import io.github.sithengineer.motoqueiro.scope.ActivityScope;
import io.github.sithengineer.motoqueiro.util.CompositeSubscriptionManager;

@Module public class CruisingModule {

  private final CruisingContract.View view;
  private final CruisingNavigator cruisingNavigator;

  public CruisingModule(CruisingContract.View view, CruisingNavigator cruisingNavigator) {
    this.view = view;
    this.cruisingNavigator = cruisingNavigator;
  }

  @Provides @ActivityScope CruisingContract.View provideView() {
    return view;
  }

  @Provides @ActivityScope CruisingNavigator provideNavigator() {
    return cruisingNavigator;
  }

  @Provides @ActivityScope CompositeSubscriptionManager provideCompositeSubscriptionManager() {
    return new CompositeSubscriptionManager();
  }

  @Provides @ActivityScope CruisingContract.Presenter provideCruisingPresenter(
      CruisingContract.View view, CompositeSubscriptionManager subscriptionManager,
      CruisingNavigator navigator) {
    return new CruisingPresenter(view, subscriptionManager, navigator);
  }
}
