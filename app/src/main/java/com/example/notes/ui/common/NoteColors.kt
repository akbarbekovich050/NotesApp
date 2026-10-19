package com.example.notes.ui.common

import androidx.annotation.ColorRes
import com.example.notes.R

/** Палитра карточек: индекс хранится в базе. */
object NoteColors {
    data class Option(@ColorRes val color: Int, val label: String)

    val options = listOf(
        Option(R.color.note_white, "Белый"),
        Option(R.color.note_yellow, "Жёлтый"),
        Option(R.color.note_green, "Зелёный"),
        Option(R.color.note_blue, "Голубой"),
        Option(R.color.note_pink, "Розовый"),
        Option(R.color.note_purple, "Сиреневый"),
    )

    @ColorRes
    fun colorRes(index: Int): Int = options.getOrElse(index) { options[0] }.color
}
