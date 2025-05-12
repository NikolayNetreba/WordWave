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
    val pos: String?,
    val syn: List<Synonym>?,
    val mean: List<Meaning>?,
    val ex: List<Example>?
)

data class Synonym(
    val text: String,
    val pos: String?
)

data class Meaning(
    val text: String
)

data class Example(
    val text: String,
    val tr: List<ExampleTranslation>
)

data class ExampleTranslation(
    val text: String
)
