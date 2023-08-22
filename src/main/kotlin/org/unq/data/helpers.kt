package org.unq.data

import org.unq.model.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random



fun getDeveloper(name: String): Developer {
    return allDevelopers.find { it.name == name } ?: throw Exception("T_T")
}

fun getListOfTags(names: List<String>): List<Tag> {
    return names.map { name -> allTags.find { it.name == name } ?: throw Exception("T_T") }
}

fun getESRB(name: String): ESRB {
    return when(name) {
        "Mature" -> ESRB.mature17plus
        "Everyone 10+" -> ESRB.everyone10plus
        "Teen" -> ESRB.teen
        "Adults Only" -> ESRB.adultsOnly
        "Everyone" -> ESRB.everyone
        "everyone" -> ESRB.everyone
        "Rating Pending" -> ESRB.ratingPending
        else -> ESRB.ratingPending
    }
}

fun getRandom(random: Random, from: Double, to: Double) : Double {
    return random.nextDouble(to - from) + from
}

fun randomPrice(random: Random): Price {
    return Price("USD", getRandom(random,0.0, 200.0))
}

fun getDate(date: String): LocalDate {
    return LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
}

fun <F> getRandomList(random: Random, list: List<F>, amountOfElements: Int): List<F> {
    return List(amountOfElements) { random.nextInt(0, list.size) }.toSet().map { list[it] }
}

fun getRandomBoolean(random: Random): Boolean {
    return random.nextBoolean()
}