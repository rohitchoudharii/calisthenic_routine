package com.calisthenics.routine.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.calisthenics.routine.config.BulkUploadDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream

class BulkDumpViewModel(
    private val exerciseViewModel: ExerciseViewModel,
    private val routineViewModel: RoutineViewModel,
    private val workflowViewModel: WorkoutViewModel
) : ViewModel() {

    fun dumpEntitiesFromFile(inputStream: InputStream?){
        val bulkUploadDto = LoadJsonData(inputStream)

        if(bulkUploadDto.exercises.isNotEmpty()){
            if(bulkUploadDto.reset){
                exerciseViewModel.getAllExercises().forEach { exerciseViewModel.delete(it) }
            }
            for(exercise in bulkUploadDto.exercises){
                if(exercise.id != exerciseViewModel.getExercise(exercise.id).id){
                    Log.d("LoadedExercise", "Loaded Exercise: $exercise")
                    exerciseViewModel.saveExercise(exercise)
                }else{
                    Log.d("LoadedExercise", "Already Exist Exercise: $exercise")
                }
            }
        }
    }

    fun LoadJsonData(inputStream: InputStream?): BulkUploadDto {
        var bulkUploadDtos = BulkUploadDto()
        if(inputStream != null){
            val i = try {
                bulkUploadDtos = ObjectMapper().readValue(inputStream)

                Log.d("BulkDumpViewModel", "Loaded BulkUpdateDto: $bulkUploadDtos")
            } catch (e: Exception) {
                Log.e("LoadJsonData", "Error reading JSON file", e)
            }
        }
        return bulkUploadDtos
    }
}