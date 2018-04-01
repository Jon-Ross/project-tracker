package com.baddesigns.android.projects.models.data_models

import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class ListsDataModelTest {

    private lateinit var model: ListsDataModel

    @Before
    fun setUp() {
        val projects = mutableListOf<ListItemModel>()
        val pId = UUID.randomUUID()
        val pConId = UUID.randomUUID()
        projects.add(ListItemModel(name = "pnm", connections = mutableSetOf(pConId), id = pId))
        val libraries = mutableListOf<ListItemModel>()
        val lId = UUID.randomUUID()
        val lConId = UUID.randomUUID()
        libraries.add(ListItemModel(name = "lnm", connections = mutableSetOf(lConId), id = lId))
        model = ListsDataModel(projects, libraries)
    }

    @Test
    fun deepCopy() {
        val copy = model.deepCopy()

        Assert.assertEquals(model, copy)

        model.projectsList[0] = ListItemModel()
        model.librariesList[0] = ListItemModel()

        Assert.assertNotSame(model, copy)
    }
}