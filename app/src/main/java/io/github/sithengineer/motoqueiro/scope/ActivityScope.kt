package io.github.sithengineer.motoqueiro.scope

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

@Scope
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(RUNTIME)
annotation class ActivityScope
