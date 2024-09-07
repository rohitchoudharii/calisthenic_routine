package com.calisthenics.routine.ViewModels

import androidx.lifecycle.ViewModel
import com.calisthenics.routine.models.Logs
import com.calisthenics.routine.models.Routine
import io.objectbox.Box

class LogsViewModel(private val routineBox: Box<Logs>): ViewModel() {
}