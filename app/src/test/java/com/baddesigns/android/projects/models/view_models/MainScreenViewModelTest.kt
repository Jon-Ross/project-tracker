package com.baddesigns.android.projects.models.view_models

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotSame
import org.junit.Before
import org.junit.Test
import java.util.*

class MainScreenViewModelTest {

    private lateinit var model: MainScreenViewModel

    @Before
    fun setUp() {
        val projects = mutableListOf<ListItemViewModel>()
        val pId = UUID.randomUUID()
        val pConId = UUID.randomUUID()
        projects.add(ListItemViewModel(name = "pnm", selected = true, checkboxShowing = false,
                connections = mutableSetOf(pConId), id = pId))
        val libraries = mutableListOf<ListItemViewModel>()
        val lId = UUID.randomUUID()
        val lConId = UUID.randomUUID()
        libraries.add(ListItemViewModel(name = "lnm", selected = false, checkboxShowing = true,
                connections = mutableSetOf(lConId), id = lId))
        model = MainScreenViewModel(projects, libraries)
    }

    @Test
    fun deepCopy() {
        val copy = model.deepCopy()

        assertEquals(model, copy)

        model.projectsList[0].selected = false
        model.librariesList[0].selected = true

        assertNotSame(model, copy)
    }
}