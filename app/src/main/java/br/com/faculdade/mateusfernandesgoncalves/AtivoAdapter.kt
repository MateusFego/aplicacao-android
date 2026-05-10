package br.com.faculdade.mateusfernandesgoncalves

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AtivoAdapter(private val lista: MutableList<Ativo>) : RecyclerView.Adapter<AtivoAdapter.AtivoViewHolder>() {

    class AtivoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNome = view.findViewById<TextView>(android.R.id.text1)
        val txtIp = view.findViewById<TextView>(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AtivoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return AtivoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AtivoViewHolder, position: Int) {
        val ativo = lista[position]
        holder.txtNome.text = ativo.nome
        holder.txtIp.text = ativo.ip
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            val options = arrayOf("Editar", "Excluir")

            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Opções para ${ativo.nome}")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> (context as InventarioActivity).abrirEditarAtivo(ativo)
                        1 -> (context as InventarioActivity).confirmarExcluir(ativo)
                    }
                }.show()
            true
        }
    }


    override fun getItemCount() = lista.size

    fun atualizarLista(novaLista: List<Ativo>) {
        lista.clear()
        lista.addAll(novaLista)
        notifyDataSetChanged()
    }
}