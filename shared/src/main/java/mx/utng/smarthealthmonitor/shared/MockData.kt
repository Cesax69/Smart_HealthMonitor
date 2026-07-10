package mx.utng.smarthealthmonitor.shared

data class RegistroSalud(
    val id: Int,
    val bpm: Int,
    val fecha: String,
    val esNormal: Boolean
)

object MockData {
    const val fcActual = 72
    const val pasosActual = 4250
    const val spo2Actual = 98

    val historialFC = listOf(
        RegistroSalud(1, 72, "Hoy, 08:00", true),
        RegistroSalud(2, 115, "Hoy, 10:30", false),
        RegistroSalud(3, 68, "Ayer, 22:15", true),
        RegistroSalud(4, 75, "Ayer, 14:00", true),
        RegistroSalud(5, 125, "25 May, 09:00", false),
        RegistroSalud(6, 80, "24 May, 18:30", true),
        RegistroSalud(7, 70, "23 May, 07:00", true),
        RegistroSalud(8, 110, "22 May, 21:00", false),
        RegistroSalud(9, 65, "21 May, 06:00", true),
        RegistroSalud(10, 78, "20 May, 15:45", true)
    )
}
