package com.themasterspirit.easyflickr.ui.search

import android.content.Context
import android.database.Cursor
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.utils.application
import com.themasterspirit.easyflickr.utils.inflate
import kotlinx.android.synthetic.main.item_search_suggestion.view.*

class SearchSuggestionAdapter(
        private val searchView: SearchView, cursor: Cursor
) : CursorAdapter(searchView.context, cursor, 0 /*FLAG_REGISTER_CONTENT_OBSERVER*/) {

    private val logger = searchView.context.application.logger

    private val availableCursorCodes = mutableSetOf<Int>()

    var onRemoveClickListener: ((query: String) -> Unit)? = null

    init {
        availableCursorCodes.add(cursor.hashCode())
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return parent.inflate(R.layout.item_search_suggestion)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val text = cursor.getString(cursor.getColumnIndexOrThrow("query"))
        with(view) {
            tvSearchQuery.text = text
            setOnClickListener { searchView.setQuery(text, true) }
            ivDelete.setOnClickListener { onRemoveClickListener?.invoke(text) }
        }
    }

    override fun getCount(): Int {
        return super.getCount().also { count ->
            logger.log(TAG, "getCount(); count = [$count], cursor = [${cursor.hashCode()}]")
        }
    }

    override fun changeCursor(newCursor: Cursor) {
        logger.log(TAG, "changeCursor(); new[${newCursor.hashCode()}]")
    }

    override fun swapCursor(newCursor: Cursor): Cursor {
        availableCursorCodes.add(newCursor.hashCode())
        logger.log(TAG, "swapCursor(); newCursor.hashCode() = [${newCursor.hashCode()}]")
        return newCursor.let { cursor: Cursor ->
            return@let if (availableCursorCodes.contains(cursor.hashCode())) {
                super.swapCursor(cursor)?.also { oldCursor ->
                    availableCursorCodes.remove(oldCursor.hashCode())
                    logger.log(TAG, "oldCursor.hashCode() = [${oldCursor.hashCode()}]")
                }
            } else null
        } ?: cursor
    }

    companion object {
        const val TAG = "SearchSuggestionAdapter"
    }
}