package com.example.booklibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val books: List<Books>,
    private val onBookClick: (Books) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.textTitle)
        val authorText: TextView = itemView.findViewById(R.id.textAuthor)
        val yearText: TextView = itemView.findViewById(R.id.textYear)
        val expandIcon: ImageView = itemView.findViewById(R.id.imageExpand)
        val expandedLayout: View = itemView.findViewById(R.id.layoutExpanded)
        val ratingText: TextView = itemView.findViewById(R.id.textRating)
        val descriptionText: TextView = itemView.findViewById(R.id.textDescription)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toggleExpansion(position)
                }
            }

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBookClick(books[position])
                }
                true
            }
        }

        private fun toggleExpansion(position: Int) {
            if (expandedPositions.contains(position)) {
                expandedPositions.remove(position)
            } else {
                expandedPositions.add(position)
            }
            notifyItemChanged(position)
        }

        fun bind(book: Books, position: Int) {
            titleText.text = book.title
            authorText.text = "ავტორი: ${book.author}"
            yearText.text = "წელი: ${book.year}"

            val isExpanded = expandedPositions.contains(position)

            if (isExpanded) {
                expandedLayout.visibility = View.VISIBLE
                expandIcon.rotation = 180f
                ratingText.text = "რეიტინგი: ${book.rating}/5"
                descriptionText.text = book.description // ან Descriptions, თუ ასეა დასახელებული
            } else {
                expandedLayout.visibility = View.GONE
                expandIcon.rotation = 0f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position], position)
    }

    override fun getItemCount(): Int = books.size
}
