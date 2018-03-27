package com.baddesigns.android.projects.data_providers

import com.baddesigns.android.projects.helpers.generators.MainScreen.ModelGenerator
import com.baddesigns.android.projects.models.data_models.ListItemModel
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
class SimpleDataProviderTest {

    @Mock
    private lateinit var callback: IMainScreenDataProvider.Callback

    private lateinit var dataProvider: SimpleDataProvider

    private lateinit var modelGenerator: ModelGenerator

    @Before
    fun setUp() {
        initMocks(this)

        modelGenerator = ModelGenerator()
        dataProvider = SimpleDataProvider

        dataProvider.addLibraryItems(modelGenerator.generateLibrariesDataModelList())
        dataProvider.addProjectItems(modelGenerator.generateProjectsDataModelList())
    }

    @After
    fun tearDown() {
        dataProvider.clearDb()
    }

    @Test
    fun addProjectItems_varargs() {
        dataProvider.clearDb()
        val projectsItems = modelGenerator.generateProjectsDataModelList()

        projectsItems.forEach {
            dataProvider.addProjectItems(it)
        }

        assertEquals(3, dataProvider.model.projectsList.size)
        assertEquals(projectsItems, dataProvider.model.projectsList)
    }

    @Test
    fun addProjectItems_list() {
        dataProvider.clearDb()
        val projectsItems = modelGenerator.generateProjectsDataModelList()

        dataProvider.addProjectItems(projectsItems)

        assertEquals(3, dataProvider.model.projectsList.size)
        assertEquals(projectsItems, dataProvider.model.projectsList)
    }

    @Test
    fun addLibraryItems_varargs() {
        dataProvider.clearDb()
        val libraryItems = modelGenerator.generateLibrariesDataModelList()

        libraryItems.forEach {
            dataProvider.addLibraryItems(it)
        }

        assertEquals(4, dataProvider.model.librariesList.size)
        assertEquals(libraryItems, dataProvider.model.librariesList)
    }

    @Test
    fun addLibraryItems_list() {
        dataProvider.clearDb()
        val libraryItems = modelGenerator.generateLibrariesDataModelList()

        dataProvider.addLibraryItems(libraryItems)

        assertEquals(4, dataProvider.model.librariesList.size)
        assertEquals(libraryItems, dataProvider.model.librariesList)
    }

    @Test
    fun fetchLists() {
        dataProvider.fetchLists(callback)

        verify(callback).onListsRetrieved(dataProvider.model)
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
        dataProvider.addProjectItems(modelGenerator.generateProjectsDataModelList())
        dataProvider.addLibraryItems(modelGenerator.generateLibrariesDataModelList())

        dataProvider.clearDb()

        assertEquals(0, dataProvider.model.librariesList.size)
        assertEquals(0, dataProvider.model.projectsList.size)
    }
}