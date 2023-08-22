package org.unq.model

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