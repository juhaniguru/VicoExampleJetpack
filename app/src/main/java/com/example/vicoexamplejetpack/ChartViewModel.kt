package com.example.vicoexamplejetpack

import androidx.lifecycle.ViewModel
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChartViewModel : ViewModel() {

    private val _chartData =
        MutableStateFlow(mapOf("A" to 8f, "B" to 4f, "C" to 6f, "D" to 1f, "E" to 2f))
    val chartData = _chartData.asStateFlow()

    private val _labelState = MutableStateFlow(ExtraStore.Key<List<String>>())
    val labelState = _labelState.asStateFlow()

    fun addValue() {
        _chartData.update {
            mapOf("A" to 8f, "B" to 4f, "C" to 6f, "D" to 1f, "E" to 2f, "F" to 3f) // Create a new map from scratch
        }
    }
}