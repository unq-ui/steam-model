package org.unq.model

import java.time.LocalDate

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

enum class ESRB {
    everyone,
    everyone10plus,
    teen,
    mature17plus,
    adultsOnly,
    ratingPending
}