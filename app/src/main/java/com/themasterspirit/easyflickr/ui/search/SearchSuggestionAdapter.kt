package com.themasterspirit.easyflickr.ui.search

import android.content.Context
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.utils.inflate
import kotlinx.android.synthetic.main.item_search_suggestion.view.*

class SearchSuggestionAdapter(
        private val searchView: SearchView, cursor: Cursor
) : CursorAdapter(searchView.context, cursor, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return parent.inflate(R.layout.item_search_suggestion)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val text = cursor.getString(cursor.getColumnIndexOrThrow("query"))
        with(view) {
            setOnClickListener { searchView.setQuery(text, true) }
            tvSearchQuery.text = text
            ivDelete.setOnClickListener {
                // todo: remove search text
                Toast.makeText(context, "delete [$text] click", Toast.LENGTH_SHORT).show()
            }
        }
    }
}