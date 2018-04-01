package com.baddesigns.android.projects.models.view_models

import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
data class MainScreenViewModel(
        val projectsList: List<ListItemViewModel> = mutableListOf(),
        val librariesList: List<ListItemViewModel> = mutableListOf()) {

    fun deepCopy() : MainScreenViewModel {
        return copy(projectsList = deepCopyList(projectsList) as MutableList,
                librariesList = deepCopyList(librariesList) as MutableList)
    }

    private fun deepCopyList(list: List<ListItemViewModel>) : List<ListItemViewModel> {
        val listCopy = mutableListOf<ListItemViewModel>()
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