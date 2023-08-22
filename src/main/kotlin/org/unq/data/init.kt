package org.unq.data

import org.unq.SteamSystem
import org.unq.model.CardInfo
import org.unq.model.DraftPurchase
import org.unq.model.DraftReview
import java.time.LocalDate
import kotlin.random.Random

fun initSteamSystem(): SteamSystem {
    val random = Random(100)

    val games = getAllGamesData(random)

    val steam = SteamSystem(
        games,
        allDevelopers,
        allTags,
        mutableListOf()
    )

    allUsers.map { steam.addNewUser(it) }
    steam.users.forEach { user ->
        getRandomList(random, steam.games, 40).forEach {
            steam.purchaseGame(user.id, DraftPurchase(it.id, CardInfo("a", 1234, LocalDate.now(), 123)))
        }
        getRandomList(random, steam.users, 5).forEach {
            if (user.id != it.id) {
                steam.addOrRemoveFriend(user.id, it.id)
            }
        }
        getRandomList(random, user.games, 30).forEach {
            steam.addReview(user.id, DraftReview(it.id, getRandomBoolean(random), getRandomList(random, allReviewText, 1)[0]))
        }
    }

    return steam
}
