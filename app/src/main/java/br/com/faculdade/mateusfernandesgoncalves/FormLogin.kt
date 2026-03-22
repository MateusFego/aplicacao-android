package br.com.faculdade.imepac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FormLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)

        // No comando abaixo mando esconder o Toolbar
        supportActionBar?.hide()
    }
}