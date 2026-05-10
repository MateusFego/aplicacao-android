package br.com.faculdade.mateusfernandesgoncalves

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaPerfil : AppCompatActivity() {
    private lateinit var edit_email: EditText
    private lateinit var edit_usuario: EditText
    private lateinit var bt_sair: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_perfil)

        supportActionBar?.hide()
        IniciarComponentes()

        db = FirebaseFirestore.getInstance()

        bt_sair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@TelaPerfil, FormLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val userEmail = FirebaseAuth.getInstance().currentUser?.email

        if (userEmail != null) {
            edit_email.setText(userEmail)
            buscarNomeDoEmail(userEmail)
        }
    }

    fun buscarNomeDoEmail(email: String) {
        val usuariosRef = db.collection("Usuarios")
        val query = usuariosRef.whereEqualTo("email", email)

        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val documento = querySnapshot.documents[0]
                val nome = documento.getString("nome")

                if (nome != null) {
                    edit_usuario.setText(nome)
                }
            }
        }.addOnFailureListener { e ->
            println("Erro ao buscar documento: $e")
        }
    }

    private fun IniciarComponentes() {
        edit_email = findViewById(R.id.textEmailUser)
        edit_usuario = findViewById(R.id.textNomeUser)
        bt_sair = findViewById(R.id.bt_sair)
    }
}