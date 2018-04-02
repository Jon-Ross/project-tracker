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
        val viewModel = mapper.mapDataToView(lists)

        view.updateListsView(viewModel.projectsList, viewModel.librariesList)
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
        view.updateListsView(projectsVM, librariesVM)
    }

    override fun librariesListItemCheckboxClicked(checked: Boolean, id: UUID) {
        val projectsVM = view.getProjectsList()
        val librariesVM = view.getLibrariesList()
        if(checked) {
            val library = getItemFromId(id, librariesVM) ?: return
            val newProjectsVM = filterListWithItem(projectsVM, library)
            setAllViewModelsCheckboxesVisibility(newProjectsVM, false)
            view.updateProjectsListView(newProjectsVM)
            return
        } else {
            var filteredProjectsVM = mapper.mapDataToView(dataProvider.getCachedLists()).projectsList
            for(library in librariesVM) {
                if(library.selected) {
                    filteredProjectsVM = filterListWithItem(filteredProjectsVM, library)
                }
            }
            if(areAllCheckboxesDeselected(librariesVM)) {
                resetViewModelsListStates(filteredProjectsVM)
            } else {
                setAllViewModelsCheckboxesVisibility(filteredProjectsVM, false)
            }
            view.updateProjectsListView(filteredProjectsVM)
        }
    }

    private fun filterListWithItem(
            list: List<ListItemViewModel>, item: ListItemViewModel) : List<ListItemViewModel> {
        return list.filter {
            var filter = false
            for(con in it.connections) {
                if(con == item.id) {
                    filter = true
                    break
                }
            }
            filter
        }
    }

    private fun getItemFromId(id: UUID, list: List<ListItemViewModel>) : ListItemViewModel? {
        for(item in list) {
            if(item.id == id) {
                return item
            }
        }
        return null
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

    private fun areAllCheckboxesDeselected(list: List<ListItemViewModel>) : Boolean {
        for(item in list) {
            if(item.selected) return false
        }
        return true
    }

    private fun setAllViewModelsCheckboxesVisibility(list: List<ListItemViewModel>, show: Boolean) {
        for(item in list) {
            item.checkboxShowing = show
        }
    }

    override fun setView(view: MainScreenContract.View) {
        this.view = view
    }
}