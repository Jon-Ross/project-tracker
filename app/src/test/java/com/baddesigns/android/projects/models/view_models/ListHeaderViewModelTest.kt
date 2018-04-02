package com.baddesigns.android.projects.models.view_models

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

class ListHeaderViewModelTest {

    @Mock
    private lateinit var list: MutableList<ListItemViewModel>

    private lateinit var model: ListHeaderViewModel

    @Before
    fun setUp() {
        initMocks(this)

        model = ListHeaderViewModel("title", list)
    }

    @Test
    fun getChildList() {
        assertEquals(list, model.childList)
    }

    @Test
    fun isInitiallyExpanded() {
        assertTrue(model.isInitiallyExpanded)
    }
}