package br.com.faculdade.mateusfernandesgoncalves

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class HardeningActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hardening)
        supportActionBar?.hide()

        container = findViewById(R.id.containerChecklist)
        val editNovo = findViewById<EditText>(R.id.editNovoHardening)
        val btAdd = findViewById<Button>(R.id.btAddHardening)

        btAdd.setOnClickListener {
            val tarefa = editNovo.text.toString().trim()
            if (tarefa.isNotEmpty()) {
                salvarItemHardening(tarefa)
                editNovo.text.clear()
            }
        }

        carregarItensHardening()
    }

    private fun salvarItemHardening(texto: String) {
        val item = hashMapOf(
            "tarefa" to texto,
            "concluido" to false,
            "timestamp" to System.currentTimeMillis()
        )
        db.collection("Hardening").add(item).addOnSuccessListener {
            carregarItensHardening()
        }
    }

    private fun carregarItensHardening() {
        db.collection("Hardening").orderBy("timestamp").get().addOnSuccessListener { result ->
            container.removeAllViews()
            for (document in result) {
                val tarefa = document.getString("tarefa") ?: ""
                val concluido = document.getBoolean("concluido") ?: false

                val cb = CheckBox(this)
                cb.text = tarefa
                cb.isChecked = concluido
                cb.textSize = 18f
                cb.setPadding(10, 20, 10, 20)

                // Atualiza o status no Firebase ao clicar
                cb.setOnCheckedChangeListener { _, isChecked ->
                    db.collection("Hardening").document(document.id).update("concluido", isChecked)
                }

                // Clique longo para remover um item do checklist
                cb.setOnLongClickListener {
                    db.collection("Hardening").document(document.id).delete().addOnSuccessListener {
                        Toast.makeText(this, "Item removido", Toast.LENGTH_SHORT).show()
                        carregarItensHardening()
                    }
                    true
                }

                container.addView(cb)
            }
        }
    }
}