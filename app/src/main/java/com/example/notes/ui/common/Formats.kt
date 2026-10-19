package com.example.notes.ui.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val dateFormat = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.forLanguageTag("ru"))

fun formatDate(millis: Long): String = dateFormat.format(Date(millis))
