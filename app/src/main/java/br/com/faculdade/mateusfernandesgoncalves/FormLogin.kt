package br.com.faculdade.mateusfernandesgoncalves

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FormLogin : AppCompatActivity() {

    private lateinit var edit_email: EditText
    private lateinit var edit_senha: EditText
    private lateinit var bt_entrada: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var text_tela_cadastro: TextView
    private lateinit var txt_nome_app: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)

        supportActionBar?.hide()
        IniciarComponents()
        configurarDestaqueCadastro()

        text_tela_cadastro.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)
        }

        bt_entrada.setOnClickListener {
            val email = edit_email.text.toString()
            val senha = edit_senha.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar = Snackbar.make(it, "Preencha todos os campos!", Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(Color.WHITE)
                snackbar.setTextColor(Color.BLACK)
                snackbar.show()
            } else {
                AutenticarUsuario()
            }
        }
    }

    private fun configurarDestaqueCadastro() {
        // Criando o efeito de link na palavra "aqui"
        val texto = "Crie uma conta clicando aqui"
        val spannable = SpannableString(texto)

        // Cor de destaque (Azul) para os últimos 4 caracteres ("aqui")
        val corDestaque = ForegroundColorSpan(Color.parseColor("#2980B9"))
        spannable.setSpan(corDestaque, 24, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Adiciona sublinhado apenas no "aqui"
        spannable.setSpan(UnderlineSpan(), 24, 28, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        text_tela_cadastro.text = spannable
    }

    private fun IniciarComponents() {
        edit_email = findViewById(R.id.edit_email)
        edit_senha = findViewById(R.id.edit_senha)
        bt_entrada = findViewById(R.id.bt_entrada)
        progressBar = findViewById(R.id.progress_bar)
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro)
        txt_nome_app = findViewById(R.id.txt_nome_app)
    }

    fun AutenticarUsuario() {
        val email = edit_email.text.toString()
        val senha = edit_senha.text.toString()

        progressBar.visibility = View.VISIBLE

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.GONE
                    val intent = Intent(this@FormLogin, TelaPrincipal::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    progressBar.visibility = View.GONE
                    val erro = task.exception?.message ?: "Erro ao autenticar"
                    Snackbar.make(findViewById(android.R.id.content), erro, Snackbar.LENGTH_LONG).show()
                }
            }
    }
}