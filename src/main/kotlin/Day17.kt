import java.io.File
import java.util.PriorityQueue
import Direction.*

/*
--- Day 17: Clumsy Crucible ---
The lava starts flowing rapidly once the Lava Production Facility is operational. As you leave, the reindeer offers you a parachute, allowing you to quickly reach Gear Island.

As you descend, your bird's-eye view of Gear Island reveals why you had trouble finding anyone on your way up: half of Gear Island is empty, but the half below you is a giant factory city!

You land near the gradually-filling pool of lava at the base of your new lavafall. Lavaducts will eventually carry the lava throughout the city, but to make use of it immediately, Elves are loading it into large crucibles on wheels.

The crucibles are top-heavy and pushed by hand. Unfortunately, the crucibles become very difficult to steer at high speeds, and so it can be hard to go in a straight line for very long.

To get Desert Island the machine parts it needs as soon as possible, you'll need to find the best way to get the crucible from the lava pool to the machine parts factory. To do this, you need to minimize heat loss while choosing a route that doesn't require the crucible to go in a straight line for too long.

Fortunately, the Elves here have a map (your puzzle input) that uses traffic patterns, ambient temperature, and hundreds of other parameters to calculate exactly how much heat loss can be expected for a crucible entering any particular city block.

For example:


Explain
2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
Each city block is marked by a single digit that represents the amount of heat loss if the crucible enters that block. The starting point, the lava pool, is the top-left city block; the destination, the machine parts factory, is the bottom-right city block. (Because you already start in the top-left block, you don't incur that block's heat loss unless you leave that block and then return to it.)

Because it is difficult to keep the top-heavy crucible going in a straight line for very long, it can move at most three blocks in a single direction before it must turn 90 degrees left or right. The crucible also can't reverse direction; after entering each city block, it may only turn left, continue straight, or turn right.

One way to minimize heat loss is this path:

2>>34^>>>1323
32v>>>35v5623
32552456v>>54
3446585845v52
4546657867v>6
14385987984v4
44578769877v6
36378779796v>
465496798688v
456467998645v
12246868655<v
25465488877v5
43226746555v>
This path never moves more than three consecutive blocks in the same direction and incurs a heat loss of only 102.

Directing the crucible from the lava pool to the machine parts factory, but not moving more than three consecutive blocks in the same direction, what is the least heat loss it can incur?
*/

class Day17(val input: List<String>) {
    private val maxX = input.size
    private val maxY = input.first().length
    private val finish = Point(maxX - 1, maxY - 1)

    fun solution1() = solve(minRepeat = 1, maxRepeat = 3)

    private fun solve(minRepeat: Int, maxRepeat: Int): Int {
        val queue = PriorityQueue<Block>(compareBy { it.distance }).apply {
            add(Block(Point(1, 0), RIGHT, 1, 0))
            add(Block(Point(0, 1), DOWN, 1, 0))
        }
        val visits = hashMapOf<Triple<Point, Direction, Int>, Int>()

        while (queue.isNotEmpty()) {
            val (node, direction, repeat, distance) = queue.poll()
            val newDistance = distance + node.weight

            if (node == finish && repeat >= minRepeat)
                return newDistance

            val visit = Triple(node, direction, repeat)
            if (newDistance >= visits.getOrDefault(visit, Int.MAX_VALUE))
                continue

            visits[visit] = newDistance

            if (repeat >= minRepeat) {
                val left = direction.toLeft()
                val right = direction.toRight()

                (node + left).takeIf { it.valid }?.let { queue.add(Block(it, left, 1, newDistance)) }
                (node + right).takeIf { it.valid }?.let { queue.add(Block(it, right, 1, newDistance)) }
            }


            if (repeat < maxRepeat)
                (node + direction).takeIf { it.valid }?.let { queue.add(Block(it, direction, repeat + 1, newDistance)) }
        }

        return -1
    }

    private val Point.weight get() = input[y][x].digitToInt()
    private val Point.valid get() = x in 0..<maxX && y in 0..<maxY

    private fun Direction.toRight() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

    private fun Direction.toLeft() = when (this) {
        UP -> LEFT
        LEFT -> DOWN
        DOWN -> RIGHT
        RIGHT -> UP
    }

    private data class Block(
        val point: Point,
        val direction: Direction,
        val repeat: Int,
        val distance: Int,
    )
}

fun main() {
    val fileInput = File("src/main/kotlin/inputs/Day17.txt").readLines()
    println(Day17(fileInput).solution1())
}