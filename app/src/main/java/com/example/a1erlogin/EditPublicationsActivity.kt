package com.example.a1erlogin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditPublicationsActivity : AppCompatActivity() {
    private lateinit var editTextInput: TextInputEditText
    private lateinit var editBtn: Button
    private var documentId: String? = null
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_publications)

        editTextInput = findViewById(R.id.EditPublicationTextInput)
        editBtn = findViewById(R.id.EditBtn)

        // Recupera los datos del Intent
        documentId = intent.getStringExtra("documentId")
        val content = intent.getStringExtra("content")
        email = intent.getStringExtra("email")

        // Establece el texto actual
        editTextInput.setText(content)

        editBtn.setOnClickListener {
            if (!editTextInput.text.isNullOrEmpty()) {
                updatePublication()
            }
        }
    }

    private fun updatePublication() {
        val db = Firebase.firestore
        val newContent = editTextInput.text.toString()

        documentId?.let { id ->
            db.collection("publications")
                .document(id)
                .update("content", newContent)
                .addOnSuccessListener {
                    Log.d(TAG, "Publicación actualizada")
                    showAlert("Éxito", "Publicación actualizada correctamente")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al actualizar", e)
                    showAlert("Error", "No se pudo actualizar la publicación")
                }
        }
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar") { _, _ ->
            goBackToMyPublications()
        }
        builder.create().show()
    }

    private fun goBackToMyPublications() {
        val intent = Intent(this, MyPublicationsActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }
}

