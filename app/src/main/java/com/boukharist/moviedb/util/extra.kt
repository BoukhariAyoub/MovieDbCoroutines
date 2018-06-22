package com.boukharist.moviedb.util

import android.app.Activity

/**
 * Retrieve extra from Activity intent
 */
inline fun <reified T> Activity.extra(key: String) =
        lazy { intent.extras[key] as? T ?: error("Intent Argument $key is missing") }

/**
 * get Tag for any Object
 */
fun Any.getTag(): String = this.javaClass.simpleName