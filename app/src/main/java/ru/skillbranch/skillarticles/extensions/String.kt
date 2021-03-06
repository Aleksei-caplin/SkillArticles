package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf(needle: String, ignoreCase: Boolean = true): List<Int> {

    val indexes = mutableListOf<Int>()

    if (this.isNullOrEmpty() || needle.isEmpty()) return indexes

    var currentIdx = 0

    while (currentIdx > -1) {
        currentIdx = indexOf(needle, currentIdx, ignoreCase)
        if (currentIdx > -1) {
            indexes.add(currentIdx)
            currentIdx += needle.length
        }
    }

    return indexes

    //TODO boyer moore
    //val indexes = mutableListOf<Int>()
    //if (this == null || needle.isEmpty()) return indexes
//
    //val needleArr = needle.toCharArray()
    //var currentIdx = BoyerMoore.indexOf(this.toCharArray(), needleArr)
//
    //while (currentIdx != -1) {
    //    indexes.add(currentIdx)
    //    currentIdx = BoyerMoore.indexOf(this.substring(currentIdx + needleArr.size).toCharArray(), needleArr)
    //}
//
    //return indexes
}