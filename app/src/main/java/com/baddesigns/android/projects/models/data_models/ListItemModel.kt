package com.baddesigns.android.projects.models.data_models

import com.baddesigns.android.projects.ItemType
import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
data class ListItemModel(
        val id: UUID = UUID.randomUUID(),
        val name: String = "",
        val connections: MutableSet<UUID> = mutableSetOf(),
        val itemType: ItemType = ItemType.PROJECT)