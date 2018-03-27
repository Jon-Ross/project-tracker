package com.baddesigns.android.projects.models.view_models

import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
data class ListItemViewModel(
        val name: String = "",
        var selected: Boolean = false,
        var checkboxShowing: Boolean = true,
        var hidden: Boolean = false,
        val connections: MutableSet<UUID> = mutableSetOf(),
        val id: UUID = UUID.randomUUID()
)