package com.calisthenics.routine.view.Logs

sealed class Screens(val screen: String) {
    data object LOG_HOME: Screens("Log home")
    data object SHOW_LOG: Screens("Show log")
}