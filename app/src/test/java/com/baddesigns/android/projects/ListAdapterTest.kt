package com.baddesigns.android.projects

import android.view.ViewGroup
import com.baddesigns.android.projects.helpers.generators.MainScreen.ModelGenerator
import com.baddesigns.android.projects.models.view_models.ListHeaderViewModel
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

/**
 * Created by Jon-Ross on 25/03/2018.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ListAdapterTest {

    @Mock
    private lateinit var projectsListItemCheckboxListener: ListViewHolder.Callback
    @Mock
    private lateinit var libraryListItemCheckboxListener: ListViewHolder.Callback

    private lateinit var listAdapter: ListAdapter

    @Before
    fun setUp() {
        initMocks(this)

        val models = ModelGenerator().generateConnectedViewModel()

        val projectsHeader = ListHeaderViewModel("Projects",
                models.projectsList as MutableList<ListItemViewModel>)
        val librariesHeader = ListHeaderViewModel("Libraries",
                models.librariesList as MutableList<ListItemViewModel>)
        val parentList = mutableListOf(projectsHeader, librariesHeader)

        listAdapter = ListAdapter(parentList)
    }

    @Test
    fun onCreateParentViewHolder() {
        val viewHolder = listAdapter.onCreateParentViewHolder(
                object : ViewGroup(RuntimeEnvironment.application) {
                    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
                    }
                }, 0)

        assertNotNull(viewHolder.itemView.findViewById(R.id.headerTitle))
    }

    @Test
    fun onCreateChildViewHolder() {
        val viewHolder = listAdapter.onCreateChildViewHolder(
                object : ViewGroup(RuntimeEnvironment.application) {
            override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
            }
        }, 0)

        assertNotNull(viewHolder.itemView.findViewById(R.id.itemName))
        assertNotNull(viewHolder.itemView.findViewById(R.id.itemCheckBox))
    }

    @Test
    fun onBindParentViewHolder() {
        val viewHolder = mock(HeaderViewHolder::class.java)
        val model = mock(ListHeaderViewModel::class.java)

        listAdapter.onBindParentViewHolder(viewHolder, 0, model)

        verify(viewHolder).bindView(model)
    }

    @Test
    fun onBindChildViewHolder_setProjectsListener() {
        listAdapter.setProjectsItemCheckboxListener(projectsListItemCheckboxListener)
        val viewHolder = mock(ListViewHolder::class.java)
        val model = ListItemViewModel(itemType = ItemType.PROJECT)

        listAdapter.onBindChildViewHolder(viewHolder, 0, 0, model)

        verify(viewHolder).setCallback(projectsListItemCheckboxListener)
        verify(viewHolder).bindView(model)
    }

    @Test
    fun onBindChildViewHolder_setLibrariesListener() {
        listAdapter.setLibrariesItemCheckboxListener(libraryListItemCheckboxListener)
        val viewHolder = mock(ListViewHolder::class.java)
        val model = ListItemViewModel(itemType = ItemType.LIBRARY)

        listAdapter.onBindChildViewHolder(viewHolder, 0, 0, model)

        verify(viewHolder).setCallback(libraryListItemCheckboxListener)
        verify(viewHolder).bindView(model)
    }

    @Test
    fun setProjectsListItems() {
        val newList = mutableListOf(ListItemViewModel("hello"))

        listAdapter.setProjectsListItems(newList)

        assertEquals(newList, listAdapter.items[0].list)
    }

    @Test
    fun setLibrariesListItems() {
        val newList = mutableListOf(ListItemViewModel("hello"))

        listAdapter.setLibrariesListItems(newList)

        assertEquals(newList, listAdapter.items[1].list)
    }

    @Test
    fun setParentList() {
        val list = mutableListOf(
                ListHeaderViewModel("list1", mutableListOf(ListItemViewModel())),
                ListHeaderViewModel("list2", mutableListOf(ListItemViewModel())))

        listAdapter.setParentList(list, true)

        assertEquals(2, listAdapter.items.size)
        assertEquals(list, listAdapter.items)
    }
}