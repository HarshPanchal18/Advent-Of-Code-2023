import java.io.File

/*
--- Day 23: A Long Walk ---
The Elves resume water filtering operations! Clean water starts flowing over the edge of Island Island.
They offer to help you go over the edge of Island Island, too! Just hold on tight to one end of this impossibly long rope and they'll lower you down a safe distance from the massive waterfall you just created.
As you finally reach Snow Island, you see that the water isn't really reaching the ground: it's being absorbed by the air itself. It looks like you'll finally have a little downtime while the moisture builds up to snow-producing levels. Snow Island is pretty scenic, even without any snow; why not take a walk?
There's a map of nearby hiking trails (your puzzle input) that indicates paths (.), forest (#), and steep slopes (^, >, v, and <).

For example:
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
You're currently on the single path tile in the top row; your goal is to reach the single path tile in the bottom row. Because of all the mist from the waterfall, the slopes are probably quite icy; if you step onto a slope tile, your next step must be downhill (in the direction the arrow is pointing). To make sure you have the most scenic hike possible, never step onto the same tile twice. What is the longest hike you can take?

In the example above, the longest hike you can take is marked with O, and your starting position is marked S:
#S#####################
#OOOOOOO#########...###
#######O#########.#.###
###OOOOO#OOO>.###.#.###
###O#####O#O#.###.#.###
###OOOOO#O#O#.....#...#
###v###O#O#O#########.#
###...#O#O#OOOOOOO#...#
#####.#O#O#######O#.###
#.....#O#O#OOOOOOO#...#
#.#####O#O#O#########v#
#.#...#OOO#OOO###OOOOO#
#.#.#v#######O###O###O#
#...#.>.#...>OOO#O###O#
#####v#.#.###v#O#O###O#
#.....#...#...#O#O#OOO#
#.#########.###O#O#O###
#...###...#...#OOO#O###
###.###.#.###v#####O###
#...#...#.#.>.>.#.>O###
#.###.###.#.###.#.#O###
#.....###...###...#OOO#
#####################O#
This hike contains 94 steps. (The other possible hikes you could have taken were 90, 86, 82, 82, and 74 steps long.)
Find the longest hike you can take through the hiking trails listed on your map. How many steps long is the longest hike?
*/

class Day23(val input: List<String>) {
    private val start = Point2D(1, 0)
    private val end = Point2D(input.first().lastIndex - 1, input.lastIndex)
    private val visited = Array(input.size) { BooleanArray(input.first().length) }

    fun solution1(): Int {
        return findMax(
            current = start,
            visited = visited,
            getNeighbours = { current ->
                when (input[current.y][current.x]) {
                    '>' -> listOf(current.copy(x = current.x + 1) to 1)
                    '<' -> listOf(current.copy(x = current.x - 1) to 1)
                    '^' -> listOf(current.copy(y = current.y - 1) to 1)
                    'v' -> listOf(current.copy(y = current.y + 1) to 1)
                    else -> current.validNeighbours().map { it to 1 }
                }
            }
        )
    }

    private fun findMax(
        current: Point2D, visited: Array<BooleanArray>, distance: Int = 0,
        getNeighbours: (Point2D) -> List<Pair<Point2D, Int>>
    ): Int {

        if (current == end) return distance

        visited[current.y][current.x] = true
        val max = getNeighbours(current)
            .filter { (neighbour, _) -> !visited[neighbour.y][neighbour.x] }
            .maxOfOrNull { (neighbour, weight) ->
                findMax(neighbour, visited, distance + weight, getNeighbours)
            }

        visited[current.y][current.x] = false

        return max ?: 0
    }

    private fun Point2D.validNeighbours(): List<Point2D> = neighbours()
        .filter { it.y in input.indices && it.x in input.first().indices && input[it.y][it.x] in ".<>^v" }
}


fun main() {
    val fileInput = File("src/main/kotlin/inputs/Day23.txt").readLines()
    println(Day23(fileInput).solution1())
}