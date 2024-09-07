package com.calisthenics.routine.view.Workout

sealed class Screens(val screen: String) {
    /**
     * WORKOUT_HOME: SHow 2 buttons
     *  1. Workout plans
     *  2. Perform workout
     *
     *  WORKOUT_PLANS: Show all workout from Sunday to Saturday.
     *  Provide workout edit button where user can link routines to it.
     *
     *  EDIT_WORKOUT: User can link routines to the plan
     *
     *  PERFORM_WORKOUT: User will see all the workout which need to be done today. They will click on start exercise which to navigate to WORKOUT_EXERCISE
     *
     *  WORKOUT_ROUTINE:
     *      Buttons available:
     *          1. Log sets
     *          2. Next exercise
     *          3. Finish Exercise
     *  FINISH_EXERCISE: Here we will log the exercise into the Log table.
     */
    data object WORKOUT_HOME: Screens("Workout Home")
    data object WORKOUT_PLANS: Screens("Workout Plans")
    data object EDIT_WORKOUT: Screens("Edit Workout")
    data object START_NEW_WORKOUT: Screens("Perform Workout")
    data object WORKOUT_ROUTINE: Screens("Workout Routine")
    data object FINISH_WORKOUT: Screens("Finish Workout")
}