package com.example.a1erlogin

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

class MyPublicationsActivity : AppCompatActivity() {
    lateinit var MypublicationsTextInput: TextInputEditText
    lateinit var EditBtn: Button
    lateinit var DeleteBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_my_publications)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setup() {
        title = "Publicar"
        MypublicationsTextInput = findViewById(R.id.textView4)
        EditBtn = findViewById(R.id.EditBtn)
        DeleteBtn = findViewById(R.id.DeleteBtn)

        EditBtn.setOnClickListener {
            if (!MypublicationsTextInput.text.isNullOrEmpty()) {
                EditMyPublications()
            }
        }
    }
    fun EditMyPublications() {
        val db = com.google.firebase.ktx.Firebase.firestore
        val email =
            intent.getStringExtra("email") ?: "Autor desconocido" // Obtén el email del Intent
        val publication = hashMapOf(
            "content" to MypublicationsTextInput.text.toString(),
            "author" to email // Usa el email como autor
        )
    }
    private fun btnPublishFun() {
        val email = intent.getStringExtra("email") // Obtén el email del Intent actual
        val EditIntent: Intent = Intent(this, EditPublicationsActivity::class.java).apply {
            putExtra("email", email) // Pasa el email al siguiente Intent
        }
        startActivity(EditIntent)
    }

    fun DeletePublication(){

    }
    }
