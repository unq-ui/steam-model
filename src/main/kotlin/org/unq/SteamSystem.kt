package org.unq

import org.unq.model.*

fun <E> getPage(list: List<E>, page: Int): PageInfo<E> {
    if (page < 1) throw Exception("Page most be 1 or more")
    val chunkedList = list.chunked(10)
    return PageInfo(
        currentPage = page,
        list = chunkedList.getOrElse(page - 1) { listOf() },
        amountOfElements = list.size,
        amountOfPages = chunkedList.size,
    )
}

class SteamSystem(
    val games: List<Game>,
    val developers: List<Developer>,
    val tags: List<Tag>,
    val users: MutableList<User>,
) {

    private val idGenerator = IdGenerator()

    fun addNewUser(user: DraftUser): User {
        users.forEach {
            if (it.email == user.email) throw UserException("Email is token")
        }
        val newUser = User(
            idGenerator.nextUserId(),
            user.email,
            user.password,
            user.name,
            user.image,
            user.backgroundImage,
            mutableListOf(),
            mutableListOf(),
        )
        users.add(newUser)
        return newUser
    }

    fun getTag(id: String): Tag {
        return tags.find { it.id == id } ?: throw NotFoundTag()
    }

    fun getUser(id: String): User {
        return users.find { it.id == id } ?: throw NotFoundUser()
    }

    fun getGame(id: String): Game {
        return games.find { it.id == id } ?: throw NotFoundGame()
    }

    fun getDeveloper(id: String): Developer {
        return developers.find { it.id == id } ?: throw NotFoundDeveloper()
    }

    fun getUserReviews(userId: String): List<Review> {
        val user = getUser(userId)
        return games
            .flatMap { game -> game.reviews }
            .filter { it.user === user }
    }

    fun getRecommendedGames(): List<Game> {
        return games
            .sortedByDescending { game -> game.reviews.count { review -> review.isRecommended } }
            .take(10)
    }

    fun getGames(page: Int = 1): PageInfo<Game> {
        return getPage(games, page)
    }

    fun getGamesByTag(tagId: String, page: Int = 1): PageInfo<Game> {
        val tag = getTag(tagId)
        val filterGames = games.filter { it.tags.contains(tag) }
        return getPage(filterGames, page)
    }

    fun getGamesByDeveloper(developerId: String, page: Int = 1): PageInfo<Game> {
        val developer = getDeveloper(developerId)
        val filterGames = games.filter { it.developer == developer }
        return getPage(filterGames, page)
    }

    fun searchGame(name: String, page: Int = 1): PageInfo<Game> {
        val filterGames = games.filter { it.name.contains(name, true) }
        return getPage(filterGames, page)
    }

    fun searchUser(name: String, page: Int = 1): PageInfo<User> {
        val filterUsers = users.filter { it.name.contains(name, true) }
        return getPage(filterUsers, page)
    }

    fun addReview(userId: String, draftReview: DraftReview): Game {
        val user = getUser(userId)
        val game = getGame(draftReview.gameId)
        if (!user.games.contains(game)) throw ReviewException("You need to own the game to leave a review")
        game.reviews.find { it.user == user }?.let { throw  ReviewException("You've already submitted a review for this game") }
        game.reviews.add(Review(idGenerator.nextReviewId(), user, draftReview.isRecommended, draftReview.text))
        return game
    }

    fun purchaseGame(userId: String, draftPurchase: DraftPurchase): User {
        val user = getUser(userId)
        val game = getGame(draftPurchase.gameId)
        if(user.games.contains(game)) throw PurchaseException("You already have the game")
        user.games.add(game)
        return user
    }

    fun addOrRemoveFriend(userId: String, friendId: String): User {
        if (userId == friendId) throw UserException("You cannot self-add.")
        val user = getUser(userId)
        val friend = getUser(friendId)
        if (user.friends.remove(friend)) {
            friend.friends.remove(user)
        } else {
            user.friends.add(friend)
            friend.friends.add(user)
        }
        return user
    }

}
