import kotlin.math.absoluteValue

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = copy(x = x + other.x, y = y + other.y)
    operator fun plus(other: Direction) = plus(other.move)
    fun distanceTo(other: Point) = (x - other.x).absoluteValue + (y - other.y).absoluteValue

    fun orthogonalNeighbours() = setOf(
        copy(x = x - 1),
        copy(x = x + 1),
        copy(y = y - 1),
        copy(y = y + 1),
    )

    fun neighbours() = setOf(
        copy(y = y - 1),
        copy(x = x + 1, y = y - 1),
        copy(x = x + 1),
        copy(x = x + 1, y = y + 1),
        copy(y = y + 1),
        copy(x = x - 1, y = y + 1),
        copy(x = x - 1),
        copy(x = x - 1, y = y - 1),
    )
}