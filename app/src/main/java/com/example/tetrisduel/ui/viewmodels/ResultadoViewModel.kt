package com.example.tetrisduel.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tetrisduel.repositories.JuegoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultadoViewModel @Inject constructor(
    private val repositorio: JuegoRepository
) : ViewModel() {

    fun desconectar() {
        repositorio.desconectar()
    }
}
