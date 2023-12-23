import java.io.File
import Direction.RIGHT
import Direction.LEFT
import Direction.UP
import Direction.DOWN
import kotlin.math.absoluteValue

/*
--- Day 16: The Floor Will Be Lava ---
With the beam of light completely focused somewhere, the reindeer leads you deeper still into the Lava Production Facility. At some point, you realize that the steel facility walls have been replaced with cave, and the doorways are just cave, and the floor is cave, and you're pretty sure this is actually just a giant cave.
Finally, as you approach what must be the heart of the mountain, you see a bright light in a cavern up ahead. There, you discover that the beam of light you so carefully focused is emerging from the cavern wall closest to the facility and pouring all of its energy into a contraption on the opposite side.
Upon closer inspection, the contraption appears to be a flat, two-dimensional square grid containing empty space (.), mirrors (/ and \), and splitters (| and -).
The contraption is aligned so that most of the beam bounces around the grid, but each tile on the grid converts some of the beam's light into heat to melt the rock in the cavern.

You note the layout of the contraption (your puzzle input). For example:
.|...\....
|.-.\.....
.....|-...
........|.
..........
.........\
..../.\\..
.-.-/..|..
.|....-|.\
..//.|....
The beam enters in the top-left corner from the left and heading to the right. Then, its behavior depends on what it encounters as it moves:

If the beam encounters empty space (.), it continues in the same direction.
If the beam encounters a mirror (/ or \), the beam is reflected 90 degrees depending on the angle of the mirror. For instance, a rightward-moving beam that encounters a / mirror would continue upward in the mirror's column, while a rightward-moving beam that encounters a \ mirror would continue downward from the mirror's column.
If the beam encounters the pointy end of a splitter (| or -), the beam passes through the splitter as if the splitter were empty space. For instance, a rightward-moving beam that encounters a - splitter would continue in the same direction.
If the beam encounters the flat side of a splitter (| or -), the beam is split into two beams going in each of the two directions the splitter's pointy ends are pointing. For instance, a rightward-moving beam that encounters a | splitter would split into two beams: one that continues upward from the splitter's column and one that continues downward from the splitter's column.
Beams do not interact with other beams; a tile can have many beams passing through it at the same time. A tile is energized if that tile has at least one beam pass through it, reflect in it, or split in it.

In the above example, here is how the beam of light bounces around the contraption:
>|<<<\....
|v-.\^....
.v...|->>>
.v...v^.|.
.v...v^...
.v...v^..\
.v../2\\..
<->-/vv|..
.|<<<2-|.\
.v//.|.v..
Beams are only shown on empty tiles; arrows indicate the direction of the beams.
If a tile contains beams moving in multiple directions, the number of distinct directions is shown instead.

Here is the same diagram but instead only showing whether a tile is energized (#) or not (.):
######....
.#...#....
.#...#####
.#...##...
.#...##...
.#...##...
.#..####..
########..
.#######..
.#...#.#..
Ultimately, in this example, 46 tiles become energized.

The light isn't energizing enough tiles to produce lava; to debug the contraption, you need to start by analyzing the current situation. With the beam starting in the top-left heading right, how many tiles end up being energized?

--- Part Two ---
As you try to work out what might be wrong, the reindeer tugs on your shirt and leads you to a nearby control panel. There, a collection of buttons lets you align the contraption so that the beam enters from any edge tile and heading away from that edge. (You can choose either of two directions for the beam if it starts on a corner; for instance, if the beam starts in the bottom-right corner, it can start heading either left or upward.)
So, the beam could start on any tile in the top row (heading downward), any tile in the bottom row (heading upward), any tile in the leftmost column (heading right), or any tile in the rightmost column (heading left). To produce lava, you need to find the configuration that energizes as many tiles as possible.

In the above example, this can be achieved by starting the beam in the fourth tile from the left in the top row:
.|<2<\....
|v-v\^....
.v.v.|->>>
.v.v.v^.|.
.v.v.v^...
.v.v.v^..\
.v.v/2\\..
<-2-/vv|..
.|<<<2-|.\
.v//.|.v..

Using this configuration, 51 tiles are energized:
.#####....
.#.#.#....
.#.#.#####
.#.#.##...
.#.#.##...
.#.#.##...
.#.#####..
########..
.#######..
.#...#.#..

Find the initial beam configuration that energizes the largest number of tiles; how many tiles are energized in that configuration?
*/

enum class Direction(val move: Point) {
    UP(move = Point(0, -1)),
    DOWN(move = Point(0, 1)),
    LEFT(move = Point(-1, 0)),
    RIGHT(move = Point(1, 0));
}

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

private typealias DirectedPoint = Pair<Point, Direction>

class Day16(val input: List<String>) {

    private val maxX = input.first().length
    private val maxY = input.size

    fun solution1(): Int {
        return getBeamVisits(Point(0, 0) to RIGHT)
    }

    fun solution2(): Int {
        return listOf(
            (0..<maxX).maxOf { x -> getBeamVisits(Point(x, 0) to DOWN) },
            (0..<maxX).maxOf { x -> getBeamVisits(Point(x, maxY - 1) to UP) },
            (0..<maxY).maxOf { y -> getBeamVisits(Point(0, y) to LEFT) },
            (0..<maxY).maxOf { y -> getBeamVisits(Point(maxX - 1, y) to RIGHT) },
        ).max()
    }

    private fun getBeamVisits(start: DirectedPoint): Int {
        val queue = ArrayDeque<DirectedPoint>().apply { add(start) }
        val visited = hashSetOf<DirectedPoint>()

        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            if (!node.valid || node in visited)
                continue

            when (node.state) {
                '|' to RIGHT, '|' to LEFT -> listOf(UP, DOWN)
                '-' to UP, '-' to DOWN -> listOf(LEFT, RIGHT)
                '/' to RIGHT -> listOf(UP)
                '/' to LEFT -> listOf(DOWN)
                '/' to UP -> listOf(RIGHT)
                '/' to DOWN -> listOf(LEFT)
                '\\' to RIGHT -> listOf(DOWN)
                '\\' to LEFT -> listOf(UP)
                '\\' to UP -> listOf(LEFT)
                '\\' to DOWN -> listOf(RIGHT)
                else -> listOf(node.second)
            }.forEach { dir ->
                queue.add(node.plus(dir))
            }.also {
                visited.add(node)
            }

        }
        return visited.distinctBy { it.first }.size
    }

    private val DirectedPoint.value get() = input[first.y][first.x]
    private val DirectedPoint.state get() = value to second
    private val DirectedPoint.valid get() = first.x in 0..<maxX && first.y in 0..<maxY
    private operator fun DirectedPoint.plus(direction: Direction): DirectedPoint = (first + direction) to direction
}


fun main() {
    val fileInput = File("src/main/kotlin/inputs/Day16.txt").readLines()
    println(Day16(fileInput).solution1())
    println(Day16(fileInput).solution2())
}