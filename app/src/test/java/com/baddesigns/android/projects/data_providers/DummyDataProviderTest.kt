package com.baddesigns.android.projects.data_providers

import com.baddesigns.android.projects.helpers.generators.MainScreen.ModelGenerator
import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.ListsDataModel
import junit.framework.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class DummyDataProviderTest {

    @Mock
    private lateinit var callback: IMainScreenDataProvider.Callback

    private lateinit var dataProvider: DummyDataProvider

    private val modelGenerator = ModelGenerator()
    private lateinit var dataModel: ListsDataModel

    @Before
    fun setUp() {
        initMocks(this)

        dataProvider = DummyDataProvider

        dataModel = modelGenerator.generateDisconnectedDataModel()

        dataProvider.addProjectItems(dataModel.projectsList)
        dataProvider.addLibraryItems(dataModel.librariesList)
    }

    @After
    fun tearDown() {
        dataProvider.clearDb()
    }

    @Test
    fun addProjectItems_varargs() {
        dataProvider.clearDb()
        val projectsItems = dataModel.projectsList

        projectsItems.forEach {
            dataProvider.addProjectItems(it)
        }

        assertEquals(3, dataProvider.model.projectsList.size)
        assertEquals(projectsItems, dataProvider.model.projectsList)
    }

    @Test
    fun addProjectItems_list() {
        dataProvider.clearDb()
        val projectsItems = dataModel.projectsList

        dataProvider.addProjectItems(projectsItems)

        assertEquals(3, dataProvider.model.projectsList.size)
        assertEquals(projectsItems, dataProvider.model.projectsList)
    }

    @Test
    fun addLibraryItems_varargs() {
        dataProvider.clearDb()
        val libraryItems = dataModel.librariesList

        libraryItems.forEach {
            dataProvider.addLibraryItems(it)
        }

        assertEquals(10, dataProvider.model.librariesList.size)
        assertEquals(libraryItems, dataProvider.model.librariesList)
    }

    @Test
    fun addLibraryItems_list() {
        dataProvider.clearDb()
        val libraryItems = dataModel.librariesList

        dataProvider.addLibraryItems(libraryItems)

        assertEquals(10, dataProvider.model.librariesList.size)
        assertEquals(libraryItems, dataProvider.model.librariesList)
    }

    @Test
    fun fetchLists_withCallback() {
        dataProvider.fetchLists(callback)

        verify(callback).onListsRetrieved(dataProvider.model)
    }

    @Test
    fun fetchLists() {
        assertEquals(dataProvider.model, dataProvider.fetchLists())
    }

    @Test
    fun getCachedLists() {
        assertEquals(dataProvider.model, dataProvider.getCachedLists())
    }

    @Test
    fun connectModels_passing2ExistingModels_connectsWithEachOtherAndReturnsTrue() {
        val project = dataProvider.model.projectsList[0]
        val library = dataProvider.model.librariesList[0]

        assertTrue(dataProvider.connectItems(project, library))

        assertEquals(1, project.connections.size)
        assertTrue(project.connections.contains(library.id))
        assertEquals(1, library.connections.size)
        assertTrue(library.connections.contains(project.id))
    }

    @Test
    fun connectModels_bothModelsAreProjectsDontConnect() {
        val project1 = dataProvider.model.projectsList[0]
        val project2 = dataProvider.model.projectsList[1]

        assertFalse(dataProvider.connectItems(project1, project2))

        assertEquals(0, project1.connections.size)
        assertEquals(0, project2.connections.size)
    }

    @Test
    fun connectModels_bothModelsAreLibrariesDontConnect() {
        val library1 = dataProvider.model.librariesList[0]
        val library2 = dataProvider.model.librariesList[1]

        assertFalse(dataProvider.connectItems(library1, library2))

        assertEquals(0, library1.connections.size)
        assertEquals(0, library2.connections.size)
    }

    @Test
    fun connectModels_whenConnectingNonExistentModel_failsAndReturnsFalse() {
        val newProject = ListItemModel(name = "p1")

        assertFalse(dataProvider.connectItems(newProject, dataProvider.model.librariesList[0]))
        assertEquals(0, newProject.connections.size)
        assertEquals(0, dataProvider.model.librariesList[0].connections.size)
    }

    @Test
    fun clearDb() {
        dataProvider.addProjectItems(dataModel.projectsList)
        dataProvider.addLibraryItems(dataModel.librariesList)

        dataProvider.clearDb()

        assertEquals(0, dataProvider.model.librariesList.size)
        assertEquals(0, dataProvider.model.projectsList.size)
    }
}