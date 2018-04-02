package com.baddesigns.android.projects

import android.view.LayoutInflater
import android.widget.TextView
import com.baddesigns.android.projects.models.view_models.ListHeaderViewModel
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class HeaderViewHolderTest {

    private lateinit var viewHolder: HeaderViewHolder

    private val view = LayoutInflater.from(RuntimeEnvironment.application)
            .inflate(R.layout.header_item, null)

    @Test
    fun bindView() {
        viewHolder = HeaderViewHolder(view)
        viewHolder.bindView(ListHeaderViewModel("Header", mutableListOf()))

        val titleView: TextView = viewHolder.itemView.findViewById(R.id.headerTitle)
        assertEquals("Header", titleView.text)
    }
}