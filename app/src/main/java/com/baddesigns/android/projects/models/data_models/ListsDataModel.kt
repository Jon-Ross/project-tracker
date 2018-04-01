package com.baddesigns.android.projects.models.data_models

import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
data class ListsDataModel(
        val projectsList: MutableList<ListItemModel> = mutableListOf(),
        val librariesList: MutableList<ListItemModel> = mutableListOf()) {

    fun deepCopy() : ListsDataModel {
        return copy(projectsList = deepCopyList(projectsList) as MutableList,
                librariesList = deepCopyList(librariesList) as MutableList)
    }

    private fun deepCopyList(list: List<ListItemModel>) : List<ListItemModel> {
        val listCopy = mutableListOf<ListItemModel>()
        for(project in list) {
            val connections = mutableSetOf<UUID>()
            for(con in project.connections) {
                connections.add(con)
            }
            listCopy.add(project.copy(connections = connections))
        }
        return listCopy
    }
}