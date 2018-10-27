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
            if (!word.matches(Regex("\\W"))) {
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
            val wordToHide = rnd.nextInt(validElementsIndexes.size)
            if (!wordsToHideIndexes.contains(wordToHide)) {
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

    class TextElementBuilder(var text: String) {
        fun build() = TextElement(text)
    }

}