package com.example.data.model

enum class VideoSortOrder(val displayName: String) {
    NEWEST("Newest first"),
    OLDEST("Oldest first"),
    NAME_ASC("Name (A to Z)"),
    DURATION_DESC("Longest first"),
    DURATION_ASC("Shortest first")
}
