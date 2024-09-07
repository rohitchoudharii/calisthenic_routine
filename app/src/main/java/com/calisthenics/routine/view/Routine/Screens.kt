package com.calisthenics.routine.view.Routine

sealed class Screens(val screen: String) {
    data object ROUTINE_HOME: Screens("Routine home")
    data object CREATE_ROUTINE: Screens("Create Routine")
    data object EDIT_ROUTINE: Screens("Edit Routine")
    data object ROUTINE_DETAILS: Screens("Routine Details")
}