package com.baddesigns.android.projects.generaters

import com.baddesigns.android.projects.models.data_models.ListItemModel

fun generateProjectsDataModelList() : MutableList<ListItemModel> {
    return mutableListOf(
            ListItemModel(name = "Projects"),
            ListItemModel(name = "Spoiler Free"),
            ListItemModel(name = "AI")
    )
}

fun generateLibrariesDataModelList() : MutableList<ListItemModel> {
    return mutableListOf(
            ListItemModel(name = "RxJava"),
            ListItemModel(name = "OkHTTP"),
            ListItemModel(name = "Retrofit"),
            ListItemModel(name = "Fuel"),
            ListItemModel(name = "SQLite"),
            ListItemModel(name = "Realm"),
            ListItemModel(name = "Room"),
            ListItemModel(name = "Anko"),
            ListItemModel(name = "DBFlow"),
            ListItemModel(name = "Firebase")
    )
}

fun connectAnyDataModels(item1: ListItemModel, item2: ListItemModel) {
    item1.connections.add(item2.id)
    item2.connections.add(item1.id)
}