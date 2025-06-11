package com.example.booklibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    private var booksList = mutableListOf<Books>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewBooks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = BookAdapter(booksList) { book ->
            Snackbar.make(recyclerView, "Selected: ${book.title}", Snackbar.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,  // Drag directions
            ItemTouchHelper.RIGHT                        // Swipe direction
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition

                // გადაადგილება მონაცემების სიაში
                Collections.swap(booksList, fromPos, toPos)
                adapter.notifyItemMoved(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedBook = booksList[position]
                booksList.removeAt(position)
                adapter.notifyItemRemoved(position)

                Snackbar.make(recyclerView, "წიგნი წაიშალა", Snackbar.LENGTH_LONG)
                    .setAction("შეინარჩუნე") {
                        booksList.add(position, deletedBook)
                        adapter.notifyItemInserted(position)
                    }.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        ResClient.init()

        ResClient.getReqResService().getBooks(1)
            .enqueue(object : Callback<ReqesObj<List<Books>>> {
                override fun onResponse(
                    call: Call<ReqesObj<List<Books>>>,
                    response: Response<ReqesObj<List<Books>>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        booksList.clear()
                        response.body()!!.data?.let {
                            booksList.addAll(it)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<ReqesObj<List<Books>>>, t: Throwable) {
                    Snackbar.make(recyclerView, "ჩატვირთვა ვერ მოხერხდა: ${t.localizedMessage}", Snackbar.LENGTH_LONG).show()
                }
            })
    }
}
