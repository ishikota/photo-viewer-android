package com.ishikota.photoviewerandroid.infra.moshi

import com.squareup.moshi.Moshi

fun buildDefaultMoshi() = Moshi.Builder()
//    .add(DateAdapter()) TODO
    .build()


