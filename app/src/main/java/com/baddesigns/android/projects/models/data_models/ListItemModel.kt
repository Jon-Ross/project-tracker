package com.baddesigns.android.projects.models.data_models

import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
data class ListItemModel(
        val id: UUID = UUID.randomUUID(),
        val name: String = "",
        val selected: Boolean = false,
        val connections: MutableSet<UUID> = mutableSetOf())