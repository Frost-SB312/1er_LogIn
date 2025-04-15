package com.example.a1erlogin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditPublicationsActivity : AppCompatActivity() {
    lateinit var EditPublicationsTextInput: TextInputEditText
    lateinit var EditPublicationshBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_publications)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setup() {
        title = "Publicar"
        EditPublicationsTextInput = findViewById(R.id.EditPublicationTextInput)
        EditPublicationshBtn = findViewById(R.id.EditBtn)

        EditPublicationshBtn.setOnClickListener {
            if (!EditPublicationsTextInput.text.isNullOrEmpty()) {
                EditPublication()
            }
        }
    }
    fun EditPublication() {
        val db = Firebase.firestore
        val email = intent.getStringExtra("email") ?: "Autor desconocido" // Obtén el email del Intent
        val publication = hashMapOf(
            "content" to EditPublicationsTextInput.text.toString(),
            "author" to email // Usa el email como autor
        )

        // Add a new document with a generated ID
        db.collection("publications")
            .add(publication)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                showAlert()

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                showAlertError()
            }
    }
    private fun showPublications() {
        val email = intent.getStringExtra("email") // Obtén el email del Intent actual
        val publicationsIntent: Intent = Intent(this, PublicationsActivity::class.java).apply {
            putExtra("email", email) // Pasa el email al siguiente Intent
        }
        startActivity(publicationsIntent)
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage("Pubvlicación editada con éxito")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertError() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Hubo un error al editar la publicación")
        builder.setPositiveButton("Aceptar") { _, _ -> showPublications() }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}