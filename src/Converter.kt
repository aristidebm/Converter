package org.godmind.converter

import java.math.BigInteger


val PREDEFINED_LETTER_TO_NUMBER = mapOf(
    "0" to "zero",
    "1" to "un",
    "2" to "deux",
    "3" to "trois",
    "4" to "quatre",
    "5" to "cinq",
    "6" to "six",
    "7" to "sept",
    "8" to "huit",
    "9" to "neuf",
    "10" to "dix",
    "11" to "onze",
    "12" to "douze",
    "13" to "treize",
    "14" to "quatorze",
    "15" to "quinze",
    "16" to "seize",
    "17" to "dix-sept",
    "18" to "dix-neuf",
    "19" to "dix-neuf",
    "20" to "vingt",
    "30" to "trente",
    "40" to "quarante",
    "50" to "cinquante",
    "60" to "soixante",
    "70" to "soixante-dix",
    "71" to "soixante-onze",
    "72" to "soixante-douze",
    "73" to "soixante-treize",
    "74" to "soixante-quatorze",
    "75" to "soixante-quinze",
    "76" to "soixante-dix-seize",
    "77" to "soixante-dix-sept",
    "78" to "soixante-dix-huit",
    "79" to "soixante-dix-neuf",
    "80" to "quatre-vingt",
    "90" to "quatre-vingt-dix",
    "91" to "quatre-vingt-onze",
    "92" to "quatre-vingt-douze",
    "93" to "quatre-vingt-treize",
    "94" to "quatre-vingt-quatorze",
    "95" to "quatre-vingt-quinze",
    "96" to "quatre-vingt-dix-seize",
    "97" to "quatre-vingt-dix-sept",
    "98" to "quatre-vingt-dix-huit",
    "99" to "quatre-vingt-dix-neuf",
)

val POSTFIX = mapOf(
    "0" to "cent",
    "1" to "mille",
    "2" to "million",
    "3" to "milliard",
    "4" to "billion",
    "5" to "billiard",
    "6" to "trillion",
    "7" to "trilliard",
    "8" to "quadrillion"
)

class Converter {
    fun convert(number: String): String? {
        if (number in PREDEFINED_LETTER_TO_NUMBER) {
            return PREDEFINED_LETTER_TO_NUMBER[number]
        }
        val result: MutableList<String> = mutableListOf()
        val blocks: MutableList<Int> = this.decompose(number.toBigInteger())
        blocks.reverse()
        val blocksLength = blocks.size
        for (idx in 0 until blocksLength) {
            result.addAll(this.numberToLetter(blocksLength - idx - 1, blocks[idx]))
        }
        return result.joinToString(" ")
    }

    private fun decompose(number: BigInteger): MutableList<Int> {
        val blocks: MutableList<Int> = mutableListOf()
        var localNumber: BigInteger = number
        var div: BigInteger
        var mod: BigInteger
        val divisor = BigInteger.valueOf(1000)
        val stopBound = BigInteger.valueOf(0)
        while (localNumber != stopBound) {
            div = localNumber / divisor
            mod = localNumber % divisor
            blocks.add(mod.intValueExact())
            localNumber = div
        }
        return blocks
    }

    private fun numberToLetter(idx: Int, block: Int): MutableList<String> {
        val letter: MutableList<String> = mutableListOf()
        if (block == 0) {
            return letter
        }
        val divisor = 100
        val centaine: Int = block / divisor
        val dizaine: Int = block % divisor

        if (centaine != 0 && centaine != 1) {
            PREDEFINED_LETTER_TO_NUMBER[centaine.toString()]?.let { letter.add(it) }
            POSTFIX["0"]?.let { letter.add(it) }
        }
        // we don't want to have "un cent" we want "cent" instead
        if (centaine == 1) {
            POSTFIX["0"]?.let { postfix -> letter.add(postfix) }
        }

        if (dizaine != 0 && dizaine.toString() in PREDEFINED_LETTER_TO_NUMBER) {
            PREDEFINED_LETTER_TO_NUMBER[dizaine.toString()]?.let { letter.add(it) }
        } else if (dizaine != 0) {
            val div: Int = dizaine / 10
            val mod: Int = dizaine % 10
            PREDEFINED_LETTER_TO_NUMBER[(div * 10).toString()]?.let { letter.add(it) }
            PREDEFINED_LETTER_TO_NUMBER[mod.toString()]?.let { letter.add(it) }
        }
        // ignore cent
        if (idx != 0) {
            POSTFIX[idx.toString()]?.let { letter.add(it) }
        }
        // we don't want "un mille" but just "mille"
        if (idx == 1 && block == 1) {
            letter.remove(PREDEFINED_LETTER_TO_NUMBER["1"])
        }
        return letter
    }
}

fun main(args: Array<String>) {
    var number: String?
    do {
        print("Entrer un nombre : ")
        number = readLine()
    } while (!number?.matches(regex = Regex("(\\d+[_]?\\d+)+"))!!)
    number = number.split("_").joinToString("")
    val converter = Converter()
    val letterNumber = converter.convert(number)
    println("$number: $letterNumber")
}

