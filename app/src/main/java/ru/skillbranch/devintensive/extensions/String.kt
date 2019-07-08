package ru.skillbranch.devintensive.extensions

fun String.truncate(value: Int = 16): String {
    val trimmedString = this.trim()
    return if (trimmedString.length > value) "${trimmedString.substring(0, value).trim()}..."
    else trimmedString
}

fun String.stripHtml(): String {
    val myPattern = "((<.*?>)|(&amp;)|(&lt;)|(&gt;)|(&quot;)|(&#\\d+;))"
    val strippedString: String = Regex(myPattern).replace(this, "")
    return Regex(" +").replace(strippedString, " ")
}