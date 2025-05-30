package com.example.a1erlogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.edit

enum class ProviderType {
    BASIC
}

class HomeActivity : AppCompatActivity() {

    lateinit var logOutButton: Button
    lateinit var btnPublications: Button
    lateinit var btnMyPublications: Button
    lateinit var btnPublish: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home) // Mueve esta línea arriba



        setup()

        // Data Saving
        getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit() {
            val email = intent.getStringExtra("email")
            val provider = intent.getStringExtra("provider")
            putString("email", email)
            putString("provider", provider)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun setup() {

        title = "Home"

        // Inicializamos las vistas
        logOutButton = findViewById(R.id.logOutButton)
        btnPublications = findViewById(R.id.publications)
        btnPublish = findViewById(R.id.publish)
        btnMyPublications = findViewById(R.id.mypublications)




        btnMyPublications.setOnClickListener {
            btnMyPublicationsFun()
        }


        btnPublish.setOnClickListener {
            btnPublishFun()
        }

        btnPublications.setOnClickListener {
            btnPublicationsFun()
        }

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit() {
                clear()
            }
            onBackPressed()
            //
        }

    }
        private fun btnPublicationsFun() {
            val email = intent.getStringExtra("email") // Obtén el email del Intent actual
            val publicationsIntent: Intent = Intent(this, PublicationsActivity::class.java).apply {
                putExtra("email", email) // Pasa el email al siguiente Intent
            }
            startActivity(publicationsIntent)
        }

    private fun btnMyPublicationsFun() {
        val email = intent.getStringExtra("email") // Obtén el email del Intent actual
        val myPublicationsIntent: Intent = Intent(this, MyPublicationsActivity::class.java).apply {
            putExtra("email", email) // Pasa el email al siguiente Intent
        }
        startActivity(myPublicationsIntent)
    }

    private fun btnPublishFun() {
        val email = intent.getStringExtra("email") // Obtén el email del Intent actual
        val publishIntent: Intent = Intent(this, PublishActivity::class.java).apply {
            putExtra("email", email) // Pasa el email al siguiente Intent
        }
        startActivity(publishIntent)
    }
}