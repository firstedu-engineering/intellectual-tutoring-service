package com.firsteducation.marsladder.its.client.domain

data class Question (
    val id: String,
    val body: String,
    val option: List<Option>,
    val difficulty: Double
)

data class Option(
    val value: String,
    val correct: Boolean,
)