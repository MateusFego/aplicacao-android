package br.com.faculdade.mateusfernandesgoncalves

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class InventarioActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val listaAtivos = mutableListOf<Ativo>()
    private lateinit var adapter: AtivoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)

        val recycler = findViewById<RecyclerView>(R.id.recyclerAtivos)
        val editBusca = findViewById<EditText>(R.id.editBuscaAtivo)

        adapter = AtivoAdapter(listaAtivos)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        configurarBotaoSalvarOriginal()

        editBusca.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) { carregarAtivos(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        carregarAtivos("")
    }

    private fun configurarBotaoSalvarOriginal() {
        val btSalvar = findViewById<Button>(R.id.btSalvarAtivo)
        val editNome = findViewById<EditText>(R.id.editNomeAtivo)
        val editIp = findViewById<EditText>(R.id.editIpAtivo)

        btSalvar.text = "Salvar no Firestore"
        btSalvar.setOnClickListener {
            val nome = editNome.text.toString()
            val ip = editIp.text.toString()
            if (nome.isNotEmpty() && ip.isNotEmpty()) {
                val dados = hashMapOf("nome" to nome, "ip" to ip)
                db.collection("Ativos").add(dados).addOnSuccessListener {
                    Toast.makeText(this, "Ativo salvo!", Toast.LENGTH_SHORT).show()
                    editNome.text.clear()
                    editIp.text.clear()
                    carregarAtivos("")
                }
            }
        }
    }

    private fun carregarAtivos(busca: String) {
        var query = db.collection("Ativos").orderBy("nome")
        if (busca.isNotEmpty()) {
            query = query.whereGreaterThanOrEqualTo("nome", busca)
                .whereLessThanOrEqualTo("nome", busca + "\uf8ff")
        }

        query.get().addOnSuccessListener { snapshots ->
            listaAtivos.clear()
            for (doc in snapshots) {
                listaAtivos.add(Ativo(doc.id, doc.getString("nome") ?: "", doc.getString("ip") ?: ""))
            }
            adapter.notifyDataSetChanged()
        }
    }

    fun abrirEditarAtivo(ativo: Ativo) {
        val editNome = findViewById<EditText>(R.id.editNomeAtivo)
        val editIp = findViewById<EditText>(R.id.editIpAtivo)
        val btSalvar = findViewById<Button>(R.id.btSalvarAtivo)

        editNome.setText(ativo.nome)
        editIp.setText(ativo.ip)
        btSalvar.text = "Atualizar Ativo"

        btSalvar.setOnClickListener {
            val novosDados = mapOf("nome" to editNome.text.toString(), "ip" to editIp.text.toString())
            db.collection("Ativos").document(ativo.id).update(novosDados).addOnSuccessListener {
                Toast.makeText(this, "Atualizado!", Toast.LENGTH_SHORT).show()
                editNome.text.clear()
                editIp.text.clear()
                configurarBotaoSalvarOriginal()
                carregarAtivos("")
            }
        }
    }

    fun confirmarExcluir(ativo: Ativo) {
        db.collection("Ativos").document(ativo.id).delete().addOnSuccessListener {
            Toast.makeText(this, "Excluído!", Toast.LENGTH_SHORT).show()
            carregarAtivos("")
        }
    }
}