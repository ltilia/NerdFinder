package com.bignerdranch.android.nerdfinder

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import  org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.equalTo

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class VerificationTest {
    @Test
    fun testRobolectricSetupWorks(){
        assertThat(1 , equalTo(1))
    }
}