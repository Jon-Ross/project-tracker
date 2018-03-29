package com.baddesigns.android.projects

import com.baddesigns.android.projects.data_providers.IMainScreenDataProvider
import com.baddesigns.android.projects.helpers.generators.MainScreen.ModelGenerator
import com.baddesigns.android.projects.mappers.MainScreenMapper
import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.ListsDataModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
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

    private val modelGenerator = ModelGenerator()
    private lateinit var dataModel: ListsDataModel
    private val modelMapper = MainScreenMapper()

    @Mock
    private lateinit var dataProvider: IMainScreenDataProvider
    @Mock
    private lateinit var view: MainScreenContract.View
    @Mock
    private lateinit var mapper: MainScreenMapper
    private lateinit var allProjects: MutableList<ListItemModel>
    private lateinit var allLibraries: MutableList<ListItemModel>

    private lateinit var presenter: MainScreenPresenter

    @Before
    fun setUp() {
        initMocks(this)

        dataModel = modelGenerator.generateConnectedDataModel()

        presenter = MainScreenPresenter(dataProvider, mapper)
        presenter.setView(view)

        `when`(dataProvider.fetchLists()).thenReturn(dataModel)
        allProjects = dataModel.projectsList
        allLibraries = dataModel.librariesList
    }

    @Test
    fun start() {
        presenter.start()

        verify(dataProvider).fetchLists(presenter)
    }

    private val listsDataModel: ListsDataModel
        get() {
            val dataModel = modelGenerator.generateConnectedDataModel()
            return dataModel
        }

    @Test
    fun onListsRetrieved() {
        val dataModel = modelGenerator.generateConnectedDataModel()

        val viewModel = modelGenerator.generateDisconnectedViewModel()

        `when`(mapper.mapDataToView(dataModel)).thenReturn(viewModel)

        presenter.onListsRetrieved(dataModel)

        verify(view).updateProjectsListView(viewModel.projectsList)
        verify(view).updateLibrariesListView(viewModel.librariesList)
    }

    @Test
    fun projectsHeaderClicked_toShowing() {
        presenter.projectsHeaderArrowClicked(false)

        verify(view).changeProjectsHeaderArrow(true)
        verify(view).changeProjectsListVisibility(true)
    }

    @Test
    fun projectsHeaderClicked_toHidden() {
        presenter.projectsHeaderArrowClicked(true)

        verify(view).changeProjectsHeaderArrow(false)
        verify(view).changeProjectsListVisibility(false)
    }

    @Test
    fun librariesHeaderClicked_toShowing() {
        presenter.librariesHeaderArrowClicked(false)

        verify(view).changeLibrariesHeaderArrow(true)
        verify(view).changeLibrariesListVisibility(true)
    }

    @Test
    fun librariesHeaderClicked_toHidden() {
        presenter.librariesHeaderArrowClicked(true)

        verify(view).changeLibrariesHeaderArrow(false)
        verify(view).changeLibrariesListVisibility(false)
    }

    @Test
    fun setView() {
        val newView = mock(MainScreenContract.View::class.java)

        presenter.setView(newView)

        assertEquals(newView, presenter.view)
    }

    // TODO: need to make a copy of the lists as otherwise when changing their states, they
    // change them in the implementation too
    @Test
    fun projectsListItemCheckboxClicked_checked_filtersProjectsAndLibraries() {
        val mappedProjectVM = modelMapper.mapDataModelToViewModel(allProjects[0])
        `when`(mapper.mapDataModelToViewModel(allProjects[0])).thenReturn(mappedProjectVM)

        val filteredProjectsVM: List<ListItemViewModel> = listOf(mappedProjectVM)
        filteredProjectsVM[0].checkboxShowing = true
        filteredProjectsVM[0].selected = true

        val filteredLibs = listOf(allLibraries[5], allLibraries[6])
        val filteredLibrariesVM: List<ListItemViewModel> =
                modelMapper.mapDataModelListToViewModelList(filteredLibs)
        filteredLibrariesVM[0].checkboxShowing = false
        filteredLibrariesVM[1].checkboxShowing = false
        `when`(mapper.mapDataModelListToViewModelList(filteredLibs)).thenReturn(filteredLibrariesVM)

        presenter.projectsListItemCheckboxClicked(true, allProjects[0].id)

        verify(view).updateProjectsListView(filteredProjectsVM)
        verify(view).updateLibrariesListView(filteredLibrariesVM)
    }

    // TODO: need to make a copy of the lists as otherwise when changing their states, they
    // change them in the implementation too
    @Test
    fun projectsListItemCheckboxClicked_unchecked_removesAllFilters() {
        val projectsVM = modelMapper.mapDataModelListToViewModelList(allProjects)
        val librariesVM: List<ListItemViewModel> =
                modelMapper.mapDataModelListToViewModelList(allLibraries)
        `when`(mapper.mapDataModelListToViewModelList(allProjects)).thenReturn(projectsVM)
        `when`(mapper.mapDataModelListToViewModelList(allLibraries)).thenReturn(librariesVM)

        resetViewModelsListStates(projectsVM)
        resetViewModelsListStates(librariesVM)

        presenter.projectsListItemCheckboxClicked(false, allProjects[0].id)

        verify(view).updateProjectsListView(projectsVM)
        verify(view).updateLibrariesListView(librariesVM)
    }

    // TODO: need to make a copy of the lists as otherwise when changing their states, they
    // change them in the implementation too
    @Test
    fun librariesListItemCheckboxClicked_checked_onlyOneCheck_hideProjectsCheckboxes() {
        val projectsVM = modelMapper.mapDataModelListToViewModelList(allProjects)
        `when`(view.getProjectsList()).thenReturn(projectsVM)

        setVisibilityAllViewModelsCheckboxes(projectsVM, false)

        presenter.librariesListItemCheckboxClicked(true, UUID.randomUUID())

        verify(view).updateProjectsListView(projectsVM)
    }

    // TODO: need to make a copy of the lists as otherwise when changing their states, they
    // change them in the implementation too
    @Test
    fun librariesListItemCheckboxClicked_unchecked_noChecks_showProjectsCheckboxes() {
        val projectsVM = modelMapper.mapDataModelListToViewModelList(allProjects)
        val librariesVM = modelMapper.mapDataModelListToViewModelList(allLibraries)
        setCheckedAllViewModelsCheckboxes(librariesVM, false)

        `when`(view.getProjectsList()).thenReturn(projectsVM)
        `when`(view.getLibrariesList()).thenReturn(librariesVM)

        setVisibilityAllViewModelsCheckboxes(projectsVM, true)

        presenter.librariesListItemCheckboxClicked(false, UUID.randomUUID())

        verify(view).updateProjectsListView(projectsVM)
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