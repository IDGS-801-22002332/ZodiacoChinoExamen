package org.utl.examenparcial2

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var apaterno by remember { mutableStateOf("") }
    var amaterno by remember { mutableStateOf("") }
    var dia by remember { mutableStateOf("") }
    var mes by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Datos Personales", fontSize = 20.sp)

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = apaterno, onValueChange = { apaterno = it }, label = { Text("Apaterno") })
        OutlinedTextField(value = amaterno, onValueChange = { amaterno = it }, label = { Text("Amaterno") })

        Spacer(modifier = Modifier.height(16.dp))
        Text("Fecha de nacimiento")
        Row {
            OutlinedTextField(
                value = dia,
                onValueChange = { dia = it },
                label = { Text("Día") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            )
            OutlinedTextField(
                value = mes,
                onValueChange = { mes = it },
                label = { Text("Mes") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
            )
            OutlinedTextField(
                value = anio,
                onValueChange = { anio = it },
                label = { Text("Año") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Sexo")
        Row {
            RadioButton(
                selected = sexo == "Masculino",
                onClick = { sexo = "Masculino" }
            )
            Text("Masculino", modifier = Modifier.padding(end = 8.dp))
            RadioButton(
                selected = sexo == "Femenino",
                onClick = { sexo = "Femenino" }
            )
            Text("Femenino")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                nombre = ""
                apaterno = ""
                amaterno = ""
                dia = ""
                mes = ""
                anio = ""
                sexo = ""
            }) {
                Text("Limpiar")
            }

            Button(onClick = {
                val datos = """
                    Nombre: $nombre
                    Apaterno: $apaterno
                    Amaterno: $amaterno
                    Fecha de nacimiento: $dia/$mes/$anio
                    Sexo: $sexo
                """.trimIndent()

                CoroutineScope(Dispatchers.IO).launch {
                    guardarEnArchivo(context, datos)
                }

                navController.navigate("examen")
            }) {
                Text("Siguiente")
            }
        }
    }
}

fun guardarEnArchivo(context: Context, contenido: String) {
    val file = File(context.filesDir, "informacion.txt")
    file.writeText(contenido)
}