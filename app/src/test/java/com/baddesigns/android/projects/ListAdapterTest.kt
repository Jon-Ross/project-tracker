package com.baddesigns.android.projects

import android.view.ViewGroup
import com.baddesigns.android.projects.models.view_models.ListItemViewModel
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.*

/**
 * Created by Jon-Ross on 25/03/2018.
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ListAdapterTest {

    @Mock
    private lateinit var callback: ListAdapter.Callback

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
        listAdapter = ListAdapter(listItems)
        listAdapter.setCallback(callback)
        assertEquals(callback, listAdapter.callback)
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

        verify(viewHolder).setCallback(listAdapter)
        verify(viewHolder).bindView(listAdapter.items[0])
    }

    @Test
    fun checkboxClicked_whenChecked_andOnlyOneCheck_allOtherCheckboxesSetToHidden() {
        val item3 = ListItemViewModel(name = "3", selected = true, checkboxShowing = true)
        val list = mutableListOf(
                ListItemViewModel(name = "1", selected = false, checkboxShowing = true),
                ListItemViewModel(name = "2", selected = false, checkboxShowing = true),
                item3,
                ListItemViewModel(name = "4", selected = false, checkboxShowing = true)
        )
        listAdapter.items = list

        listAdapter.checkboxClicked(true)

        verify(callback).setAllCheckboxesVisibility(false)
        verify(callback).filterList(listOf(item3.id))
    }

    @Test
    fun checkboxClicked_whenUnchecked_andNoChecks_allOtherCheckboxesSetToHidden_removeFilter() {
        val list = mutableListOf(
                ListItemViewModel(name = "1", selected = false, checkboxShowing = true),
                ListItemViewModel(name = "2", selected = false, checkboxShowing = true),
                ListItemViewModel(name = "3", selected = false, checkboxShowing = true),
                ListItemViewModel(name = "4", selected = false, checkboxShowing = true)
        )
        listAdapter.items = list

        listAdapter.checkboxClicked(false)

        verify(callback).setAllCheckboxesVisibility(true)
        verify(callback).removeFilter()
        verifyNoMoreInteractions(callback)
    }

    @Test
    fun checkboxClicked_whenChecked_andMoreThanOneSelected_filterListOnly() {
        val item1 = ListItemViewModel(name = "1", selected = true, checkboxShowing = true)
        val item3 = ListItemViewModel(name = "3", selected = true, checkboxShowing = true)
        val list = mutableListOf(
                item1,
                ListItemViewModel(name = "2", selected = false, checkboxShowing = true),
                item3,
                ListItemViewModel(name = "4", selected = false, checkboxShowing = true)
        )
        listAdapter.items = list

        listAdapter.checkboxClicked(true)

        verify(callback).filterList(listOf(item1.id, item3.id))
        verifyNoMoreInteractions(callback)
    }

    @Test
    fun checkboxClicked_whenUnchecked_andMoreThanZeroSelected_filterListOnly() {
        val item4 = ListItemViewModel(name = "4", selected = true, checkboxShowing = true)
        val list = mutableListOf(
                ListItemViewModel(name = "1", selected = false, checkboxShowing = true),
                ListItemViewModel(name = "2", selected = false, checkboxShowing = true),
                ListItemViewModel(name = "3", selected = false, checkboxShowing = true),
                item4
        )
        listAdapter.items = list

        listAdapter.checkboxClicked(false)

        verify(callback).filterList(listOf(item4.id))
        verifyNoMoreInteractions(callback)
    }

    @Test
    fun updateList() {
        val list: List<ListItemViewModel> = mutableListOf(
                ListItemViewModel(name = "Projects", selected = true)
        )
        listAdapter.updateList(list)

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
    fun filterList_whenExistingIdsPassedIn_filtersList() {
        val filter = generateFilteredList(listAdapter.items)

        listAdapter.filterList(filter)

        assertEquals(4, listAdapter.items.size)
        assertTrue(listAdapter.items[0].hidden)
        assertFalse(listAdapter.items[1].hidden)
        assertTrue(listAdapter.items[2].hidden)
        assertFalse(listAdapter.items[3].hidden)
    }

    @Test
    fun removeFilter() {
        addFiltersToList(listAdapter.items)

        listAdapter.removeFilter()

        val unhiddenList = listAdapter.items.filter {
            !it.hidden
        }
        assertEquals(4, unhiddenList.size)
    }

    @Test
    fun retrieveSelectedIds() {
        val ids = listAdapter.retrieveSelectedIds()

        assertEquals(2, ids.size)
        assertEquals(listItems[1].id, ids[0])
        assertEquals(listItems[2].id, ids[1])
    }

    @Test
    fun setCallback() {
        val newCallback = mock(ListAdapter.Callback::class.java)

        listAdapter.setCallback(newCallback)

        assertEquals(newCallback, listAdapter.callback)
    }

    private fun addFiltersToList(items: List<ListItemViewModel>) {
        for((i, item) in items.withIndex()) {
            if(i % 2 == 1) item.hidden = true
        }
    }

    private fun generateFilteredList(items: List<ListItemViewModel>) : List<UUID> {
        val filteredList: MutableList<UUID> = mutableListOf()
        for((i, item) in items.withIndex()) {
            if(i % 2 == 1) filteredList.add(item.id)
        }
        return filteredList
    }
}