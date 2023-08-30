package org.unq.model

class Requirement(
    val os: List<String> = listOf(),
    val processor: List<String> = listOf(),
    val memory: Int = 0,
    val graphics: List<String> = listOf(),
    val directX: String = "",
    val storage: Int = 0
)