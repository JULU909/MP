package com.ltu.m7019e.moviedb.v24.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.sql.Struct


@Serializable
data class Book(
    @SerialName(value = "key")
    var key: String = "",

    @SerialName(value = "title")
    var title: String = "",

    @SerialName(value = "covers")
    var covers: List<Int>? = null,

    @SerialName(value = "publish_date")
    var publishDate: String = "",

    @SerialName(value = "number_of_pages")
    var numberOfPages: Int = 0,

    @SerialName(value = "description")
    var description: String = "",

    @SerialName(value = "subjects")
    var subjects: List<String>
)


// Used for one specific work
@Serializable
data class Work(
    @SerialName(value = "key")
    var key: String = "",

    @SerialName(value = "title")
    var title: String = "",

    @SerialName(value = "covers")
    var covers: List<Int>? = null,


    @SerialName(value = "subjects")
    var subjects: List<String> = listOf(),

    @SerialName(value = "authors")
    var authors: List<AuthorRole>,

    @SerialName(value = "first_publish_date")
    var firstPublishDate: String? = null
)
{
    val coverImage: Int
        get() = covers?.firstOrNull() ?: DEFAULT_PLACEHOLDER_IMAGE

    companion object {
        const val DEFAULT_PLACEHOLDER_IMAGE = 0 // Replace with your actual placeholder image resource ID
    }
}

// For many works, used in query
@Serializable
data class Works(
    @SerialName(value = "key")
    var key: String = "",

    @SerialName(value = "title")
    var title: String = "",

    @SerialName(value = "cover_i")
    var coverImage: Int? = null,

    @SerialName(value = "author_name")
    var authorName: List<String> = listOf(""),

    @SerialName(value = "author_key")
    var author_key: List<String> = listOf(""),



    @SerialName(value = "first_publish_year")
    var firstPublishYear: Int? = null,
)

// Local version of works
@Serializable
@Entity(tableName = "works")
data class WorksLocal(
    @PrimaryKey
    @SerialName(value = "key")
    var key: String = "",

    @SerialName(value = "title")
    var title: String = "",

    @SerialName(value = "cover_i")
    var coverImage: Int? = null,

    @SerialName(value = "author_name")
    var authorName: String = "",

    @SerialName(value = "first_publish_year")
    var firstPublishYear: Int? = null,
)

@Serializable
data class AuthorRole(
    @SerialName("author")
    val author: AuthorKey
)

@Serializable
data class AuthorKey(
    @SerialName("key")
    val key: String
)

@Serializable
data class Author(
    @SerialName(value = "name")
    var name: String,

    @SerialName(value = "photos")
    var photos: List<Int>? = listOf(),

    @SerialName(value = "birth_date")
    var birthDate: String? = null,

    @SerialName(value = "death_date")
    var deathDate: String? = null
)