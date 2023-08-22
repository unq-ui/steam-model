package org.unq

import org.unq.data.*
import org.unq.model.*
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SteamSystemTest {

    private fun createDraftUser(name: String): DraftUser {
        return DraftUser(
            name,
            "$name@gmail.com",
            "password_$name",
            "https://google.com/image/$name",
            "https://google.com/backgroundImage/$name"
        )
    }

    private fun createTag(tagNumber: Int): Tag {
        return Tag(
            "t_$tagNumber",
            "t$tagNumber",
            Image("https://google.com/t$tagNumber")
        )
    }

    private fun createDeveloper(developerNumber: Int): Developer {
        return Developer("d_$developerNumber", "D$developerNumber", Image("https://google.com/d$developerNumber"))
    }

    private fun createGame(gameNumber: Int, tag: Tag, developer: Developer, releaseDate: LocalDate): Game {
        return Game(
            "g_$gameNumber",
            "g$gameNumber",
            "g$gameNumber",
            Image("https://google.com/g$gameNumber"),
            listOf(Image("https://google.com/g$gameNumber/image")),
            listOf(tag),
            Price("USD", 20.2),
            Requirement(),
            mutableListOf(),
            developer,
            releaseDate,
            mutableListOf(),
            ESRB.everyone,
            "https://google.com/g$gameNumber"
        )
    }

    private val allTags = listOf(
        createTag(0),
        createTag(1),
        createTag(2),
    )

    private val allDeveloper = listOf(
        createDeveloper(0),
        createDeveloper(1),
    )

    private val allGames = listOf(
        createGame(0, allTags[0], allDeveloper[0], getDate("2020-10-10")),
        createGame(1, allTags[0], allDeveloper[0], getDate("2020-10-09")),
    )

    fun getSteamSystem(): SteamSystem {
        return SteamSystem(
            allGames,
            allDeveloper,
            allTags,
            mutableListOf()
        )
    }

    val system = initSteamSystem()

    @Test
    fun addNewUserTest() {
        val steamSystem = getSteamSystem()
        assertEquals(steamSystem.users.size, 0)
        steamSystem.addNewUser(createDraftUser("a"))
        assertEquals(steamSystem.users.size, 1)
        val user = steamSystem.users[0]
        assertEquals(user.id, "u_0")
        assertEquals(user.name, "a")
    }

    @Test
    fun addNewUserTwoTimesTest() {
        val steamSystem = getSteamSystem()
        assertEquals(steamSystem.users.size, 0)
        steamSystem.addNewUser(createDraftUser("a"))
        assertEquals(steamSystem.users.size, 1)
        assertFailsWith<UserException>("Email is token") {
            steamSystem.addNewUser(createDraftUser("a"))
        }
    }

    @Test
    fun getTagTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(createTag(0)),
            mutableListOf()
        )
        val tag = steamSystem.getTag("t_0")
        assertEquals(tag.id, "t_0")
    }

    @Test
    fun getTagWithoutTagsTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        assertFailsWith<NotFoundTag>("Not found Tag") {
            steamSystem.getTag("t_0")
        }
    }

    @Test
    fun getTagWithWrongIdTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(createTag(0)),
            mutableListOf()
        )
        assertFailsWith<NotFoundTag>("Not found Tag") {
            steamSystem.getTag("t_1")
        }
    }

    @Test
    fun getUserTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        steamSystem.addNewUser(createDraftUser("a"))
        val user = steamSystem.getUser("u_0")
        assertEquals(user.name, "a")
    }

    @Test
    fun getUserWithoutUsersTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        assertFailsWith<NotFoundUser>("Not found User") {
            steamSystem.getUser("u_0")
        }
    }

    @Test
    fun getUserWithWrongIdTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        steamSystem.addNewUser(createDraftUser("a"))
        assertFailsWith<NotFoundUser>("Not found User") {
            steamSystem.getUser("u_1")
        }
    }

    @Test
    fun getGameTest() {
        val steamSystem = SteamSystem(
            listOf(createGame(0, allTags[0], allDeveloper[0], getDate("2020-10-10"))),
            listOf(),
            listOf(),
            mutableListOf()
        )
        val game = steamSystem.getGame("g_0")
        assertEquals(game.name, "g0")
    }

    @Test
    fun getGameWithoutGamesTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        assertFailsWith<NotFoundGame>("Not found Game") {
            steamSystem.getGame("g_0")
        }
    }

    @Test
    fun getGameWithWrongIdTest() {
        val steamSystem = SteamSystem(
            listOf(createGame(0, allTags[0], allDeveloper[0], getDate("2020-10-10"))),
            listOf(),
            listOf(),
            mutableListOf()
        )
        assertFailsWith<NotFoundGame>("Not found Game") {
            steamSystem.getGame("g_1")
        }
    }

    @Test
    fun getDeveloperTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(createDeveloper(0)),
            listOf(),
            mutableListOf()
        )
        val developer = steamSystem.getDeveloper("d_0")
        assertEquals(developer.name, "D0")
    }

    @Test
    fun getDeveloperWithoutDevelopersTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )

        assertFailsWith<NotFoundDeveloper>("Not found Developer") {
            steamSystem.getDeveloper("d_0")
        }
    }

    @Test
    fun getDeveloperWithWrongIdTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(createDeveloper(0)),
            listOf(),
            mutableListOf()
        )

        assertFailsWith<NotFoundDeveloper>("Not found Developer") {
            steamSystem.getDeveloper("d_1")
        }
    }

    @Test
    fun getUserReviewsTest() {
        val steamSystem = system
        val reviews = steamSystem.getUserReviews("u_0")
        assertEquals(reviews.size, 21)
    }

    @Test
    fun getUserReviewsWithWrongIdTest() {
        val steamSystem = system
        assertFailsWith<NotFoundUser>("Not found User") {
            steamSystem.getUserReviews("u_100")
        }
    }

    @Test
    fun getRecommendedGames() {
        val steamSystem = system
        val games = steamSystem.getRecommendedGames()
        assertEquals(games.size, 10)
        assertEquals(games[0].name, "Red Dead Redemption 2")
        assertEquals(games[9].name, "Super Meat Boy")
    }

    @Test
    fun getGamesTest() {
        val steamSystem = system
        val page = steamSystem.getGames()
        assertEquals(page.currentPage, 1)
        assertEquals(page.list.size, 10)
        assertEquals(page.amountOfElements, 80)
        assertEquals(page.amountOfPages, 8)
    }

    @Test
    fun getGamesWithPage0Test() {
        val steamSystem = system
        assertFailsWith<Exception>("Page most be 1 or more") {
            steamSystem.getGames(0)
        }
    }

    @Test
    fun getGamesWithPage9Test() {
        val steamSystem = system
        val page = steamSystem.getGames(9)
        assertEquals(page.currentPage, 9)
        assertEquals(page.list.size, 0)
        assertEquals(page.amountOfElements, 80)
        assertEquals(page.amountOfPages, 8)
    }

    @Test
    fun getGamesByTagTest() {
        val steamSystem = system
        val page = steamSystem.getGamesByTag("t_0")
        assertEquals(page.currentPage, 1)
        assertEquals(page.list.size, 10)
        assertEquals(page.amountOfElements, 74)
        assertEquals(page.amountOfPages, 8)
    }

    @Test
    fun getGamesByTagWithPage0Test() {
        val steamSystem = system
        assertFailsWith<Exception>("Page most be 1 or more") {
            steamSystem.getGamesByTag("t_0", 0)
        }
    }

    @Test
    fun getGamesByTagWithPage9Test() {
        val steamSystem = system
        val page = steamSystem.getGamesByTag("t_0", 9)
        assertEquals(page.currentPage, 9)
        assertEquals(page.list.size, 0)
        assertEquals(page.amountOfElements, 74)
        assertEquals(page.amountOfPages, 8)
    }

    @Test
    fun getGamesByTagWithWrongTagIdTest() {
        val steamSystem = system
        assertFailsWith<NotFoundTag>("Tag not found") {
            steamSystem.getGamesByTag("t_1000")
        }
    }

    @Test
    fun getGamesByDeveloperTest() {
        val steamSystem = system
        val page = steamSystem.getGamesByDeveloper("d_0")
        assertEquals(page.currentPage, 1)
        assertEquals(page.list.size, 3)
        assertEquals(page.amountOfElements, 3)
        assertEquals(page.amountOfPages, 1)
    }

    @Test
    fun getGamesByDeveloperWithPage0Test() {
        val steamSystem = system
        assertFailsWith<Exception>("Page most be 1 or more") {
            steamSystem.getGamesByDeveloper("d_0", 0)
        }
    }

    @Test
    fun getGamesByDeveloperWithPage9Test() {
        val steamSystem = system
        val page = steamSystem.getGamesByDeveloper("d_0", 9)
        assertEquals(page.currentPage, 9)
        assertEquals(page.list.size, 0)
        assertEquals(page.amountOfElements, 3)
        assertEquals(page.amountOfPages, 1)
    }

    @Test
    fun getGamesByDeveloperWithWrongTagIdTest() {
        val steamSystem = system
        assertFailsWith<NotFoundDeveloper>("Developer not found") {
            steamSystem.getGamesByDeveloper("d_1000")
        }
    }

    @Test
    fun searchGameTest() {
        val steamSystem = system
        val page = steamSystem.searchGame("auto")
        assertEquals(page.currentPage, 1)
        assertEquals(page.list.size, 3)
        assertEquals(page.amountOfElements, 3)
        assertEquals(page.amountOfPages, 1)
    }

    @Test
    fun searchGameWithoutResultTest() {
        val steamSystem = system
        val page = steamSystem.searchGame("pepe")
        assertEquals(page.currentPage, 1)
        assertEquals(page.list.size, 0)
        assertEquals(page.amountOfElements, 0)
        assertEquals(page.amountOfPages, 0)
    }

    @Test
    fun searchUserTest() {
        val steamSystem = system
        val page = steamSystem.searchUser("a")
        assertEquals(page.currentPage, 1)
        assertEquals(page.list.size, 10)
        assertEquals(page.amountOfElements, 16)
        assertEquals(page.amountOfPages, 2)
    }

    @Test
    fun searchUserWithoutResultTest() {
        val steamSystem = system
        val page = steamSystem.searchUser("lola")
        assertEquals(page.currentPage, 1)
        assertEquals(page.list.size, 0)
        assertEquals(page.amountOfElements, 0)
        assertEquals(page.amountOfPages, 0)
    }

    @Test
    fun addReviewTest() {
        val steamSystem = SteamSystem(
            allGames,
            allDeveloper,
            allTags,
            mutableListOf()
        )
        steamSystem.addNewUser(createDraftUser("a"))
        steamSystem.purchaseGame("u_0", DraftPurchase("g_0", CardInfo("a", 1234, LocalDate.now(), 123)))
        val game = steamSystem.getGame("g_0")
        assertEquals(game.reviews.size, 0)
        steamSystem.addReview("u_0", DraftReview("g_0", true, "text"))
        assertEquals(game.reviews.size, 1)
        assertEquals(game.reviews[0].user.id, "u_0")
    }

    @Test
    fun addReviewTwoTimesTest() {
        val steamSystem = SteamSystem(
            allGames,
            allDeveloper,
            allTags,
            mutableListOf()
        )
        steamSystem.addNewUser(createDraftUser("a"))
        steamSystem.purchaseGame("u_0", DraftPurchase("g_0", CardInfo("a", 1234, LocalDate.now(), 123)))
        val game = steamSystem.getGame("g_0")
        assertEquals(game.reviews.size, 0)
        steamSystem.addReview("u_0", DraftReview("g_0", true, "text"))
        assertEquals(game.reviews.size, 1)
        assertEquals(game.reviews[0].user.id, "u_0")
        assertFailsWith<ReviewException>("You've already submitted a review for this game") {
            steamSystem.addReview("u_0", DraftReview("g_0", true, "text"))
        }
    }

    @Test
    fun addReviewWithoutPurchaseGameTest() {
        val steamSystem = SteamSystem(
            allGames,
            allDeveloper,
            allTags,
            mutableListOf()
        )
        steamSystem.addNewUser(createDraftUser("a"))
        val game = steamSystem.getGame("g_0")
        assertEquals(game.reviews.size, 0)
        assertFailsWith<ReviewException>("You need to own the game to leave a review") {
            steamSystem.addReview("u_0", DraftReview("g_0", true, "text"))
        }
    }

    @Test
    fun purchaseGameTest() {
        val steamSystem = SteamSystem(
            allGames,
            allDeveloper,
            allTags,
            mutableListOf()
        )
        val user = steamSystem.addNewUser(createDraftUser("a"))
        assertEquals(user.games.size, 0)
        steamSystem.purchaseGame("u_0", DraftPurchase("g_0", CardInfo("a", 1234, LocalDate.now(), 123)))
        assertEquals(user.games.size, 1)
        assertEquals(user.games[0].id, "g_0")
    }

    @Test
    fun purchaseGameTwoTimesTest() {
        val steamSystem = SteamSystem(
            allGames,
            allDeveloper,
            allTags,
            mutableListOf()
        )
        val user = steamSystem.addNewUser(createDraftUser("a"))
        assertEquals(user.games.size, 0)
        steamSystem.purchaseGame("u_0", DraftPurchase("g_0", CardInfo("a", 1234, LocalDate.now(), 123)))
        assertEquals(user.games.size, 1)
        assertEquals(user.games[0].id, "g_0")
        assertFailsWith<PurchaseException>("You already have the game") {
            steamSystem.purchaseGame("u_0", DraftPurchase("g_0", CardInfo("a", 1234, LocalDate.now(), 123)))
        }
    }

    @Test
    fun purchaseGameWithWrongGameIdTest() {
        val steamSystem = SteamSystem(
            allGames,
            allDeveloper,
            allTags,
            mutableListOf()
        )
        val user = steamSystem.addNewUser(createDraftUser("a"))
        assertEquals(user.games.size, 0)
        assertFailsWith<NotFoundGame>("Game not found") {
            steamSystem.purchaseGame("u_0", DraftPurchase("g_1000", CardInfo("a", 1234, LocalDate.now(), 123)))
        }
    }

    @Test
    fun purchaseGameWithWrongUserIdTest() {
        val steamSystem = SteamSystem(
            allGames,
            allDeveloper,
            allTags,
            mutableListOf()
        )
        val user = steamSystem.addNewUser(createDraftUser("a"))
        assertEquals(user.games.size, 0)
        assertFailsWith<NotFoundUser>("User not found") {
            steamSystem.purchaseGame("u_100", DraftPurchase("g_1000", CardInfo("a", 1234, LocalDate.now(), 123)))
        }
    }

    @Test
    fun addOrRemoveFriendTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        val userA = steamSystem.addNewUser(createDraftUser("a"))
        val userB = steamSystem.addNewUser(createDraftUser("b"))

        assertEquals(userA.friends.size, 0)
        assertEquals(userB.friends.size, 0)

        steamSystem.addOrRemoveFriend("u_0", "u_1")

        assertEquals(userA.friends.size, 1)
        assertEquals(userB.friends.size, 1)

        assertEquals(userA.friends[0], userB)
        assertEquals(userB.friends[0], userA)
    }

    @Test
    fun addOrRemoveFriendTwoTimesTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        val userA = steamSystem.addNewUser(createDraftUser("a"))
        val userB = steamSystem.addNewUser(createDraftUser("b"))

        assertEquals(userA.friends.size, 0)
        assertEquals(userB.friends.size, 0)

        steamSystem.addOrRemoveFriend("u_0", "u_1")

        assertEquals(userA.friends.size, 1)
        assertEquals(userB.friends.size, 1)

        assertEquals(userA.friends[0], userB)
        assertEquals(userB.friends[0], userA)

        steamSystem.addOrRemoveFriend("u_0", "u_1")

        assertEquals(userA.friends.size, 0)
        assertEquals(userB.friends.size, 0)
    }

    @Test
    fun addOrRemoveFriendSameIdTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        val userA = steamSystem.addNewUser(createDraftUser("a"))
        val userB = steamSystem.addNewUser(createDraftUser("b"))

        assertEquals(userA.friends.size, 0)
        assertEquals(userB.friends.size, 0)

        assertFailsWith<UserException>("You cannot self-add") {
            steamSystem.addOrRemoveFriend("u_0", "u_0")
        }
    }

    @Test
    fun addOrRemoveFriendWithWrongUserIdTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        val userA = steamSystem.addNewUser(createDraftUser("a"))
        val userB = steamSystem.addNewUser(createDraftUser("b"))

        assertEquals(userA.friends.size, 0)
        assertEquals(userB.friends.size, 0)

        assertFailsWith<NotFoundUser>("User not found") {
            steamSystem.addOrRemoveFriend("u_1000", "u_0")
        }
    }

    @Test
    fun addOrRemoveFriendWithWrongFriendIdTest() {
        val steamSystem = SteamSystem(
            listOf(),
            listOf(),
            listOf(),
            mutableListOf()
        )
        val userA = steamSystem.addNewUser(createDraftUser("a"))
        val userB = steamSystem.addNewUser(createDraftUser("b"))

        assertEquals(userA.friends.size, 0)
        assertEquals(userB.friends.size, 0)

        assertFailsWith<NotFoundUser>("User not found") {
            steamSystem.addOrRemoveFriend("u_0", "u_1000")
        }
    }
}
