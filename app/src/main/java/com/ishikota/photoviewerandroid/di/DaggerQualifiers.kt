package com.ishikota.photoviewerandroid.di

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class OkHttpClientQualifier(val type: Type) {
    enum class Type { Default, Api }
}
