package com.example.wordwave.data.translate

data class DictionaryResponse(
    val def: List<Definition>
)

data class Definition(
    val text: String,
    val pos: String?,
    val tr: List<DictionaryTranslation>
)

data class DictionaryTranslation(
    val text: String,
    val syn: List<Synonym>?,
    val mean: List<Meaning>?,
)

data class Synonym(
    val text: String,
    val pos: String?
)

data class Meaning(
    val text: String
)

