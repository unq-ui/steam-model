package org.unq.model

class Requirement(
    os: List<String> = listOf(),
    processor: List<String> = listOf(),
    memory: Int = 0,
    graphics: List<String> = listOf(),
    directX: String = "",
    storage: Int = 0
)