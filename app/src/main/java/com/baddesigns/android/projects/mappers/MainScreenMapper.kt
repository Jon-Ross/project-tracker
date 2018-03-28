package com.baddesigns.android.projects.mappers

import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.ListsDataModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import com.baddesigns.android.projects.models.view_models.MainScreenViewModel

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class MainScreenMapper {

    fun mapDataToView(model: ListsDataModel) : MainScreenViewModel {

        val projectsListViewModels: List<ListItemViewModel> =
                mapDataModelListToViewModelList(model.projectsList)
        val librariesListViewModels: List<ListItemViewModel> =
                mapDataModelListToViewModelList(model.librariesList)

        return MainScreenViewModel(projectsListViewModels, librariesListViewModels)
    }

    fun mapDataModelListToViewModelList(list: List<ListItemModel>) :
            List<ListItemViewModel> {
        val viewModelList: MutableList<ListItemViewModel> = mutableListOf()
        list.forEach {
            viewModelList.add(mapDataModelToViewModel(it))
        }
        return viewModelList
    }

    fun mapDataModelToViewModel(dataModel: ListItemModel) : ListItemViewModel {
        return ListItemViewModel(
                name = dataModel.name,
                id = dataModel.id
        )
    }
}