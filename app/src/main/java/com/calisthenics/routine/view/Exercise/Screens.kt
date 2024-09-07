package com.calisthenics.routine.view.Exercise

sealed class Screens(val screen: String) {
    data object EXERCISE_HOME: Screens("Exercise Home")
    data object SHOW_EXERCISE: Screens("Show Exercise")
    data object EDIT_EXERCISE: Screens("Edit exercise")
    data object CREATE_EXERCISE: Screens("Create exercise")
}