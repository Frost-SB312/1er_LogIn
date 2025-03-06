package com.example.a1erlogin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC
}

class HomeActivity : AppCompatActivity() {

    lateinit var emailTextView : TextView
    lateinit var providerTextView : TextView
    lateinit var logOutButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home) // Mueve esta línea arriba

        val btnpublications: Button = findViewById(R.id.publications)

        btnpublications.setOnClickListener{
            val intent: Intent = Intent(this, PublicationsActivity::class.java)
        }

        val btnmypublications: Button = findViewById(R.id.mypublications)

        btnmypublications.setOnClickListener{
            val intent: Intent = Intent(this, MyPublicationsActivity::class.java)
        }

        val btnpublish : Button = findViewById(R.id.publish)
        btnpublish.setOnClickListener{
            val intent: Intent = Intent (this, PublishActivity::class.java)
        }

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")


        setup(email ?: "", provider ?: "")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun setup(email:String, provider: String) {

        // Inicializamos las vistas




        title = "Home"
        emailTextView.text = email
        providerTextView.text = provider


        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //
        }
    }
}