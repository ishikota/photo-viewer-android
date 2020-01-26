package com.ishikota.photoviewerandroid.ui.userdetail

import com.google.common.truth.Expect
import org.junit.Rule
import org.junit.Test

class UserDetailBindingAdaptersTest {

    @Rule
    @JvmField
    val expect: Expect = Expect.create()

    @Test
    fun testUserDetailFindFirstNumberPositionFromString() {
        val found = userDetailFindFirstNumberPositionFromString("123 followers")
        expect.that(found).isEqualTo(Pair(0, 3))

        val notFound = userDetailFindFirstNumberPositionFromString("no followers")
        expect.that(notFound).isNull()
    }
}
