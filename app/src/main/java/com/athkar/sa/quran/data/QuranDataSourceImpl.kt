package com.athkar.sa.quran.data

import javax.inject.Inject

class QuranDataSourceImpl @Inject constructor():QuranDataSource {
    override val currentQuranVersion: Int
        get() = 1
}