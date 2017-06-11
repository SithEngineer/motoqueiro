package io.github.sithengineer.motoqueiro.authentication;

import dagger.Module;
import dagger.Provides;

@Module public class AccountModule {
  @Provides AccountManager provideAccountManager() {
    return new AccountManager();
  }
}
