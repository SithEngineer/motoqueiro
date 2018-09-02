package io.github.sithengineer.motoqueiro.util

import timber.log.Timber.DebugTree

class DebugTree : DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return "${super.createStackElementTag(element)}:$element.lineNumber"
    }
}