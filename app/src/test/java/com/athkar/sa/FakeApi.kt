package com.athkar.sa

import javax.inject.Inject

class FakeApi @Inject constructor() {
    private val fakeData = mutableListOf<String>()

}