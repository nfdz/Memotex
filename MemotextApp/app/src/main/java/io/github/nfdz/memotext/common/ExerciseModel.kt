package io.github.nfdz.memotext.common

data class Exercise(val elements: List<ExerciseElement>)

interface ExerciseElement

class SpaceElement : ExerciseElement
class TextElement(val text: String) : ExerciseElement
class SlotElement(val answer: String) : ExerciseElement



