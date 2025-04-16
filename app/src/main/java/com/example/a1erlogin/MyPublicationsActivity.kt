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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

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
        MyPublicationsTextView = findViewById(R.id.publicationtext)
        EditBtn = findViewById(R.id.EditBtn)
        DeleteBtn = findViewById(R.id.DeleteBtn)

        val db = Firebase.firestore
        val email = intent.getStringExtra("email") ?: ""
        Log.d(TAG, "Email:$email")
        if (email.isNotEmpty()) {
            db.collection("publications")
                .whereEqualTo("author", email)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val content = document.getString("content") ?: ""
                        val id = document.id
                        Log.d(TAG, "content: $content")

                        // Muestra el contenido en el input
                        MyPublicationsTextView.setText(content)

                        // Guarda el ID y configura el botón Editar
                        EditBtn.setOnClickListener {
                            val newContent = MyPublicationsTextView.text.toString()
                            EditMyPublications(id, newContent)
                        }

                        DeleteBtn.setOnClickListener {
                            DeletePublication(id) // <-- llama a la función que borra
                        }

                        // Puedes hacer algo similar para DeleteBtn aquí si quieres
                        break // solo usamos la primera publicación por ahora
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MyPublicationsActivity", "Error obteniendo publicaciones", e)
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
            finish() // Cierra la actividad o redirige a otra pantalla
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
