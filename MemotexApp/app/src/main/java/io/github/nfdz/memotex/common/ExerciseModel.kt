package io.github.nfdz.memotex.common

import android.os.Parcel
import android.os.Parcelable


class Exercise(val elements: List<ExerciseElement>) : Parcelable {

    fun countSlots() = elements.count { it is SlotElement }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeList(elements)
    }
    override fun describeContents() = 0
    companion object CREATOR : Parcelable.Creator<Exercise> {
        override fun createFromParcel(parcel: Parcel): Exercise {
            return Exercise(arrayListOf<ExerciseElement>().apply {
                parcel.readList(this, ExerciseElement::class.java.classLoader)
            })
        }
        override fun newArray(size: Int): Array<Exercise?> {
            return arrayOfNulls(size)
        }
    }
}

interface ExerciseElement : Parcelable

class SpaceElement : ExerciseElement {
    override fun writeToParcel(parcel: Parcel, flags: Int) {}
    override fun describeContents() = 0
    companion object CREATOR : Parcelable.Creator<SpaceElement> {
        override fun createFromParcel(parcel: Parcel): SpaceElement {
            return SpaceElement()
        }
        override fun newArray(size: Int): Array<SpaceElement?> {
            return arrayOfNulls(size)
        }
    }
}

class TextElement(val text: String) : ExerciseElement {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
    }
    override fun describeContents() = 0
    companion object CREATOR : Parcelable.Creator<TextElement> {
        override fun createFromParcel(parcel: Parcel): TextElement {
            val text = parcel.readString()
            return TextElement(text ?: "")
        }
        override fun newArray(size: Int): Array<TextElement?> {
            return arrayOfNulls(size)
        }
    }
}

class SlotElement(val text: String) : ExerciseElement {
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
    }
    override fun describeContents() = 0
    companion object CREATOR : Parcelable.Creator<SlotElement> {
        override fun createFromParcel(parcel: Parcel): SlotElement {
            val text = parcel.readString()
            return SlotElement(text ?: "")
        }
        override fun newArray(size: Int): Array<SlotElement?> {
            return arrayOfNulls(size)
        }
    }
}

class ExerciseAnswers(val answers: Map<Int,String>) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.let {
            dest.writeInt(answers.size)
            answers.forEach { (key, value) ->
                dest.writeInt(key)
                dest.writeString(value)
            }
        }
    }
    override fun describeContents() = 0
    companion object CREATOR : Parcelable.Creator<ExerciseAnswers> {
        override fun createFromParcel(parcel: Parcel): ExerciseAnswers {
            val answersSize = parcel.readInt()
            val answers = HashMap<Int, String>(answersSize)
            for (i in 0 until answersSize) {
                val key = parcel.readInt()
                val value = parcel.readString()
                answers[key] = value ?: ""
            }
            return ExerciseAnswers(answers.toMap())
        }

        override fun newArray(size: Int): Array<ExerciseAnswers?> {
            return arrayOfNulls(size)
        }
    }
}

class ExerciseResult(val title: String, val content: String, val level: Level, val percentage: Int, val textSolution: CharSequence)
