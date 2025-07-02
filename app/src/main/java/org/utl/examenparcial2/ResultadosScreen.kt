package org.utl.examenparcial2

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ResultadosScreen(navController: NavController) {
    val respuestasUsuario = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<List<Int?>>("respuestasUsuario") ?: emptyList()

    val respuestasCorrectas = preguntas.zip(respuestasUsuario).count { (pregunta, respuestaUsuario) ->
        pregunta.respuestaCorrecta == respuestaUsuario
    }

    val calificacion = (respuestasCorrectas * 10) / preguntas.size

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Resultados",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(preguntas) { index, pregunta ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(pregunta.enunciado, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            pregunta.opciones.forEachIndexed { i, opcion ->
                                val esCorrecta = i == pregunta.respuestaCorrecta
                                val respuestaUsuario = respuestasUsuario.getOrNull(index)
                                val esSeleccionado = i == respuestaUsuario

                                val backgroundColor = when {
                                    esSeleccionado && esCorrecta -> Color(0xFFB9FBC0)
                                    esSeleccionado && !esCorrecta -> Color(0xFFFFADAD)
                                    else -> Color.Transparent
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(backgroundColor, RoundedCornerShape(4.dp))
                                ) {
                                    RadioButton(
                                        selected = esSeleccionado,
                                        onClick = {},
                                        enabled = false
                                    )
                                    Text(opcion, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    guardarCalificacion(context, calificacion)
                }
                navController.navigate("resumen")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        ) {
            Text("Ver información")
        }
    }
}

fun guardarCalificacion(context: Context, calificacion: Int) {
    val file = File(context.filesDir, "calificacion.txt")
    file.writeText(calificacion.toString())
}


