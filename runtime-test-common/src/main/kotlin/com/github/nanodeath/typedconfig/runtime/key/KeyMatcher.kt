package com.github.nanodeath.typedconfig.runtime.key

import com.github.nanodeath.typedconfig.runtime.key.Key
import io.mockk.Matcher
import io.mockk.MockKMatcherScope

data class KeyMatcher(val expectedName: String) : Matcher<Key<*>> {
    override fun match(arg: Key<*>?): Boolean = arg != null && arg.name == expectedName
}

fun MockKMatcherScope.keyWithName(name: String): Key<*> = match(KeyMatcher(name))
