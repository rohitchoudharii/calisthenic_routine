package com.calisthenics.routine.view.Home


sealed class Screens(val screen: String) {
    data object HOME: Screens("Home")
    data object EXERCISE: Screens("Exercise")
    data object ROUTINE: Screens("Routine")
    data object WORKOUT: Screens("Workout")
    data object LOGS: Screens("Logs")
}