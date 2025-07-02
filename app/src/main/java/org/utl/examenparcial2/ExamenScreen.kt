package org.utl.examenparcial2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


val preguntas = listOf(
    Preguntas(
        "¿Cuál es la suma de 2 + 2?",
        listOf("a) 8", "b) 6", "c) 4", "d) 3"),
        2
    ),
    Preguntas(
        "¿Cuál es la resta de 10 - 3?",
        listOf("a) 8", "b) 7", "c) 5", "d) 9"),
        1
    ),
    Preguntas(
        "¿Cuál es la multiplicación de 3 x 3?",
        listOf("a) 7", "b) 1", "c) 9", "d) 4"),
        2
    ),
    Preguntas(
        "¿Cuál es la división de 20 ÷ 5?",
        listOf("a) 2", "b) 3", "c) 5", "d) 4"),
        3
    ),
    Preguntas(
        "¿Cuántos días tiene una semana?",
        listOf("a) 5", "b) 6", "c) 7", "d) 8"),
        2
    ),
    Preguntas(
        "¿Cual es la raiz cuadrada de 4?",
        listOf("a) 2", "b) 4", "c) 3", "d) 8"),
        0
    )
)

@Composable
fun ExamenScreen(navController: NavController) {
    val respuestasUsuario = remember { mutableStateListOf<Int?>().apply { repeat(preguntas.size) { add(null) } } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Examen",
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
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = respuestasUsuario[index] == i,
                                        onClick = { respuestasUsuario[index] = i }
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
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set("respuestasUsuario", ArrayList(respuestasUsuario))
                navController.navigate("resultados")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        ) {
            Text("Ver Resultados")
        }


    }
}
