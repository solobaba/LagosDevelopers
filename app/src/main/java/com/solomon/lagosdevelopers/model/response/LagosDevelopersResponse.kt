package com.solomon.lagosdevelopers.model.response

import java.io.Serializable

data class LagosDevelopersResponse(
    val incomplete_results: Boolean? = false,
    val items: List<DevelopersItem> = listOf(),
    val total_count: Int? = 0
): Serializable

data class DevelopersItem(
    val avatar_url: String? = "N/A",
    val events_url: String? = "N/A",
    val followers_url: String? = "N/A",
    val following_url: String? = "N/A",
    val gists_url: String? = "N/A",
    val gravatar_id: String? = "N/A",
    val html_url: String? = "N/A",
    val id: Int? = 0,
    val login: String? = "N/A",
    val node_id: String? = "N/A",
    val organizations_url: String? = "N/A",
    val received_events_url: String? = "N/A",
    val repos_url: String? = "N/A",
    val score: Double? = 0.0,
    val site_admin: Boolean = false,
    val starred_url: String? = "N/A",
    val subscriptions_url: String? = "N/A",
    val type: String? = "N/A",
    val url: String? = "N/A"
): Serializable