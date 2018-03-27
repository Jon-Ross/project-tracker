package com.baddesigns.android.projects.helpers.generators.MainScreen

import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.MainScreenModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import com.baddesigns.android.projects.models.view_models.MainScreenViewModel

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class ModelGenerator {

    fun generateDataModel() : MainScreenModel {
        val projectsList: MutableList<ListItemModel> = generateProjectsDataModelList()
        val librariesList: MutableList<ListItemModel> = generateLibrariesDataModelList()

        return MainScreenModel(projectsList, librariesList)
    }

    fun generateViewModel() : MainScreenViewModel {
        val projectsListViewModels: MutableList<ListItemViewModel> = generateProjectsViewModelList()
        val librariesListViewModels: MutableList<ListItemViewModel> = generateLibrariesViewModelList()

        return MainScreenViewModel(projectsListViewModels, librariesListViewModels)
    }

    fun generateProjectsDataModelList() : MutableList<ListItemModel> {
        return mutableListOf(
                ListItemModel(name = "Projects", selected = true),
                ListItemModel(name = "Spoiler Free"),
                ListItemModel(name = "AI")
        )
    }

    fun generateLibrariesDataModelList() : MutableList<ListItemModel> {
        return mutableListOf(
                ListItemModel(name = "RxJava"),
                ListItemModel(name = "Room", selected = true),
                ListItemModel(name = "Retrofit"),
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
                ListItemViewModel("RxJava"),
                ListItemViewModel("Room", true),
                ListItemViewModel("Retrofit"),
                ListItemViewModel("Firebase")
        )
    }
}