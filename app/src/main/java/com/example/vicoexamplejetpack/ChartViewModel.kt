package com.example.vicoexamplejetpack

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChartViewModel: ViewModel() {

    private val _chartData = MutableStateFlow(mapOf("A" to 8f, "B" to 4f, "C" to 6f))
    val chartData = _chartData.asStateFlow()
}