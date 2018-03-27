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