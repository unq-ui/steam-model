package org.unq.model

class NotFoundUser: Exception("User not found")

class NotFoundDeveloper: Exception("Developer not found")

class NotFoundGame: Exception("Game not found")

class NotFoundTag: Exception("Tag not found")

class ReviewException(msg: String): Exception(msg)

class UserException(msg: String): Exception(msg)

class PurchaseException(msg: String): Exception(msg)

class PageException(msg: String): Exception(msg)