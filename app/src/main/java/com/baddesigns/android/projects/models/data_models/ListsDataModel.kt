package com.baddesigns.android.projects.models.data_models

/**
 * Created by Jon-Ross on 25/03/2018.
 */
data class ListsDataModel(
        val projectsList: MutableList<ListItemModel> = mutableListOf(),
        val librariesList: MutableList<ListItemModel> = mutableListOf())