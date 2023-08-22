package org.unq.model

class IdGenerator {
    private var currentUserId = -1
    private var currentReviewId = -1

    fun nextUserId(): String = "u_${++currentUserId}"
    fun nextReviewId(): String = "r_${++currentReviewId}"
}