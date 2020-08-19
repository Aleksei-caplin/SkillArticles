package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true) =
    when {
        this == null || substr.isEmpty() -> listOf()
        else -> substr
            .run { if (ignoreCase) toRegex(RegexOption.IGNORE_CASE) else toRegex() }
            .findAll(this)
            .map { it.range.first }
            .toList()
    }