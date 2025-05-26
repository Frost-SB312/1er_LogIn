package com.example.a1erlogin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class PublicationAdapter(
    private val publications: MutableList<Publication>,
    private val currentEmail: String,
    private val onLike: (String) -> Unit
) : RecyclerView.Adapter<PublicationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentTextView: TextView = view.findViewById(R.id.textViewContent)
        val likeButton: ImageButton = view.findViewById(R.id.buttonLike)
        val commentEditText: EditText = view.findViewById(R.id.editTextComment)
        val likesCountTextView: TextView = view.findViewById(R.id.textViewLikesCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publication, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val publication = publications[position]
        holder.contentTextView.text = publication.content

        val userLiked = currentEmail in publication.likes
        holder.likeButton.setImageResource(
            if (userLiked) R.drawable.ic_thumb_up_filled
            else R.drawable.ic_thumb_up_outline
        )

        // Mostrar la cantidad de likes
        val likeCount = publication.likes.size
        holder.likesCountTextView.text = "$likeCount me gusta"

        holder.likeButton.setOnClickListener {
            onLike(publication.id)
        }
    }


    override fun getItemCount() = publications.size

    fun updateLikes(publicationId: String, newLikes: List<String>) {
        val index = publications.indexOfFirst { it.id == publicationId }
        if (index != -1) {
            val updated = publications[index].copy(likes = newLikes)
            publications[index] = updated
            notifyItemChanged(index)
        }
    }
}

