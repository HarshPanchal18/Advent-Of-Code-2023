/*
--- Day 2: Cube Conundrum ---

You're launched high into the atmosphere! The apex of your trajectory just barely reaches the surface of a large island floating in the sky. You gently land in a fluffy pile of leaves.
It's quite cold, but you don't see much snow. An Elf runs over to greet you.

The Elf explains that you've arrived at Snow Island and apologizes for the lack of snow.
He'll be happy to explain the situation, but it's a bit of a walk, so you have some time.
They don't get many visitors up here; would you like to play a game in the meantime?
As you walk, the Elf shows you a small bag and some cubes which are either red, green, or blue.
Each time you play this game, he will hide a secret number of cubes of each color in the bag, and your goal is to figure out information about the number of cubes.
To get information, once a bag has been loaded with cubes, the Elf will reach into the bag, grab a handful of random cubes, show them to you, and then put them back in the bag. He'll do this a few times per game.
You play several games and record the information from each game (your puzzle input).
Each game is listed with its ID number (like the 11 in Game 11: ...) followed by a semicolon-separated list of subsets of cubes that were revealed from the bag (like 3 red, 5 green, 4 blue).

For example, the record of a few games might look like this:
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
In game 1, three sets of cubes are revealed from the bag (and then put back again). The first set is 3 blue cubes and 4 red cubes; the second set is 1 red cube, 2 green cubes, and 6 blue cubes; the third set is only 2 green cubes.

The Elf would first like to know which games would have been possible if the bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?

In the example above, games 1, 2, and 5 would have been possible if the bag had been loaded with that configuration. However, game 3 would have been impossible because at one point the Elf showed you 20 red cubes at once; similarly, game 4 would also have been impossible because the Elf showed you 15 blue cubes at once. If you add up the IDs of the games that would have been possible, you get 8.

Determine which games would have been possible if the bag had been loaded with only 12 red cubes, 13 green cubes, and 14 blue cubes. What is the sum of the IDs of those games?

--- Part Two ---
The Elf says they've stopped producing snow because they aren't getting any water! He isn't sure why the water stopped; however, he can show you how to get to the water source to check it out for yourself. It's just up ahead!

As you continue your walk, the Elf poses a second question: in each game you played, what is the fewest number of cubes of each color that could have been in the bag to make the game possible?

Again consider the example games from earlier:

Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green

In game 1, the game could have been played with as few as 4 red, 2 green, and 6 blue cubes. If any color had even one fewer cube, the game would have been impossible.

Game 2 could have been played with a minimum of 1 red, 3 green, and 4 blue cubes.
Game 3 must have been played with at least 20 red, 13 green, and 6 blue cubes.
Game 4 required at least 14 red, 3 green, and 15 blue cubes.
Game 5 needed no fewer than 6 red, 3 green, and 2 blue cubes in the bag.

The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together. The power of the minimum set of cubes in game 1 is 48. In games 2-5 it was 12, 1560, 630, and 36, respectively. Adding up these five powers produces the sum 2286.

For each game, find the minimum set of cubes that must have been present. What is the sum of the power of these sets?

 */

class Day2(input: List<String>) {

    private val games: List<Game> = input.map { Game.of(it) }

    fun solution1(): Int {
        return games.filter { game ->
            game.isPossible(12, 13, 14)
        }.sumOf { it.id }
    }

    fun solution2(): Int {
        return games.sumOf { it.cube() }
    }

    private data class Game(
        val id: Int,
        val red: Int,
        val green: Int,
        val blue: Int
    ) {
        fun isPossible(red: Int, green: Int, blue: Int): Boolean {
            return this.red <= red && this.green <= green && this.blue <= blue
        }

        fun cube(): Int = red * green * blue

        companion object {
            fun of(input: String): Game {
                val id = input.substringAfter(" ").substringBefore(":").toInt()
                val colors = mutableMapOf<String, Int>()

                input.substringAfter(":").split(";").forEach { turn ->
                    turn.split(",").map { it.trim() }.forEach { draw ->
                        val drawNum = draw.substringBefore(" ").toInt()
                        val color = draw.substringAfter(" ")
                        colors[color] = maxOf(drawNum, colors[color] ?: drawNum)
                    }
                }

                return Game(id, colors["red"] ?: 0, colors["green"] ?: 0, colors["blue"] ?: 0)
            }
        }
    }
}

