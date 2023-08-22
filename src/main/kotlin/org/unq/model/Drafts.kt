package org.unq.model

import java.time.LocalDate

class DraftReview(
    val gameId: String,
    val isRecommended: Boolean,
    val text: String
)

class DraftPurchase(
    val gameId: String,
    val card: CardInfo,
)

class CardInfo(
    val cardHolderName: String,
    val number: Number,
    val expirationDate: LocalDate,
    val cvv: Number)

class DraftUser(
    val name: String,
    val email: String,
    val password: String,
    val image: String,
    val backgroundImage: String
)