package com.example.tetrisduel.game

enum class TipoPieza(val color: Int) {
    I(1),
    O(2),
    T(3),
    S(4),
    Z(5),
    J(6),
    L(7);

    val rotaciones: List<List<Pair<Int, Int>>>
        get() = formasPorTipo.getValue(this)

    companion object {
        fun aleatoria(): TipoPieza = entries.random()
    }
}

data class Pieza(
    val tipo: TipoPieza,
    val fila: Int,
    val columna: Int,
    val rotacion: Int
) {
    val celdas: List<Pair<Int, Int>>
        get() {
            val forma = tipo.rotaciones[rotacion % tipo.rotaciones.size]
            return forma.map { (df, dc) -> Pair(fila + df, columna + dc) }
        }

    fun movida(filas: Int, columnas: Int): Pieza =
        copy(fila = fila + filas, columna = columna + columnas)

    fun rotada(): Pieza =
        copy(rotacion = (rotacion + 1) % tipo.rotaciones.size)
}

private val formasPorTipo: Map<TipoPieza, List<List<Pair<Int, Int>>>> = mapOf(
    TipoPieza.I to listOf(
        listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
        listOf(0 to 1, 1 to 1, 2 to 1, 3 to 1)
    ),
    TipoPieza.O to listOf(
        listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1)
    ),
    TipoPieza.T to listOf(
        listOf(0 to 1, 1 to 0, 1 to 1, 1 to 2),
        listOf(0 to 1, 1 to 1, 1 to 2, 2 to 1),
        listOf(1 to 0, 1 to 1, 1 to 2, 2 to 1),
        listOf(0 to 1, 1 to 0, 1 to 1, 2 to 1)
    ),
    TipoPieza.S to listOf(
        listOf(0 to 1, 0 to 2, 1 to 0, 1 to 1),
        listOf(0 to 1, 1 to 1, 1 to 2, 2 to 2)
    ),
    TipoPieza.Z to listOf(
        listOf(0 to 0, 0 to 1, 1 to 1, 1 to 2),
        listOf(0 to 2, 1 to 1, 1 to 2, 2 to 1)
    ),
    TipoPieza.J to listOf(
        listOf(0 to 0, 1 to 0, 1 to 1, 1 to 2),
        listOf(0 to 1, 0 to 2, 1 to 1, 2 to 1),
        listOf(1 to 0, 1 to 1, 1 to 2, 2 to 2),
        listOf(0 to 1, 1 to 1, 2 to 0, 2 to 1)
    ),
    TipoPieza.L to listOf(
        listOf(0 to 2, 1 to 0, 1 to 1, 1 to 2),
        listOf(0 to 1, 1 to 1, 2 to 1, 2 to 2),
        listOf(1 to 0, 1 to 1, 1 to 2, 2 to 0),
        listOf(0 to 0, 0 to 1, 1 to 1, 2 to 1)
    )
)