fun main() {
    val input = listOf(
        "Game 1: 4 red, 8 green; 8 green, 6 red; 13 red, 8 green; 2 blue, 4 red, 4 green",
        "Game 2: 5 blue; 1 red, 3 blue; 1 red, 7 blue, 1 green; 1 red, 8 blue; 7 blue, 1 red; 4 blue, 1 green, 1 red",
        "Game 3: 8 blue, 5 green, 15 red; 6 red, 6 blue, 3 green; 8 red, 2 green; 10 blue, 10 red, 6 green; 8 red, 6 blue; 15 red, 5 green, 2 blue",
        "Game 4: 10 green, 12 red, 14 blue; 5 green, 9 red, 7 blue; 14 blue, 12 red; 8 blue, 7 green, 11 red",
        "Game 5: 13 blue, 10 red, 7 green; 3 green, 8 red, 4 blue; 16 red, 5 green, 5 blue; 2 blue, 9 red, 7 green; 5 red, 14 blue, 3 green; 2 red, 11 blue, 2 green",
        "Game 6: 3 blue, 1 green; 10 green, 12 red, 6 blue; 3 green, 2 red, 5 blue; 2 blue, 11 green, 2 red; 1 red, 5 blue, 9 green",
        "Game 7: 2 blue, 10 green; 3 red, 10 blue; 3 green, 8 blue, 5 red; 8 green, 10 blue, 2 red",
        "Game 8: 15 red, 12 blue, 5 green; 10 red, 12 blue, 5 green; 10 red, 7 green",
        "Game 9: 18 blue, 14 red; 3 green, 9 blue; 1 blue, 11 red; 5 red, 7 blue, 3 green; 8 red, 4 green, 1 blue",
        "Game 10: 2 blue; 10 green, 4 blue, 3 red; 5 green, 4 red, 4 blue; 1 red, 3 blue, 4 green; 2 blue, 5 red, 3 green; 3 green, 2 red, 2 blue",
        "Game 11: 4 blue, 19 green; 19 blue, 12 green, 17 red; 11 red, 10 blue, 17 green; 9 green, 18 blue; 14 green, 9 red, 18 blue; 15 blue, 6 green, 19 red",
        "Game 12: 1 green, 6 blue, 2 red; 6 blue, 2 red, 8 green; 2 green, 2 red, 7 blue; 1 red, 3 blue, 6 green",
        "Game 13: 2 red, 11 blue, 4 green; 2 red, 7 blue; 9 green, 1 red, 12 blue; 13 blue, 8 green; 11 blue, 8 green, 1 red; 1 red, 2 blue",
        "Game 14: 1 green, 4 blue, 11 red; 11 green, 6 blue, 7 red; 7 green, 6 blue, 4 red; 12 blue, 10 red, 11 green",
        "Game 15: 1 green, 19 red, 3 blue; 11 red, 3 blue; 20 red, 4 blue",
        "Game 16: 3 red, 1 green, 7 blue; 3 blue, 4 red, 1 green; 6 blue, 7 red, 3 green",
        "Game 17: 7 blue, 4 red, 19 green; 7 green, 4 red; 8 green, 2 red, 4 blue",
        "Game 18: 1 red, 1 blue, 6 green; 2 red, 6 green, 1 blue; 4 green, 1 red, 1 blue",
        "Game 19: 4 blue, 8 green, 6 red; 2 red, 9 green, 4 blue; 9 green, 8 red, 6 blue; 3 red, 6 blue, 9 green; 8 red, 4 blue, 7 green",
        "Game 20: 3 blue, 7 green, 13 red; 13 blue; 12 red, 14 blue; 3 red, 6 green, 8 blue",
        "Game 21: 5 green, 2 red, 10 blue; 2 red, 2 green, 6 blue; 1 blue, 1 red, 7 green; 4 blue, 1 red, 2 green",
        "Game 22: 2 red; 1 green, 7 red; 3 red, 1 green, 1 blue; 4 red, 5 green, 3 blue; 1 blue, 2 green",
        "Game 23: 15 red, 1 blue, 3 green; 6 blue, 3 green, 2 red; 6 green, 4 red, 1 blue",
        "Game 24: 17 green; 1 red, 2 blue, 3 green; 10 blue, 1 green; 1 green; 1 red, 2 blue, 1 green",
        "Game 25: 2 green, 8 blue, 1 red; 1 blue, 1 red, 9 green; 1 blue, 2 green, 2 red; 3 red, 6 blue",
        "Game 26: 12 red, 19 green, 4 blue; 2 red, 10 blue, 15 green; 14 blue, 17 red, 3 green; 1 green, 15 red, 3 blue",
        "Game 27: 11 green, 1 red, 9 blue; 3 green, 10 blue; 9 green, 10 blue, 1 red; 4 green, 3 blue, 1 red; 2 blue, 5 green, 2 red; 17 blue, 2 red, 5 green",
        "Game 28: 10 green, 10 red, 5 blue; 5 red, 4 blue, 8 green; 3 green, 10 red, 3 blue; 2 blue, 8 green, 1 red; 6 red, 1 green, 4 blue",
        "Game 29: 3 blue, 11 red, 1 green; 5 blue, 3 green, 6 red; 8 red, 12 blue, 10 green; 1 blue, 4 red, 1 green",
        "Game 30: 10 blue, 1 red, 2 green; 1 red, 8 blue, 2 green; 4 blue, 3 green; 5 green, 1 red, 3 blue; 3 green, 14 blue",
        "Game 31: 3 red, 7 green, 6 blue; 11 red, 4 green, 2 blue; 1 green, 11 red, 8 blue; 6 green, 5 blue, 5 red; 4 green, 3 blue, 15 red",
        "Game 32: 9 green, 1 blue, 10 red; 13 red, 7 green; 12 red, 6 green, 1 blue",
        "Game 33: 9 green, 4 red, 6 blue; 2 red, 4 blue, 1 green; 2 blue, 11 red, 9 green",
        "Game 34: 8 green, 6 red; 4 blue, 3 green; 6 red, 1 blue, 9 green; 10 green, 1 red; 2 red, 2 blue, 2 green; 2 blue",
        "Game 35: 4 blue, 8 green, 8 red; 1 blue, 10 green; 5 green, 8 red; 4 green; 6 red, 1 blue, 6 green",
        "Game 36: 4 red, 10 blue, 16 green; 18 blue, 5 red, 5 green; 16 green, 11 blue, 1 red; 6 green, 10 blue; 4 red, 9 green, 17 blue; 1 red, 9 blue, 14 green",
        "Game 37: 1 red, 13 green, 5 blue; 2 red, 12 green, 12 blue; 5 red, 11 blue, 5 green; 9 green, 4 blue",
        "Game 38: 1 green, 12 blue, 1 red; 11 blue, 3 red, 1 green; 17 red, 11 blue; 8 red, 2 blue",
        "Game 39: 11 blue, 12 red, 1 green; 1 blue, 1 green, 4 red; 3 green, 6 blue, 3 red",
        "Game 40: 1 blue, 1 red; 9 green, 2 red, 2 blue; 9 green, 3 red; 8 green, 4 blue, 4 red; 3 green, 3 red",
        "Game 41: 7 blue, 8 red, 3 green; 4 red, 7 green, 1 blue; 5 blue, 6 red, 5 green; 4 blue, 9 red; 2 green, 9 blue, 5 red",
        "Game 42: 8 blue, 17 green, 7 red; 6 red, 11 green, 13 blue; 7 red, 3 blue, 14 green; 2 red, 12 blue, 2 green; 18 green, 8 red; 10 green, 5 blue",
        "Game 43: 5 green, 9 red, 3 blue; 3 red, 5 green; 6 green, 1 blue, 10 red; 8 blue, 1 green, 2 red",
        "Game 44: 1 red, 5 blue; 4 green, 6 red, 2 blue; 12 green, 8 red; 4 blue, 2 red, 9 green; 1 blue, 5 green, 3 red",
        "Game 45: 9 blue, 5 red, 6 green; 10 blue, 7 green, 8 red; 1 red, 1 green, 10 blue; 2 red, 1 green, 11 blue; 11 red",
        "Game 46: 14 blue, 8 green, 2 red; 10 green, 8 blue; 7 blue, 12 green; 14 green, 10 blue, 2 red",
        "Game 47: 5 blue, 7 green, 1 red; 5 blue, 5 green, 3 red; 2 red, 8 green, 3 blue; 2 red, 2 green",
        "Game 48: 2 red, 2 blue, 1 green; 1 green, 1 blue, 3 red; 1 blue, 1 red; 3 green, 8 blue",
        "Game 49: 7 red, 2 blue, 8 green; 8 red, 4 green; 2 blue, 4 red, 8 green",
        "Game 50: 9 red, 4 blue, 10 green; 11 red, 7 green, 4 blue; 4 green, 16 red, 2 blue; 13 red, 9 blue, 3 green; 1 red, 6 blue",
        "Game 51: 8 blue, 2 red, 3 green; 2 blue, 2 red; 4 blue, 1 green; 1 red, 2 blue, 2 green; 5 green, 6 blue, 1 red",
        "Game 52: 12 blue, 8 red; 11 green, 9 red, 11 blue; 8 blue, 5 green, 8 red; 3 red, 11 blue, 11 green; 12 blue, 6 green, 5 red; 10 red, 8 green",
        "Game 53: 9 green, 6 red, 3 blue; 4 blue, 5 green, 3 red; 11 green, 5 blue, 2 red; 4 red, 9 green",
        "Game 54: 13 blue, 8 green; 15 blue, 3 red, 7 green; 8 green, 1 blue; 8 blue, 3 red, 6 green; 3 red, 1 green, 12 blue; 9 green, 3 red, 2 blue",
        "Game 55: 2 red, 1 blue, 2 green; 4 blue, 3 green, 1 red; 4 red, 7 green, 4 blue; 7 green, 3 red, 1 blue; 2 blue, 4 green, 1 red; 5 blue, 1 red, 4 green",
        "Game 56: 14 green, 1 blue, 4 red; 3 red, 1 blue; 10 red, 8 blue; 8 red, 7 blue, 3 green; 3 green, 12 blue, 4 red; 7 red, 2 green",
        "Game 57: 7 blue, 8 green, 6 red; 7 green, 5 blue, 3 red; 2 red, 8 blue, 9 green",
        "Game 58: 7 green, 8 red, 3 blue; 7 red, 5 blue, 9 green; 4 blue, 3 red, 9 green; 1 green; 5 green, 2 blue; 5 blue, 7 green, 2 red",
        "Game 59: 2 blue, 10 green; 8 blue, 10 red, 1 green; 1 red, 10 blue, 7 green; 2 red, 7 blue, 1 green; 5 green, 3 blue",
        "Game 60: 1 green, 2 blue; 5 red, 2 green, 2 blue; 2 green, 3 red",
        "Game 61: 3 green, 2 red; 10 green, 7 red, 2 blue; 8 green, 2 blue; 5 green, 3 red, 1 blue; 12 green, 1 red; 1 blue, 13 green, 6 red",
        "Game 62: 11 green, 2 red; 3 blue, 3 red; 2 blue, 1 red, 10 green; 11 green, 3 blue",
        "Game 63: 7 blue; 7 red, 1 green, 8 blue; 5 red, 14 blue, 1 green",
        "Game 64: 2 green, 12 blue, 1 red; 18 blue, 10 red; 9 blue, 2 green, 13 red; 1 red, 1 green, 15 blue",
        "Game 65: 6 blue, 8 red, 8 green; 2 green, 9 red, 9 blue; 3 green, 9 red, 1 blue; 10 red, 4 blue, 2 green; 7 blue, 5 red, 5 green",
        "Game 66: 14 red, 3 green, 9 blue; 3 blue, 7 green, 12 red; 5 red, 8 green, 1 blue; 12 red, 5 green, 4 blue; 5 green, 14 blue",
        "Game 67: 1 blue, 9 red, 7 green; 12 red, 9 green, 1 blue; 13 red, 4 green, 2 blue; 1 red, 1 blue, 5 green; 10 red, 2 blue",
        "Game 68: 12 green, 2 red; 1 red, 4 green, 7 blue; 3 red, 4 blue, 14 green; 6 blue, 6 green; 7 blue, 4 green, 3 red",
        "Game 69: 2 green, 17 blue, 9 red; 6 blue, 3 green, 4 red; 11 blue, 4 red, 6 green",
        "Game 70: 11 blue, 10 red, 12 green; 9 red, 10 blue, 5 green; 2 red, 3 green, 9 blue; 5 green, 6 blue, 6 red; 12 green, 8 red, 10 blue",
        "Game 71: 7 blue, 3 red; 1 green, 11 blue, 1 red; 1 red, 5 blue, 1 green",
        "Game 72: 9 red, 7 blue; 1 green, 6 blue; 15 red, 6 blue; 5 red, 4 blue; 4 blue, 4 red, 1 green",
        "Game 73: 10 green, 4 red; 1 green, 5 red; 3 red, 1 green; 1 blue, 9 green, 6 red",
        "Game 74: 6 red, 3 blue, 8 green; 5 green, 9 red, 1 blue; 1 blue, 1 green, 2 red",
        "Game 75: 2 blue, 3 green; 3 blue, 7 green, 1 red; 6 green, 1 red; 5 green, 1 blue; 7 green, 3 blue",
        "Game 76: 4 red, 2 blue; 1 green, 7 red; 2 blue, 3 red; 1 green, 1 red, 1 blue; 4 red, 1 green",
        "Game 77: 18 green, 19 red, 11 blue; 1 blue, 18 red; 5 blue, 10 red, 16 green",
        "Game 78: 3 red, 8 blue, 1 green; 2 red, 3 blue; 1 green, 6 red, 12 blue",
        "Game 79: 5 red, 4 green, 9 blue; 3 blue; 4 red, 5 green, 2 blue; 7 blue, 5 green, 8 red; 5 red, 6 green; 7 blue, 5 green",
        "Game 80: 8 green, 11 red, 3 blue; 15 red, 4 blue, 8 green; 6 green, 14 red",
        "Game 81: 11 green, 5 red; 7 green, 14 blue, 4 red; 7 red, 8 blue, 2 green; 10 red, 3 green, 18 blue; 3 red, 1 green",
        "Game 82: 2 blue, 5 red; 3 green, 5 red, 7 blue; 3 green, 4 blue, 2 red; 10 blue, 2 green, 2 red; 8 blue, 2 red; 3 green, 3 red, 7 blue",
        "Game 83: 7 red, 12 green, 1 blue; 5 blue, 17 green, 5 red; 9 red, 3 blue; 2 blue, 1 red, 20 green; 5 red, 6 blue; 2 blue, 3 red, 11 green",
        "Game 84: 1 blue, 7 red, 6 green; 6 red, 8 green, 10 blue; 8 green, 1 blue, 6 red; 8 red, 4 blue, 6 green; 3 red, 12 blue, 8 green; 3 red, 2 blue, 7 green",
        "Game 85: 1 blue, 1 green, 8 red; 9 blue, 9 green, 2 red; 10 green, 12 red, 7 blue; 7 green, 2 blue, 7 red; 7 red, 3 green; 11 red, 9 blue, 5 green",
        "Game 86: 4 blue, 8 red; 4 red, 3 green; 7 blue, 12 red, 4 green; 4 green, 8 blue, 3 red",
        "Game 87: 6 blue, 19 green, 5 red; 20 green, 5 red, 5 blue; 8 red, 3 blue, 9 green; 11 blue, 7 green, 7 red; 17 green, 11 blue",
        "Game 88: 1 green, 2 red, 5 blue; 2 blue, 11 green; 3 red, 3 blue, 6 green; 4 blue, 2 green, 1 red; 8 green, 4 blue",
        "Game 89: 19 red, 15 green, 10 blue; 17 green, 1 red, 4 blue; 13 green, 10 blue, 15 red",
        "Game 90: 3 blue, 1 red; 4 blue, 1 red, 1 green; 4 green, 3 red; 4 red, 4 green, 5 blue; 2 green, 3 blue; 4 red, 2 green, 4 blue",
        "Game 91: 8 red, 4 blue, 16 green; 17 green, 5 blue, 4 red; 10 green, 6 red; 11 red, 7 blue; 14 blue, 4 red",
        "Game 92: 1 green, 3 red, 1 blue; 2 blue, 2 green, 5 red; 2 blue, 8 red; 1 blue, 2 green, 14 red; 3 red; 1 blue, 9 red",
        "Game 93: 11 blue, 7 red, 8 green; 8 red, 6 blue, 5 green; 4 blue, 4 green, 6 red",
        "Game 94: 2 green, 1 blue; 5 green, 5 red, 4 blue; 7 green, 2 blue; 5 red, 1 green, 3 blue; 2 blue, 1 green, 5 red; 1 red, 3 blue, 5 green",
        "Game 95: 3 red; 7 green, 4 red, 7 blue; 5 red, 5 blue",
        "Game 96: 3 red, 5 blue, 1 green; 3 blue, 14 red, 2 green; 7 blue, 3 red, 2 green; 15 red, 5 blue",
        "Game 97: 17 red, 8 green, 6 blue; 8 blue, 9 green; 4 green, 18 red",
        "Game 98: 9 blue, 2 green; 4 red, 6 blue, 3 green; 2 red; 14 red, 12 blue",
        "Game 99: 4 red, 3 green, 3 blue; 2 red, 2 blue; 7 green, 3 blue; 5 red, 2 green",
        "Game 100: 5 green, 7 red, 4 blue; 11 green, 9 red, 8 blue; 2 blue, 12 green"
    )

    val day2 = Day2(input)
    println(day2.solution1())
    println(day2.solution2())
}