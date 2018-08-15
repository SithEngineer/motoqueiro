package io.github.sithengineer.motoqueiro.ui.statistics

import dagger.Subcomponent
import io.github.sithengineer.motoqueiro.scope.ActivityScope

@ActivityScope
@Subcomponent(modules = [StatisticsModule::class])
interface StatisticsComponent {
  fun inject(fragment: StatisticsFragment)
}
