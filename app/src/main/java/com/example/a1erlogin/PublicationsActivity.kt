package com.example.a1erlogin

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class PublicationsActivity : AppCompatActivity() {

    private lateinit var currentEmail: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: PublicationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_publications)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setup()
    }

    private fun setup() {
        currentEmail = intent.getStringExtra("email") ?: ""
        recyclerView = findViewById(R.id.publicationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        db = FirebaseFirestore.getInstance()

        loadPublications()
    }

    private fun loadPublications() {
        db.collection("publications")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val publications = result.documents.mapNotNull { doc ->
                    val author = doc.getString("author") ?: return@mapNotNull null
                    if (author != currentEmail) {
                        Publication(
                            id = doc.id,
                            content = doc.getString("content") ?: "",
                            author = author,
                            timestamp = doc.getTimestamp("timestamp"),
                            likes = doc.get("likes") as? List<String> ?: emptyList()
                        )
                    } else {
                        null
                    }
                }.toMutableList()

                adapter = PublicationAdapter(
                    publications,
                    currentEmail,
                    onLike = { docId -> handleLike(docId) }
                )
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.e("PublicationsActivity", "Error al obtener publicaciones", e)
            }
    }


    private fun handleLike(publicationId: String) {
        val publicationRef = db.collection("publications").document(publicationId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(publicationRef)
            val likes = snapshot.get("likes") as? List<String> ?: emptyList()

            val updatedLikes = if (currentEmail in likes) {
                likes - currentEmail
            } else {
                likes + currentEmail
            }

            transaction.update(publicationRef, "likes", updatedLikes)
            updatedLikes // devolvemos la lista nueva
        }.addOnSuccessListener { updatedLikes ->
            adapter.updateLikes(publicationId, updatedLikes)
        }.addOnFailureListener { e ->
            Log.e("PublicationsActivity", "Error al actualizar like", e)
        }
    }

}
