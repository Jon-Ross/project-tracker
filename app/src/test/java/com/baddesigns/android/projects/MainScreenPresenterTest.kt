package com.baddesigns.android.projects

import com.baddesigns.android.projects.data_providers.IMainScreenDataProvider
import com.baddesigns.android.projects.helpers.generators.MainScreen.ModelGenerator
import com.baddesigns.android.projects.mappers.MainScreenMapper
import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.ListsDataModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import com.baddesigns.android.projects.models.view_models.MainScreenViewModel
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class MainScreenPresenterTest {

    @Mock
    private lateinit var dataProvider: IMainScreenDataProvider
    @Mock
    private lateinit var view: MainScreenContract.View
    @Mock
    private lateinit var mockMapper: MainScreenMapper

    private lateinit var dataModel: ListsDataModel
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var allProjects: MutableList<ListItemModel>
    private lateinit var allLibraries: MutableList<ListItemModel>
    private val modelGenerator = ModelGenerator()
    private val modelMapper = MainScreenMapper()

    private lateinit var presenter: MainScreenPresenter

    @Before
    fun setUp() {
        initMocks(this)

        dataModel = modelGenerator.generateConnectedDataModel()
        viewModel = modelMapper.mapDataToView(dataModel)

        presenter = MainScreenPresenter(dataProvider, mockMapper)
        presenter.setView(view)

        `when`(dataProvider.fetchLists()).thenReturn(dataModel.copy())
        `when`(dataProvider.getCachedLists()).thenReturn(dataModel.copy())
        allProjects = dataModel.projectsList
        allLibraries = dataModel.librariesList
    }

    @Test
    fun start() {
        presenter.start()

        verify(dataProvider).fetchLists(presenter)
    }

    @Test
    fun onListsRetrieved() {
        `when`(mockMapper.mapDataToView(dataModel)).thenReturn(viewModel)

        presenter.onListsRetrieved(dataModel)

        verify(view).updateListsView(viewModel.projectsList, viewModel.librariesList)
    }

    @Test
    fun setView() {
        val newView = mock(MainScreenContract.View::class.java)

        presenter.setView(newView)

        assertEquals(newView, presenter.view)
    }

    @Test
    fun projectsListItemCheckboxClicked_checked_filtersProjectsAndLibraries() {
        val mappedProjectVM = modelMapper.mapDataModelToViewModel(allProjects[0])

        `when`(mockMapper.mapDataModelToViewModel(allProjects[0])).thenReturn(mappedProjectVM)

        val filteredProjectsVM = listOf(mappedProjectVM)
        filteredProjectsVM[0].checkboxShowing = true
        filteredProjectsVM[0].selected = true

        val filteredLibs = listOf(allLibraries[5], allLibraries[6])
        val filteredLibrariesVM = modelMapper.mapDataModelListToViewModelList(filteredLibs)
        filteredLibrariesVM[0].checkboxShowing = false
        filteredLibrariesVM[1].checkboxShowing = false
        `when`(mockMapper.mapDataModelListToViewModelList(filteredLibs)).thenReturn(filteredLibrariesVM)

        presenter.projectsListItemCheckboxClicked(true, allProjects[0].id)

        verify(view).updateListsView(filteredProjectsVM, filteredLibrariesVM)
    }

    @Test
    fun projectsListItemCheckboxClicked_unchecked_removesAllFilters() {
        val projectsVM = modelMapper.mapDataModelListToViewModelList(allProjects)
        val librariesVM = modelMapper.mapDataModelListToViewModelList(allLibraries)

        `when`(mockMapper.mapDataModelListToViewModelList(allProjects)).thenReturn(projectsVM)
        `when`(mockMapper.mapDataModelListToViewModelList(allLibraries)).thenReturn(librariesVM)

        val projectsVMFinalState = copyVMList(projectsVM)
        val librariesVMFinalState = copyVMList(librariesVM)
        resetViewModelsListStates(projectsVMFinalState)
        resetViewModelsListStates(librariesVMFinalState)

        presenter.projectsListItemCheckboxClicked(false, allProjects[0].id)

        verify(view).updateListsView(projectsVMFinalState, librariesVMFinalState)
    }

    @Test
    fun librariesListItemCheckboxClicked_checked_1_hideProjectsCheckboxes_andFilterProjects() {
        val givenProjectsVM = modelMapper.mapDataModelListToViewModelList(allProjects)
        val givenLibrariesVM = modelMapper.mapDataModelListToViewModelList(allLibraries)
        givenLibrariesVM[5].selected = true

        `when`(view.getProjectsList()).thenReturn(givenProjectsVM)
        `when`(view.getLibrariesList()).thenReturn(givenLibrariesVM)

        val projectsVMFinalState = copyVMList(givenProjectsVM)
        projectsVMFinalState.removeAt(2)
        setVisibilityAllViewModelsCheckboxes(projectsVMFinalState, false)

        presenter.librariesListItemCheckboxClicked(true, allLibraries[5].id)

        verify(view).updateProjectsListView(projectsVMFinalState)
    }

    @Test
    fun librariesListItemCheckboxClicked_checked_2_hideProjectsCheckboxes_andFilterProjects() {
        val givenProjectsVM = modelMapper.mapDataModelListToViewModelList(allProjects) as MutableList
        givenProjectsVM.removeAt(2)
        val givenLibrariesVM = modelMapper.mapDataModelListToViewModelList(allLibraries)
        givenLibrariesVM[2].selected = true
        givenLibrariesVM[5].selected = true
        setVisibilityAllViewModelsCheckboxes(givenProjectsVM, false)

        `when`(view.getProjectsList()).thenReturn(givenProjectsVM)
        `when`(view.getLibrariesList()).thenReturn(givenLibrariesVM)

        val projectsVMFinalState = listOf(givenProjectsVM[1])

        presenter.librariesListItemCheckboxClicked(true, allLibraries[2].id)

        verify(view).updateProjectsListView(projectsVMFinalState)
    }

    @Test
    fun librariesListItemCheckboxClicked_unchecked_0_showProjectsCheckboxes_removeProjectFilters() {
        val givenProjectsVM = modelMapper.mapDataModelListToViewModelList(allProjects) as MutableList
        val finalProjectsVM = copyVMList(givenProjectsVM)
        givenProjectsVM.removeAt(2)
        setVisibilityAllViewModelsCheckboxes(givenProjectsVM, false)
        val givenLibrariesVM = modelMapper.mapDataModelListToViewModelList(allLibraries)
        setCheckedAllViewModelsCheckboxes(givenLibrariesVM, false)

        `when`(mockMapper.mapDataToView(dataModel)).thenReturn(viewModel)
        `when`(view.getProjectsList()).thenReturn(givenProjectsVM)
        `when`(view.getLibrariesList()).thenReturn(givenLibrariesVM)

        setVisibilityAllViewModelsCheckboxes(finalProjectsVM, true)

        presenter.librariesListItemCheckboxClicked(false, allLibraries[5].id)

        verify(view).updateProjectsListView(finalProjectsVM)
    }

    @Test
    fun librariesListItemCheckboxClicked_unchecked_1_filterProjects() {
        val givenProjectsVM = viewModel.deepCopy().projectsList as MutableList
        val finalProjectsVM = copyVMList(givenProjectsVM)
        givenProjectsVM.removeAt(2)
        givenProjectsVM.removeAt(0)
        val givenLibrariesVM = viewModel.librariesList
        givenLibrariesVM[5].selected = true

        `when`(mockMapper.mapDataToView(dataModel)).thenReturn(viewModel.deepCopy())
        `when`(view.getProjectsList()).thenReturn(givenProjectsVM)
        `when`(view.getLibrariesList()).thenReturn(givenLibrariesVM)

        finalProjectsVM.removeAt(2)
        setVisibilityAllViewModelsCheckboxes(finalProjectsVM, false)

        presenter.librariesListItemCheckboxClicked(false, allLibraries[2].id)

        verify(view).updateProjectsListView(finalProjectsVM)
    }

    private fun deepCopyModel(model: ListsDataModel) : ListsDataModel {
        return model.copy(projectsList = deepCopyList(model.projectsList) as MutableList,
                librariesList = deepCopyList(model.librariesList) as MutableList)
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

    private fun copyVMList(list: List<ListItemViewModel>) : MutableList<ListItemViewModel> {
        val listCopy: MutableList<ListItemViewModel> = mutableListOf()
        for(item in list) {
            listCopy.add(item.copy(item.name, item.selected, item.checkboxShowing,
                    id = item.id, connections = item.connections))
        }
        return listCopy
    }

    private fun resetViewModelsListStates(list: List<ListItemViewModel>) {
        for(item in list) {
            item.checkboxShowing = true
            item.selected = false
        }
    }

    private fun setVisibilityAllViewModelsCheckboxes(list: List<ListItemViewModel>, show: Boolean) {
        for(item in list) {
            item.checkboxShowing = show
        }
    }

    private fun setCheckedAllViewModelsCheckboxes(list: List<ListItemViewModel>, selected: Boolean) {
        for(item in list) {
            item.selected = selected
        }
    }
}