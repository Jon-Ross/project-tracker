package com.baddesigns.android.projects.mappers

import com.baddesigns.android.projects.models.data_models.ListItemModel
import com.baddesigns.android.projects.models.data_models.ListsDataModel
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
                ListItemModel(name = "Projects"),
                ListItemModel(name = "Spoiler Free")
        )
        val librariesList = mutableListOf(
                ListItemModel(name = "Room"),
                ListItemModel(name = "Retrofit")
        )

        val dataModel = ListsDataModel(projectsList, librariesList)

        val viewModel = mapper.mapDataToView(dataModel)

        assertEquals(projectsList[0].name, viewModel.projectsList[0].name)
        assertEquals(projectsList[0].id, viewModel.projectsList[0].id)

        assertEquals(projectsList[1].name, viewModel.projectsList[1].name)
        assertEquals(projectsList[1].id, viewModel.projectsList[1].id)

        assertEquals(librariesList[0].name, viewModel.librariesList[0].name)
        assertEquals(librariesList[0].id, viewModel.librariesList[0].id)

        assertEquals(librariesList[1].name, viewModel.librariesList[1].name)
        assertEquals(librariesList[1].id, viewModel.librariesList[1].id)
    }

}