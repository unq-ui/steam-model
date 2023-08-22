# Steam

## How to use

```kotlin
val steamSystem = initSteamSystem()
```

## SteamSystem

```kotlin
class SteamSystem(
    val games: List<Game>,
    val developers: List<Developer>,
    val tags: List<Tag>,
    val users: MutableList<User>,
) {

    /**
     * Add a new user to the system.
     *
     * @param user The user to add.
     * @return The newly created user.
     * @throws UserException If the email address is already in use.
     */
    fun addNewUser(user: DraftUser): User

    /**
     * Get a tag by its ID.
     *
     * @param id The ID of the tag.
     * @return The tag with the given ID.
     * @throws NotFoundTag If the tag with the given ID does not exist.
     */
    fun getTag(id: String): Tag

    /**
     * Get a user by its ID.
     *
     * @param id The ID of the user.
     * @return The user with the given ID.
     * @throws NotFoundUser If the user with the given ID does not exist.
     */
    fun getUser(id: String): User

    /**
     * Get a game by its ID.
     *
     * @param id The ID of the game.
     * @return The game with the given ID.
     * @throws NotFoundGame If the game with the given ID does not exist.
     */
    fun getGame(id: String): Game

    /**
     * Get a developer by its ID.
     *
     * @param id The ID of the developer.
     * @return The developer with the given ID.
     * @throws NotFoundDeveloper If the developer with the given ID does not exist.
     */
    fun getDeveloper(id: String): Developer

    /**
     * Get the reviews written by the user with the given ID.
     *
     * @param userId The ID of the user.
     * @return The list of reviews written by the user.
     * @throws NotFoundUser If the user with the given ID does not exist.
     */
    fun getUserReviews(userId: String): List<Review>

    /**
     * Get a list of recommended games.
     *
     * The recommended games are the games that have the most reviews that are marked as recommended.
     *
     * @return A list of recommended games.
     */
    fun getRecommendedGames(): List<Game>

    /**
     * Get a list of games.
     *
     * @param page The page number.
     * @return A list of games.
     * @throws PageException If the page number is less than 1.
     */
    fun getGames(page: Int = 1): PageInfo<Game>

    /**
     * Get a list of games by tag.
     *
     * @param tagId The ID of the tag.
     * @param page The page number.
     * @return A list of games.
     * @throws NotFoundTag If the tag with the given ID does not exist.
     * @throws PageException If the page number is less than 1.
     */
    fun getGamesByTag(tagId: String, page: Int = 1): PageInfo<Game>

    /**
     * Get a list of games by developer.
     *
     * @param developerId The ID of the developer.
     * @param page The page number.
     * @return A list of games.
     * @throws NotFoundDeveloper If the developer with the given ID does not exist.
     * @throws PageException If the page number is less than 1.
     */
    fun getGamesByDeveloper(developerId: String, page: Int = 1): PageInfo<Game>

    /**
     * Search for games by name.
     *
     * @param name The name of the game.
     * @param page The page number.
     * @return A list of games.
     * @throws PageException If the page number is less than 1.
     */
    fun searchGame(name: String, page: Int = 1): PageInfo<Game>

    /**
     * Search for users by name.
     *
     * @param name The name of the user.
     * @param page The page number.
     * @return A list of users.
     * @throws PageException If the page number is less than 1.
     */
    fun searchUser(name: String, page: Int = 1): PageInfo<User>

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
    fun addReview(userId: String, draftReview: DraftReview): Game

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
    fun purchaseGame(userId: String, draftPurchase: DraftPurchase): User

    /**
     * Add or remove a friend.
     *
     * @param userId The ID of the user who is adding or removing the friend.
     * @param friendId The ID of the friend.
     * @return The user who added or removed the friend.
     * @throws NotFoundUser If the user or the friend with the given ID does not exist.
     * @throws UserException If the user is trying to self-add.
     */
    fun addOrRemoveFriend(userId: String, friendId: String): User
```

## Model

```kotlin
class User(
    val id: String,
    val email: String,
    val password: String,
    val name: String,
    val image: String,
    val backgroundImage: String,
    val games: MutableList<Game>,
    val friends: MutableList<User>
)
```

```kotlin
class Game(
    val id: String,
    val name: String,
    val description: String,
    val mainImage: Image,
    val multimedia: List<Image>,
    val tags: List<Tag>,
    val price: Price,
    val requirement: Requirement,
    val relatedGames: MutableList<Game>,
    val developer: Developer,
    val releaseDate: LocalDate,
    val reviews: MutableList<Review>,
    val esrb: ESRB,
    val website: String
)
```

```kotlin
class Tag(
    val id: String,
    val name: String,
    val image: Image
)
```

```kotlin
class Review(
    val id: String,
    val user: User,
    val game: Game,
    val isRecommended: Boolean,
    val text: String
)
```

```kotlin
class Requirement(
    os: List<String> = listOf(),
    processor: List<String> = listOf(),
    memory: Int = 0,
    graphics: List<String> = listOf(),
    directX: String = "",
    storage: Int = 0
)
```

```kotlin
class Price(
    val currency: String,
    val amount: Double
)
```

```kotlin
class Image(
    val src: String
)
```

```kotlin
enum class ESRB {
    everyone,
    everyone10plus,
    teen,
    mature17plus,
    adultsOnly,
    ratingPending
}
```

```kotlin
class Developer(
    val id: String,
    val name: String,
    val image: Image
)
```

## Pagination

```kotlin
class PageInfo<E>(
    val currentPage: Int,
    val list: List<E>,
    val amountOfElements: Int,
    val amountOfPages: Int
)
```

## Drafts

```kotlin
class DraftReview(
    val gameId: String,
    val isRecommended: Boolean,
    val text: String
)
```

```kotlin
class DraftPurchase(
    val gameId: String,
    val card: CardInfo,
)
```

```kotlin
class CardInfo(
    val cardHolderName: String,
    val number: Number,
    val expirationDate: LocalDate,
    val cvv: Number)
```

```kotlin
class DraftUser(
    val name: String,
    val email: String,
    val password: String,
    val image: String,
    val backgroundImage: String
)
```