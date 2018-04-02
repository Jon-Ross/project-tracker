package com.baddesigns.android.projects.generaters

import com.baddesigns.android.projects.ItemType
import com.baddesigns.android.projects.models.data_models.ListItemModel

fun generateProjectsDataModelList() : MutableList<ListItemModel> {
    return mutableListOf(
            ListItemModel(name = "Projects", itemType = ItemType.PROJECT),
            ListItemModel(name = "Spoiler Free", itemType = ItemType.PROJECT),
            ListItemModel(name = "AI", itemType = ItemType.PROJECT)
    )
}

fun generateLibrariesDataModelList() : MutableList<ListItemModel> {
    return mutableListOf(
            ListItemModel(name = "RxJava", itemType = ItemType.LIBRARY),
            ListItemModel(name = "OkHTTP", itemType = ItemType.LIBRARY),
            ListItemModel(name = "Retrofit", itemType = ItemType.LIBRARY),
            ListItemModel(name = "Fuel", itemType = ItemType.LIBRARY),
            ListItemModel(name = "SQLite", itemType = ItemType.LIBRARY),
            ListItemModel(name = "Realm", itemType = ItemType.LIBRARY),
            ListItemModel(name = "Room", itemType = ItemType.LIBRARY),
            ListItemModel(name = "Anko", itemType = ItemType.LIBRARY),
            ListItemModel(name = "DBFlow", itemType = ItemType.LIBRARY),
            ListItemModel(name = "Firebase", itemType = ItemType.LIBRARY)
    )
}

fun connectAnyDataModels(item1: ListItemModel, item2: ListItemModel) {
    item1.connections.add(item2.id)
    item2.connections.add(item1.id)
}