package io.github.nfdz.memotext.common

import java.util.*

interface ExerciseAlgorithm {
    fun execute(text: String, wordsToHidePercent: Int, lettersToHidePercent: Int): Exercise
}

class ExerciseAlgorithmImpl : ExerciseAlgorithm {

    override fun execute(text: String, wordsToHidePercent: Int, lettersToHidePercent: Int): Exercise {
        // Split text
        val rawElementsValues = mutableListOf<String>()
        val validElementsIndexes = mutableListOf<Int>()
        val words = text.trim().split(Regex("\\s"))
        words.forEach { word ->
            rawElementsValues.add(word)
            if (isWordValid(word)) {
                validElementsIndexes.add(rawElementsValues.size - 1)
            }
        }
        // Randomize words to hide
        val wordsToHideIndexes = mutableSetOf<Int>()
        val wordsToHide = Math.floor(validElementsIndexes.size * wordsToHidePercent.toDouble()/100)
        val loopLimit = wordsToHide * 5
        var loopCounter = 0
        val rnd = Random()
        do {
            loopCounter++
            val wordToHideIndex = rnd.nextInt(validElementsIndexes.size)
            val wordToHide = validElementsIndexes[wordToHideIndex]
            if (!wordsToHideIndexes.contains(wordToHide) &&
                    !wordsToHideIndexes.contains(wordToHide+1) &&
                    !wordsToHideIndexes.contains(wordToHide-1)) {
                wordsToHideIndexes.add(wordToHide)
            }
        } while (wordsToHideIndexes.size < wordsToHide && loopCounter < loopLimit)
        // Build exercise
        val rawExercise = mutableListOf<Any>()
        rawExercise.add(SpaceElement())
        rawElementsValues.forEachIndexed { index, word ->
            if (wordsToHideIndexes.contains(index)) {
                rawExercise.add(SlotElement(word))
            } else {
                rawExercise.add(TextElementBuilder(word))
            }
            rawExercise.add(SpaceElement())
        }
        // Optimize
        do {
            var wasOptimizied = false
            val limit = rawExercise.size - 1
            for (i in 0..limit) {
                val currentElement = rawExercise[i]
                if (currentElement is TextElementBuilder) {
                    val nextTextElement = i + 2
                    if (nextTextElement < rawExercise.size) {
                        val nextElement = rawExercise[nextTextElement]
                        if (nextElement is TextElementBuilder) {
                            currentElement.text += " ${nextElement.text}"
                            rawExercise.remove(rawExercise[i + 1]) // whitespace
                            rawExercise.remove(nextElement) // whitespace
                            wasOptimizied = true
                            break
                        }
                    }
                }
            }
        } while (wasOptimizied)
        // Callback
        val exerciseList = mutableListOf<ExerciseElement>()
        rawExercise.forEach {
            if (it is TextElementBuilder) {
                exerciseList.add(it.build())
            } else if (it is ExerciseElement) {
                exerciseList.add(it)
            }
        }
        return Exercise(exerciseList.toList())
    }

    private fun isWordValid(word: String): Boolean {
        for (c in word) {
            if ((c !in 'a'..'z') && (c !in 'A'..'Z')) {
                return false
            }
        }
        return true
    }

    class TextElementBuilder(var text: String) {
        fun build() = TextElement(text)
    }

}