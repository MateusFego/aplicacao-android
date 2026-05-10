package br.com.faculdade.mateusfernandesgoncalves

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth // Importação que faltava

class TelaPrincipal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)

        supportActionBar?.hide()

        // 1. Perfil (Corrigido para TelaPerfil)
        findViewById<Button>(R.id.bt_perfil).setOnClickListener {
            startActivity(Intent(this, TelaPerfil::class.java))
        }

        // 2. Calculadora
        findViewById<Button>(R.id.bt_calculadora).setOnClickListener {
            startActivity(Intent(this, CalculadoraRede::class.java))
        }

        // 3. Inventário
        findViewById<Button>(R.id.bt_inventario).setOnClickListener {
            startActivity(Intent(this, InventarioActivity::class.java))
        }

        // 4. Status
        findViewById<Button>(R.id.bt_status).setOnClickListener {
            startActivity(Intent(this, StatusActivity::class.java))
        }

        // 5. Hardening
        findViewById<Button>(R.id.bt_hardening).setOnClickListener {
            startActivity(Intent(this, HardeningActivity::class.java))
        }

        // 6. Deslogar (Agora dentro do onCreate corretamente)
        findViewById<Button>(R.id.bt_deslogar).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
            finish()
        }
    }
}