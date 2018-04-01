package com.baddesigns.android.projects.helpers.generators.MainScreen

import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.ListsDataModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import com.baddesigns.android.projects.models.view_models.MainScreenViewModel

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class ModelGenerator {

    fun generateConnectedDataModel() : ListsDataModel {
        val projectsList: MutableList<ListItemModel> = generateProjectsDataModelList()
        val librariesList: MutableList<ListItemModel> = generateLibrariesDataModelList()
        connectAnyDataModels(projectsList[0], librariesList[5])
        connectAnyDataModels(projectsList[0], librariesList[6])
        connectAnyDataModels(projectsList[1], librariesList[2])
        connectAnyDataModels(projectsList[1], librariesList[3])
        connectAnyDataModels(projectsList[1], librariesList[5])

        return ListsDataModel(projectsList, librariesList)
    }

    fun generateDisconnectedDataModel() : ListsDataModel {
        val projectsList: MutableList<ListItemModel> = generateProjectsDataModelList()
        val librariesList: MutableList<ListItemModel> = generateLibrariesDataModelList()

        return ListsDataModel(projectsList, librariesList)
    }

    fun generateConnectedViewModel() : MainScreenViewModel {
        val projectsListViewModels: MutableList<ListItemViewModel> = generateProjectsViewModelList()
        val librariesListViewModels: MutableList<ListItemViewModel> = generateLibrariesViewModelList()
        connectAnyViewModels(projectsListViewModels[0], librariesListViewModels[5])
        connectAnyViewModels(projectsListViewModels[0], librariesListViewModels[6])
        connectAnyViewModels(projectsListViewModels[1], librariesListViewModels[2])
        connectAnyViewModels(projectsListViewModels[1], librariesListViewModels[3])
        connectAnyViewModels(projectsListViewModels[1], librariesListViewModels[5])

        return MainScreenViewModel(projectsListViewModels, librariesListViewModels)
    }

    fun generateDisconnectedViewModel() : MainScreenViewModel {
        val projectsListViewModels: MutableList<ListItemViewModel> = generateProjectsViewModelList()
        val librariesListViewModels: MutableList<ListItemViewModel> = generateLibrariesViewModelList()

        return MainScreenViewModel(projectsListViewModels, librariesListViewModels)
    }
}

fun connectAnyDataModels(item1: ListItemModel, item2: ListItemModel) {
    item1.connections.add(item2.id)
    item2.connections.add(item1.id)
}

fun connectAnyViewModels(item1: ListItemViewModel, item2: ListItemViewModel) {
    item1.connections.add(item2.id)
    item2.connections.add(item1.id)
}

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

fun generateProjectsViewModelList() : MutableList<ListItemViewModel> {
    return mutableListOf(
            ListItemViewModel("Projects", true),
            ListItemViewModel("Spoiler Free"),
            ListItemViewModel("AI")
    )
}

fun generateLibrariesViewModelList() : MutableList<ListItemViewModel> {
    return mutableListOf(
            ListItemViewModel(name = "RxJava"),
            ListItemViewModel(name = "OkHTTP"),
            ListItemViewModel(name = "Retrofit", selected = true),
            ListItemViewModel(name = "Fuel"),
            ListItemViewModel(name = "SQLite"),
            ListItemViewModel(name = "Realm", selected = true),
            ListItemViewModel(name = "Room"),
            ListItemViewModel(name = "Anko"),
            ListItemViewModel(name = "DBFlow"),
            ListItemViewModel(name = "Firebase")
    )
}