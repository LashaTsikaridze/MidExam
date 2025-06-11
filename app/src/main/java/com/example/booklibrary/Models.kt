package com.example.booklibrary

import android.graphics.pdf.PdfDocument.Page
import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName
import java.io.Serial

data class Books(
    val id: Int?,
    val title: String?,
    val author: String?,
    val rating: Double?,       // თუ JSON-ში არის ციფრი
    val year: Int?,
    val description: String?   // პატარა d-ით
)


data class ReqesObj<T>(
    var page: Int?,
    @SerializedName("per_page")
    var perPage: Int?,
    var total: Long?,
    @SerializedName("total_pages")
    var totalPages: Int?,
    var data: T?
)