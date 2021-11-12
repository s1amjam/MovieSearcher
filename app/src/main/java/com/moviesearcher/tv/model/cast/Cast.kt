package com.moviesearcher.tv.model.cast

data class Cast(
    val adult: Boolean?,
    val gender: Int?,
    val id: Int?,
    val known_for_department: String?,
    val name: String?,
    val order: Int?,
    val original_name: String?,
    val popularity: Double?,
    val profile_path: String?,
    val roles: List<Role>?,
    val total_episode_count: Int?
)