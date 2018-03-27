package com.baddesigns.android.projects.mappers

import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.MainScreenModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import com.baddesigns.android.projects.models.view_models.MainScreenViewModel

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class MainScreenMapper {

    fun mapDataToView(model: MainScreenModel) : MainScreenViewModel {

        val projectsListViewModels: List<ListItemViewModel> =
                mapDataModelListToViewModelList(model.projectsList)
        val librariesListViewModels: List<ListItemViewModel> =
                mapDataModelListToViewModelList(model.librariesList)

        return MainScreenViewModel(projectsListViewModels, librariesListViewModels)
    }

    private fun mapDataModelListToViewModelList(list: List<ListItemModel>) :
            List<ListItemViewModel> {
        val viewModelList: MutableList<ListItemViewModel> = mutableListOf()
        list.forEach {
            viewModelList.add(ListItemViewModel(
                    name = it.name,
                    selected = it.selected,
                    connections = it.connections,
                    id = it.id)
            )
        }
        return viewModelList
    }
}