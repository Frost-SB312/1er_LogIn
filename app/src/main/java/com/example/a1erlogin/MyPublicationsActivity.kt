package com.example.a1erlogin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query


class MyPublicationsActivity : AppCompatActivity() {
    lateinit var MyPublicationsTextView: TextView
    lateinit var EditBtn: Button
    lateinit var DeleteBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_my_publications)
        setup()



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setup() {
        title = "Mis publicaciones"
        val recyclerView = findViewById<RecyclerView>(R.id.publicationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val email = intent.getStringExtra("email") ?: ""
        val db = Firebase.firestore

        if (email.isNotEmpty()) {
            db.collection("publications")
                .whereEqualTo("author", email)
                .orderBy("timestamp", Query.Direction.DESCENDING) // ⬅️ Nuevo
                .get()
                .addOnSuccessListener { result ->
                    val publications = result.map { doc ->
                        Publication(
                            id = doc.id,
                            content = doc.getString("content") ?: "",
                            timestamp = doc.getTimestamp("timestamp"),
                            likes = doc.get("likes") as? List<String> ?: emptyList()  // <-- Aquí
                        )
                    }





                    val adapter = MyPublicationAdapter(publications,
                        onEdit = { pub -> EditMyPublications(pub.id, pub.content) },
                        onDelete = { pub -> DeletePublication(pub.id) }
                    )

                    recyclerView.adapter = adapter
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error obteniendo publicaciones", e)
                }
        }

    }


    fun EditMyPublications(documentId: String, content: String) {
        val email = intent.getStringExtra("email") ?: "Autor desconocido"

        val editIntent = Intent(this, EditPublicationsActivity::class.java).apply {
            putExtra("documentId", documentId)
            putExtra("content", content)
            putExtra("email", email)
        }
        startActivity(editIntent)
    }


    private fun btnPublishFun() {
        val email = intent.getStringExtra("email") // Obtén el email del Intent actual
        val EditIntent: Intent = Intent(this, EditPublicationsActivity::class.java).apply {
            putExtra("email", email) // Pasa el email al siguiente Intent
        }
        startActivity(EditIntent)
    }

    fun DeletePublication(documentId: String) {
        val db = Firebase.firestore

        db.collection("publications")
            .document(documentId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Publicación eliminada con éxito.")
                showAlertDeleted()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error al eliminar la publicación", e)
            }
    }

    private fun showAlertDeleted() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Publicación eliminada")
        builder.setMessage("La publicación ha sido eliminada exitosamente.")
        builder.setPositiveButton("Aceptar") { _, _ ->
            setup()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
