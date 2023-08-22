package org.unq.data

import org.unq.model.Game
import org.unq.model.Image
import kotlin.random.Random

fun getAllGamesData(random: Random): List<Game> {
    val allGamesData = gamesData.map {
        Game(
            it.id,
            it.name,
            it.description,
            Image(it.mainImage),
            it.multimedia.map { image -> Image(image) },
            getListOfTags(it.tags),
            randomPrice(random),
            it.requirements,
            mutableListOf(),
            getDeveloper(it.developer),
            getDate(it.released),
            mutableListOf(),
            getESRB(it.esrb),
            it.website
        )
    }
    return allGamesData.map {
        val relatedGames = getRandomList(random, allGamesData, 10).toMutableList()
        relatedGames.remove(it)
        it.relatedGames.addAll(relatedGames)
        it
    }
}
