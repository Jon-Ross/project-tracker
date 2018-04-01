package com.baddesigns.android.projects

import android.view.ViewGroup
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import junit.framework.Assert.*
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
    private lateinit var callback: ListAdapter.Callback
    @Mock
    private lateinit var listItemCheckboxListener: ListViewHolder.Callback

    private lateinit var listAdapter: ListAdapter
    private lateinit var listItems: List<ListItemViewModel>

    @Before
    fun setUp() {
        initMocks(this)

        listItems = mutableListOf(
                ListItemViewModel(name = "hello"),
                ListItemViewModel(name = "world", selected = true),
                ListItemViewModel(name = "Projects", selected = true),
                ListItemViewModel(name = "RxJava"))
        listAdapter = ListAdapter(listItems, listItemCheckboxListener)
    }

    @Test
    fun onCreateViewHolder() {
        val viewHolder = listAdapter.onCreateViewHolder(
                object : ViewGroup(RuntimeEnvironment.application) {
            override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
            }
        }, 0)

        assertNotNull(viewHolder.itemView.findViewById(R.id.itemName))
        assertNotNull(viewHolder.itemView.findViewById(R.id.itemCheckBox))
    }

    @Test
    fun getItemCount() {
        assertEquals(4, listAdapter.itemCount)
    }

    @Test
    fun onBindViewHolder() {
        val viewHolder = mock(ListViewHolder::class.java)
        listAdapter.onBindViewHolder(viewHolder, 0)

        verify(viewHolder).setCallback(listItemCheckboxListener)
        verify(viewHolder).bindView(listAdapter.items[0])
    }

    @Test
    fun getListItems() {
        assertEquals(listItems, listAdapter.getListItems())
    }

    @Test
    fun setListItems() {
        val newList = mutableListOf(ListItemViewModel("hello"))

        listAdapter.setListItems(newList)

        assertEquals(newList, listAdapter.items)
    }

    @Test
    fun updateList() {
        val list: List<ListItemViewModel> = mutableListOf(
                ListItemViewModel(name = "Projects", selected = true)
        )
        listAdapter.setListItems(list)

        assertEquals(1, listAdapter.itemCount)
        assertEquals(list, listAdapter.items)
    }

    @Test
    fun setAllCheckboxesVisibility_whenSetToShowing_holderSetsCheckboxesShowing() {
        listItems.forEach {
            it.checkboxShowing = false
        }

        listAdapter.setAllCheckboxesVisibility(true)

        listItems.forEach {
            assertTrue(it.checkboxShowing)
        }
    }

    @Test
    fun setAllCheckboxesVisibility_whenSetToNotShowing_holderSetsCheckboxesHidden() {
        listItems.forEach {
            it.checkboxShowing = true
        }

        listAdapter.setAllCheckboxesVisibility(false)

        listItems.forEach {
            assertFalse(it.checkboxShowing)
        }
    }

    @Test
    fun retrieveSelectedIds() {
        val ids = listAdapter.retrieveSelectedIds()

        assertEquals(2, ids.size)
        assertEquals(listItems[1].id, ids[0])
        assertEquals(listItems[2].id, ids[1])
    }
}