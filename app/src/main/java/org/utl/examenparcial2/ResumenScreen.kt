package org.utl.examenparcial2

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import java.util.Calendar

@Composable
fun ResumenScreen() {
    val context = LocalContext.current

    var nombreCompleto by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf(0) }
    var signoZodiacal by remember { mutableStateOf("") }
    var calificacion by remember { mutableStateOf(0) }
    var imagenSigno by remember { mutableStateOf(R.drawable.deault) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val datosPersonales = leerInformacionArchivo(context)
            val calif = leerCalificacionArchivo(context)
            val nombre = datosPersonales["Nombre"] ?: ""
            val apaterno = datosPersonales["Apaterno"] ?: ""
            val amaterno = datosPersonales["Amaterno"] ?: ""
            val fechaNacimiento = datosPersonales["Fecha"] ?: ""
            val anioNacimiento = fechaNacimiento.split("/").getOrNull(2)?.toIntOrNull() ?: 2000

            val edadCalculada = calcularEdad(anioNacimiento)
            val signo = obtenerSignoZodiacalChino(anioNacimiento)
            val imagen = obtenerImagenDelSigno(signo)

            withContext(Dispatchers.Main) {
                nombreCompleto = "$nombre $apaterno $amaterno"
                edad = edadCalculada
                signoZodiacal = signo
                calificacion = calif
                imagenSigno = imagen
            }
        }
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "Hola $nombreCompleto", fontSize = 22.sp)
            Text(text = "Tienes $edad años y tu signo zodiacal es $signoZodiacal", fontSize = 18.sp)
            Image(
                painter = painterResource(id = imagenSigno),
                contentDescription = "Signo Zodiacal Chino",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Calificación: $calificacion / 10",
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Button(
                onClick = {
                    borrarArchivos(context)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Terminar")
            }
        }
    }
}

suspend fun leerInformacionArchivo(context: Context): Map<String, String> {
    val file = File(context.filesDir, "informacion.txt")
    if (!file.exists()) return emptyMap()

    val contenido = file.readText()
    val lines = contenido.lines()
    val datos = mutableMapOf<String, String>()

    lines.forEach { line ->
        when {
            line.startsWith("Nombre:") -> datos["Nombre"] = line.removePrefix("Nombre:").trim()
            line.startsWith("Apaterno:") -> datos["Apaterno"] = line.removePrefix("Apaterno:").trim()
            line.startsWith("Amaterno:") -> datos["Amaterno"] = line.removePrefix("Amaterno:").trim()
            line.startsWith("Fecha de nacimiento:") -> datos["Fecha"] = line.removePrefix("Fecha de nacimiento:").trim()
            line.startsWith("Sexo:") -> datos["Sexo"] = line.removePrefix("Sexo:").trim()
        }
    }

    return datos
}

suspend fun leerCalificacionArchivo(context: Context): Int {
    val file = File(context.filesDir, "calificacion.txt")
    if (!file.exists()) return 0
    return file.readText().trim().toIntOrNull() ?: 0
}

fun calcularEdad(anioNacimiento: Int): Int {
    val anioActual = Calendar.getInstance().get(Calendar.YEAR)
    return anioActual - anioNacimiento
}

fun obtenerSignoZodiacalChino(anioNacimiento: Int): String {
    val signos = listOf(
        "Rata", "Buey", "Tigre", "Conejo", "Dragón", "Serpiente",
        "Caballo", "Cabra", "Mono", "Gallo", "Perro", "Cerdo"
    )
    val index = (anioNacimiento - 1900) % 12
    return signos[index]
}


fun obtenerImagenDelSigno(signo: String): Int {
    return when (signo) {
        "Rata" -> R.drawable.rata
        "Buey" -> R.drawable.buey
        "Tigre" -> R.drawable.tigre
        "Conejo" -> R.drawable.conejo
        "Dragón" -> R.drawable.dragon
        "Serpiente" -> R.drawable.serpiente
        "Caballo" -> R.drawable.caballo
        "Cabra" -> R.drawable.cabra
        "Mono" -> R.drawable.mono
        "Gallo" -> R.drawable.gallo
        "Perro" -> R.drawable.perro
        "Cerdo" -> R.drawable.cerdo
        else -> R.drawable.deault
    }
}

fun borrarArchivos(context: Context) {
    val infoFile = File(context.filesDir, "informacion.txt")
    val califFile = File(context.filesDir, "calificacion.txt")

    if (infoFile.exists()) infoFile.writeText("")
    if (califFile.exists()) califFile.writeText("")
}
