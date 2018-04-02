package com.baddesigns.android.projects.models.view_models

import com.baddesigns.android.projects.ItemType
import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
data class ListItemViewModel(
        val name: String = "",
        var selected: Boolean = false,
        var checkboxShowing: Boolean = true,
        val connections: MutableSet<UUID> = mutableSetOf(),
        val id: UUID = UUID.randomUUID(),
        val itemType: ItemType = ItemType.PROJECT
)