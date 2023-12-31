package org.unq

import org.unq.model.*

/**
 * Get a paginated list of elements.
 *
 * @param list The list of elements.
 * @param page The page number.
 * @return A pagination object for the elements.
 * @throws PageException If the page number is less than 1.
 */
fun <E> getPage(list: List<E>, page: Int): PageInfo<E> {
    if (page < 1) throw PageException("Page most be 1 or more")
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

    /**
     * Add a new user to the system.
     *
     * @param user The user to add.
     * @return The newly created user.
     * @throws UserException If the email address is already in use.
     */
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

    /**
     * Get a tag by its ID.
     *
     * @param id The ID of the tag.
     * @return The tag with the given ID.
     * @throws NotFoundTag If the tag with the given ID does not exist.
     */
    fun getTag(id: String): Tag {
        return tags.find { it.id == id } ?: throw NotFoundTag()
    }

    /**
     * Get a user by its ID.
     *
     * @param id The ID of the user.
     * @return The user with the given ID.
     * @throws NotFoundUser If the user with the given ID does not exist.
     */
    fun getUser(id: String): User {
        return users.find { it.id == id } ?: throw NotFoundUser()
    }

    /**
     * Get a game by its ID.
     *
     * @param id The ID of the game.
     * @return The game with the given ID.
     * @throws NotFoundGame If the game with the given ID does not exist.
     */
    fun getGame(id: String): Game {
        return games.find { it.id == id } ?: throw NotFoundGame()
    }

    /**
     * Get a developer by its ID.
     *
     * @param id The ID of the developer.
     * @return The developer with the given ID.
     * @throws NotFoundDeveloper If the developer with the given ID does not exist.
     */
    fun getDeveloper(id: String): Developer {
        return developers.find { it.id == id } ?: throw NotFoundDeveloper()
    }

    /**
     * Get the reviews written by the user with the given ID.
     *
     * @param userId The ID of the user.
     * @return The list of reviews written by the user.
     * @throws NotFoundUser If the user with the given ID does not exist.
     */
    fun getUserReviews(userId: String): List<Review> {
        val user = getUser(userId)
        return games
            .flatMap { game -> game.reviews }
            .filter { it.user === user }
    }

    /**
     * Get a list of recommended games.
     *
     * The recommended games are the games that have the most reviews that are marked as recommended.
     *
     * @return A list of recommended games.
     */
    fun getRecommendedGames(): List<Game> {
        return games
            .sortedByDescending { game -> game.reviews.count { review -> review.isRecommended } }
            .take(10)
    }

    /**
     * Get a list of games.
     *
     * @param page The page number.
     * @return A list of games.
     * @throws PageException If the page number is less than 1.
     */
    fun getGames(page: Int = 1): PageInfo<Game> {
        return getPage(games, page)
    }

    /**
     * Get a list of games by tag.
     *
     * @param tagId The ID of the tag.
     * @param page The page number.
     * @return A list of games.
     * @throws NotFoundTag If the tag with the given ID does not exist.
     * @throws PageException If the page number is less than 1.
     */
    fun getGamesByTag(tagId: String, page: Int = 1): PageInfo<Game> {
        val tag = getTag(tagId)
        val filterGames = games.filter { it.tags.contains(tag) }
        return getPage(filterGames, page)
    }

    /**
     * Get a list of games by developer.
     *
     * @param developerId The ID of the developer.
     * @param page The page number.
     * @return A list of games.
     * @throws NotFoundDeveloper If the developer with the given ID does not exist.
     * @throws PageException If the page number is less than 1.
     */
    fun getGamesByDeveloper(developerId: String, page: Int = 1): PageInfo<Game> {
        val developer = getDeveloper(developerId)
        val filterGames = games.filter { it.developer == developer }
        return getPage(filterGames, page)
    }

    /**
     * Search for games by name.
     *
     * @param name The name of the game.
     * @param page The page number.
     * @return A list of games.
     * @throws PageException If the page number is less than 1.
     */
    fun searchGame(name: String, page: Int = 1): PageInfo<Game> {
        val filterGames = games.filter { it.name.contains(name, true) }
        return getPage(filterGames, page)
    }

    /**
     * Search for users by name.
     *
     * @param name The name of the user.
     * @param page The page number.
     * @return A list of users.
     * @throws PageException If the page number is less than 1.
     */
    fun searchUser(name: String, page: Int = 1): PageInfo<User> {
        val filterUsers = users.filter { it.name.contains(name, true) }
        return getPage(filterUsers, page)
    }

    /**
     * Add a review for a game.
     *
     * @param userId The ID of the user who is submitting the review.
     * @param draftReview The draft review object.
     * @return The game that was reviewed.
     * @throws NotFoundUser If the user with the given ID does not exist.
     * @throws NotFoundGame If the game with the given ID does not exist.
     * @throws ReviewException If the user does not own the game or if the user has already submitted a review for the game.
     */
    fun addReview(userId: String, draftReview: DraftReview): Game {
        val user = getUser(userId)
        val game = getGame(draftReview.gameId)
        if (!user.games.contains(game)) throw ReviewException("You need to own the game to leave a review")
        game.reviews.find { it.user == user }?.let { throw  ReviewException("You've already submitted a review for this game") }
        game.reviews.add(Review(idGenerator.nextReviewId(), user, game, draftReview.isRecommended, draftReview.text))
        return game
    }

    /**
     * Purchase a game.
     *
     * @param userId The ID of the user who is purchasing the game.
     * @param draftPurchase The draft purchase object.
     * @return The user who purchased the game.
     * @throws NotFoundUser If the user with the given ID does not exist.
     * @throws NotFoundGame If the game with the given ID does not exist.
     * @throws PurchaseException If the user already owns the game.
     */
    fun purchaseGame(userId: String, draftPurchase: DraftPurchase): User {
        val user = getUser(userId)
        val game = getGame(draftPurchase.gameId)
        if(user.games.contains(game)) throw PurchaseException("You already have the game")
        user.games.add(game)
        return user
    }

    /**
     * Add or remove a friend.
     *
     * @param userId The ID of the user who is adding or removing the friend.
     * @param friendId The ID of the friend.
     * @return The user who added or removed the friend.
     * @throws NotFoundUser If the user or the friend with the given ID does not exist.
     * @throws UserException If the user is trying to self-add.
     */
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
