package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when(units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    var diff = this.time - date.time
    // разница между временем текущего экземпляра и временем, которое передано в качестве аргумента (lastVisit)
    val isPast = (diff < 0)
    diff = abs(diff)
    return when(diff) {
        in 0..SECOND -> "только что"
        in SECOND+1..SECOND*45 -> if (isPast) "несколько секунд назад" else "через несколько секунд"
        in SECOND*45+1..SECOND*75 -> if (isPast) "минуту назад" else "через минуту"
        in SECOND*75+1..MINUTE*45 -> {
            val minutes: Int = (diff / 1000 / 60).toInt()
            val plural = TimeUnits.MINUTE.plural(minutes)
            if (isPast) "$plural назад" else "через $plural"
        }
        in MINUTE*45+1..MINUTE*75 -> if (isPast) "час назад" else "через час"
        in MINUTE*75+1..HOUR*22 -> {
            val hours: Int = (diff / 1000 / 60 / 60).toInt()
            val plural = TimeUnits.HOUR.plural(hours)
            if (isPast) "$plural назад" else "через $plural"
        }
        in HOUR*22+1..HOUR*26 -> if (isPast) "день назад" else "через день"
        in HOUR*26+1..DAY*360 -> {
            val days: Int = (diff / 1000 / 60 / 60 / 24).toInt()
            val plural = TimeUnits.DAY.plural(days)
            if (isPast) "$plural назад" else "через $plural"
        }
        else -> if (isPast) "более года назад" else "более чем через год"
    }
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(i: Int): String {
        val category = when {
            i % 10 == 1 && i % 100 != 11 -> "one"
            i % 10 in 2..4 && i % 100 !in 12..14 -> "few"
            else -> "many"
        }
        return when {
            this == SECOND && category == "one" -> "$i секунду"
            this == MINUTE && category == "one" -> "$i минуту"
            this == HOUR && category == "one" -> "$i час"
            this == DAY && category == "one" -> "$i день"
            this == SECOND && category == "few" -> "$i секунды"
            this == MINUTE && category == "few" -> "$i минуты"
            this == HOUR && category == "few" -> "$i часа"
            this == DAY && category == "few" -> "$i дня"
            this == SECOND && category == "many" -> "$i секунд"
            this == MINUTE && category == "many" -> "$i минут"
            this == HOUR && category == "many" -> "$i часов"
            this == DAY && category == "many" -> "$i дней"
            else -> "неизвестный формат"
        }
    }
}