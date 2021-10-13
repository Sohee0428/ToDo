package com.example.aoppart5chapter01.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

internal abstract class BaseViewModel : ViewModel() {

    abstract fun fetchData(): Job

}