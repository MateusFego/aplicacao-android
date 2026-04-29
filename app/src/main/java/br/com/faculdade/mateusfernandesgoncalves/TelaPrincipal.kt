package br.com.faculdade.mateusfernandesgoncalves

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TelaPrincipal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)

        // Esconde a barra de título se desejar
        supportActionBar?.hide()
    }
}