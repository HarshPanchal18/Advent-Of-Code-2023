import java.io.File

/*
--- Day 1: Trebuchet?! ---
Something is wrong with global snow production, and you've been selected to take a look. The Elves have even given you a map; on it, they've used stars to mark the top fifty locations that are likely to be having problems.

You've been doing this long enough to know that to restore snow operations, you need to check all fifty stars by December 25th.

Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!

You try to ask why they can't just use a weather machine ("not powerful enough") and where they're even sending you ("the sky") and why your map looks mostly blank ("you sure ask a lot of questions") and hang on did you just say the sky ("of course, where do you think snow comes from") when you realize that the Elves are already loading you into a trebuchet ("please hold still, we need to strap you in").

As they're making the final adjustments, they discover that their calibration document (your puzzle input) has been amended by a very young Elf who was apparently just excited to show off her art skills. Consequently, the Elves are having trouble reading the values on the document.

The newly-improved calibration document consists of lines of text; each line originally contained a specific calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining the first digit and the last digit (in that order) to form a single two-digit number.

For example:
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet

In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together produces 142.
Consider your entire calibration document. What is the sum of all the calibration values?
*/

class Day1(private val input: List<String>) {

    private val words = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    fun solution1(): Int {
        return input.sumOf { calibrationValue(it) }
    }

    fun solution2(): Int {
        return input.sumOf { row ->
            // Run through each character and turn it into a digit or a null,
            // and then map each of them to a String. In theory, we could take
            // the first and last digits from the resulting list instead of joining.
            calibrationValue(
                // Returning a list containing only the non-null results of applying the given transform function to each character and its index in the original char sequence.
                row.mapIndexedNotNull { index, ch ->
                    // if ch is already digit, take it as is.
                    if (ch.isDigit())
                        ch
                    else // Otherwise, see if this is the start of a word and if so map to the digit that it represents.
                        row.possibleWordsAt(index).firstNotNullOfOrNull { candidate ->
                            // Returning the first non-null value produced by transform function being applied to elements of this collection in iteration order, or null if no non-null value was produced.
                            words[candidate]
                        }
                }.joinToString()
            )
        }
    }

    private fun String.possibleWordsAt(position: Int): List<String> {
        return (3..5).map { len ->
            substring(position, (position + len).coerceAtMost(length))
        }
    }

    private fun calibrationValue(row: String): Int {
        // Return first and last digit of current row
        return "${row.first { it.isDigit() }}${row.last { it.isDigit() }}".toInt()
    }

}

fun main() {
    println(Day1(File("src/main/kotlin/inputs/Day1.txt").readLines()).solution1())
    println(Day1(File("src/main/kotlin/inputs/Day1.txt").readLines()).solution2())
}