package com.athkar.sa.ui.homeScreen

import com.athkar.sa.models.Athkar
import com.athkar.sa.models.getAthkar

sealed class UiHomeScreens {
    object AthkarContainer : UiHomeScreens()
    class AthkarsUI(val athkar: Athkar) : UiHomeScreens()
}

fun bindScreens(): List<UiHomeScreens> {
    val mutableList = mutableListOf<UiHomeScreens>(UiHomeScreens.AthkarContainer).apply {
        addAll(
            getAthkar().map { UiHomeScreens.AthkarsUI(it) })
    }
    return mutableList.toList()
}