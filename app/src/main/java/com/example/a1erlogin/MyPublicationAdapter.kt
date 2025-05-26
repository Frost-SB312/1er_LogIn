package com.example.a1erlogin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class MyPublicationAdapter(
    private val publications: List<Publication>,
    private val onEdit: (Publication) -> Unit,
    private val onDelete: (Publication) -> Unit
) : RecyclerView.Adapter<MyPublicationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.findViewById(R.id.publicationContent)
        val editBtn: Button = view.findViewById(R.id.btnEdit)
        val deleteBtn: Button = view.findViewById(R.id.btnDelete)
        val likesCountTextView: TextView = view.findViewById(R.id.textViewLikesCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_publication, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val publication = publications[position]

        // Formatear fecha si existe
        val dateFormatted = publication.timestamp?.toDate()?.let {
            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(it)
        } ?: "Fecha desconocida"

        holder.content.text = "${publication.content}\n\nPublicado: $dateFormatted"
        holder.editBtn.setOnClickListener { onEdit(publication) }
        holder.deleteBtn.setOnClickListener { onDelete(publication) }

        // Mostrar la cantidad de likes
        val likeCount = publication.likes.size
        holder.likesCountTextView.text = "$likeCount me gusta"
    }

    override fun getItemCount() = publications.size
}
