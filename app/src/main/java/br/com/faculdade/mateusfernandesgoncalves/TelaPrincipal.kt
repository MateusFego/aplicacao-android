package br.com.faculdade.mateusfernandesgoncalves

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TelaPrincipal : AppCompatActivity() {

    private lateinit var btPerfil: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)

        supportActionBar?.hide()
        iniciarComponentes()

        btPerfil.setOnClickListener {
            // Navega para a tela de perfil [cite: 5, 76]
            val intent = Intent(this, TelaPerfil::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarComponentes() {
        // Agora o ID bt_perfil existe no XML acima
        btPerfil = findViewById(R.id.bt_perfil)
    }
}