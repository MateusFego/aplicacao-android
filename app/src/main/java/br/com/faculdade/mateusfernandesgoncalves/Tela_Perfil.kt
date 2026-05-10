package br.com.faculdade.mateusfernandesgoncalves

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaPerfil : AppCompatActivity() {

    private lateinit var editNome: EditText
    private lateinit var txtEmail: TextView
    private val db = FirebaseFirestore.getInstance()
    private val usuarioID = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_perfil)

        supportActionBar?.hide()

        editNome = findViewById(R.id.edit_nome_perfil)
        txtEmail = findViewById(R.id.txt_email_perfil)
        val btSalvar = findViewById<Button>(R.id.bt_salvar_perfil)

        carregarDadosUsuario()

        btSalvar.setOnClickListener {
            salvarAlteracoes()
        }
    }

    private fun carregarDadosUsuario() {
        if (usuarioID != null) {
            // Busca o documento do usuário pelo UID logado
            db.collection("Usuarios").document(usuarioID)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        editNome.setText(document.getString("nome"))
                        txtEmail.text = "Email: ${document.getString("email")}"
                    }
                }
        }
    }

    private fun salvarAlteracoes() {
        val novoNome = editNome.text.toString()

        if (usuarioID != null && novoNome.isNotEmpty()) {
            // Atualiza apenas o campo "nome" no Firestore
            db.collection("Usuarios").document(usuarioID)
                .update("nome", novoNome)
                .addOnSuccessListener {
                    Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}