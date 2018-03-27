package com.baddesigns.android.projects.mappers

import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.MainScreenModel
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Created by Jon-Ross on 25/03/2018.
 */
class MainScreenMapperTest {

    private val mapper = MainScreenMapper()

    @Test
    fun mapDataToView() {
        val projectsList = mutableListOf(
                ListItemModel(name = "Projects", selected = true),
                ListItemModel(name = "Spoiler Free")
        )
        val librariesList = mutableListOf(
                ListItemModel(name = "Room", selected = true),
                ListItemModel(name = "Retrofit")
        )

        val dataModel = MainScreenModel(projectsList, librariesList)

        val viewModel = mapper.mapDataToView(dataModel)

        assertEquals(projectsList[0].name, viewModel.projectsList[0].name)
        assertEquals(projectsList[0].selected, viewModel.projectsList[0].selected)
        assertEquals(projectsList[0].id, viewModel.projectsList[0].id)
        assertEquals(projectsList[0].connections, viewModel.projectsList[0].connections)

        assertEquals(projectsList[1].name, viewModel.projectsList[1].name)
        assertEquals(projectsList[1].selected, viewModel.projectsList[1].selected)
        assertEquals(projectsList[1].id, viewModel.projectsList[1].id)
        assertEquals(projectsList[1].connections, viewModel.projectsList[1].connections)

        assertEquals(librariesList[0].name, viewModel.librariesList[0].name)
        assertEquals(librariesList[0].selected, viewModel.librariesList[0].selected)
        assertEquals(librariesList[0].id, viewModel.librariesList[0].id)
        assertEquals(librariesList[0].connections, viewModel.librariesList[0].connections)

        assertEquals(librariesList[1].name, viewModel.librariesList[1].name)
        assertEquals(librariesList[1].selected, viewModel.librariesList[1].selected)
        assertEquals(librariesList[1].id, viewModel.librariesList[1].id)
        assertEquals(librariesList[1].connections, viewModel.librariesList[1].connections)
    }

}