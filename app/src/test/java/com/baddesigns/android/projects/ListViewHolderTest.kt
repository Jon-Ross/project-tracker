package com.baddesigns.android.projects

import android.support.v7.widget.AppCompatCheckBox
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
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
class ListViewHolderTest {

    @Mock
    private lateinit var callback: ListViewHolder.Callback

    private lateinit var viewHolder: ListViewHolder

    private val view = LayoutInflater.from(RuntimeEnvironment.application)
            .inflate(R.layout.list_item, null)

    @Before
    fun setUp() {
        initMocks(this)

        viewHolder = ListViewHolder(view)

        viewHolder.setCallback(callback)
    }

    @Test
    fun bindView_checkboxCheckedAndVisible_listItemVisible() {
        viewHolder.bindView(ListItemViewModel(
                "hello",
                false,
                true,
                false))

        val nameView: TextView = viewHolder.itemView.findViewById(R.id.itemName)
        val checkBoxView: AppCompatCheckBox = viewHolder.itemView.findViewById(R.id.itemCheckBox)
        assertEquals("hello", nameView.text)
        assertFalse(checkBoxView.isChecked)
        assertEquals(View.VISIBLE, checkBoxView.visibility)
        assertEquals(View.VISIBLE, viewHolder.itemView.visibility)
    }

    @Test
    fun bindView_checkboxUncheckedAndGone_listItemGone() {
        viewHolder.bindView(ListItemViewModel(
                "hello",
                true,
                false,
                true))

        val nameView: TextView = viewHolder.itemView.findViewById(R.id.itemName)
        val checkBoxView: AppCompatCheckBox = viewHolder.itemView.findViewById(R.id.itemCheckBox)

        assertEquals("hello", nameView.text)
        assertTrue(checkBoxView.isChecked)
        assertEquals(View.GONE, checkBoxView.visibility)
        assertEquals(View.GONE, viewHolder.itemView.visibility)
    }

    @Test
    fun checkChanged_true_changesItemSelection_delegatesToCallback() {
        val selected = false
        val item = ListItemViewModel("hello", selected, false)
        viewHolder.bindView(item)

        val checkBoxView: AppCompatCheckBox = viewHolder.itemView.findViewById(R.id.itemCheckBox)
        checkBoxView.isChecked = selected

        checkBoxView.performClick()

        assertTrue(item.selected)
        verify(callback).checkboxClicked(true)
    }

    @Test
    fun checkChanged_false_changesItemSelection_delegatesToCallback() {
        val selected = true
        val item = ListItemViewModel("hello", selected, false)
        viewHolder.bindView(item)

        val checkBoxView: AppCompatCheckBox = viewHolder.itemView.findViewById(R.id.itemCheckBox)
        checkBoxView.isChecked = selected

        checkBoxView.performClick()

        assertFalse(item.selected)
        verify(callback).checkboxClicked(false)
    }

    @Test
    fun setCallback() {
        val newCallback = mock(ListViewHolder.Callback::class.java)
        viewHolder.setCallback(newCallback)

        assertEquals(newCallback, viewHolder.callback)
    }
}