package org.unq.model

class Review(val id: String, val user: User, val game: Game, val isRecommended: Boolean, val text: String)