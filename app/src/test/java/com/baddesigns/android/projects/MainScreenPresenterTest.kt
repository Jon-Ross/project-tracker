package com.baddesigns.android.projects

import com.baddesigns.android.projects.data_providers.IMainScreenDataProvider
import com.baddesigns.android.projects.helpers.generators.MainScreen.ModelGenerator
import com.baddesigns.android.projects.mappers.MainScreenMapper
import com.baddesigns.android.projects.models.view_models.MainScreenViewModel
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class MainScreenPresenterTest {

    private val modelGenerator = ModelGenerator()

    @Mock
    private lateinit var dataProvider: IMainScreenDataProvider
    @Mock
    private lateinit var view: MainScreenContract.View
    @Mock
    private lateinit var mapper: MainScreenMapper

    private lateinit var presenter: MainScreenPresenter

    @Before
    fun setUp() {
        initMocks(this)

        presenter = MainScreenPresenter(dataProvider, mapper)
        presenter.setView(view)
    }

    @Test
    fun start() {
        presenter.start()

        verify(dataProvider).fetchLists(presenter)
    }

    @Test
    fun onListsRetrieved() {

        val dataModel = modelGenerator.generateDataModel()
        dataModel.librariesList

        val projectsListViewModel = modelGenerator.generateProjectsViewModelList()
        val librariesListViewModel = modelGenerator.generateLibrariesViewModelList()
        val viewModel = MainScreenViewModel(projectsListViewModel, librariesListViewModel)

        `when`(mapper.mapDataToView(dataModel)).thenReturn(viewModel)

        presenter.onListsRetrieved(dataModel)

        verify(view).updateProjectsListView(projectsListViewModel)
        verify(view).updateLibrariesListView(librariesListViewModel)
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
}