package com.baddesigns.android.projects

import com.baddesigns.android.projects.data_providers.IMainScreenDataProvider
import com.baddesigns.android.projects.mappers.MainScreenMapper
import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.ListsDataModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class MainScreenPresenter(private val dataProvider: IMainScreenDataProvider,
                          private val mapper: MainScreenMapper) :
        MainScreenContract.Presenter, IMainScreenDataProvider.Callback {

    internal lateinit var view: MainScreenContract.View

    override fun start() {
        dataProvider.fetchLists(this)
    }

    override fun onListsRetrieved(lists: ListsDataModel) {
        val dataModel = mapper.mapDataToView(lists)

        view.updateProjectsListView(dataModel.projectsList)
        view.updateLibrariesListView(dataModel.librariesList)
    }

    override fun projectsHeaderArrowClicked(contentsWereShowing: Boolean) {
        val toShowing = !contentsWereShowing
        view.changeProjectsHeaderArrow(toShowing)
        view.changeProjectsListVisibility(toShowing)
    }

    override fun librariesHeaderArrowClicked(contentsWereShowing: Boolean) {
        val toShowing = !contentsWereShowing
        view.changeLibrariesHeaderArrow(toShowing)
        view.changeLibrariesListVisibility(toShowing)
    }

    override fun projectsListItemCheckboxClicked(checked: Boolean, id: UUID) {
        val dataModel = dataProvider.fetchLists()
        val projectsList = dataModel.projectsList
        val librariesList = dataModel.librariesList

        val projectsVM: List<ListItemViewModel>
        val librariesVM: List<ListItemViewModel>
        if(checked) {
            val selectedProject: ListItemViewModel
            val pos = getItemPosition(id, projectsList)
            if (pos >= 0) {
                selectedProject = mapper.mapDataModelToViewModel(projectsList[pos])
                selectedProject.checkboxShowing = true
                selectedProject.selected = true
            } else return
            projectsVM = listOf(selectedProject)

            val filteredLibs = mutableListOf<ListItemModel>()
            for (lib in librariesList) {
                for (con in lib.connections) {
                    if (con == id) {
                        filteredLibs.add(lib)
                        break
                    }
                }
            }
            librariesVM = mapper.mapDataModelListToViewModelList(filteredLibs)
            for (libVM in librariesVM) {
                libVM.checkboxShowing = false
            }
        } else {
            projectsVM = mapper.mapDataModelListToViewModelList(projectsList)
            librariesVM = mapper.mapDataModelListToViewModelList(librariesList)
            resetViewModelsListStates(projectsVM)
            resetViewModelsListStates(librariesVM)
        }
        view.updateProjectsListView(projectsVM)
        view.updateLibrariesListView(librariesVM)
    }

    private fun resetViewModelsListStates(list: List<ListItemViewModel>) {
        for(item in list) {
            item.checkboxShowing = true
            item.selected = false
        }
    }

    private fun getItemPosition(id: UUID, list: List<ListItemModel>) : Int {
        for((i, item) in list.withIndex()) {
            if(item.id == id) {
                return i
            }
        }
        return -1
    }

    override fun librariesListItemCheckboxClicked(checked: Boolean, id: UUID) {
    }

    override fun setView(view: MainScreenContract.View) {
        this.view = view
    }
}