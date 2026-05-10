package br.com.faculdade.mateusfernandesgoncalves

import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class StatusActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)
        supportActionBar?.hide()

        val editBusca = findViewById<EditText>(R.id.editBuscaCVE)
        val btBuscar = findViewById<Button>(R.id.btBuscarCVE)
        val txtResultado = findViewById<TextView>(R.id.txtResultadoCVE)
        val progress = findViewById<ProgressBar>(R.id.progressCVE)

        btBuscar.setOnClickListener {
            val termo = editBusca.text.toString().trim()
            if (termo.isNotEmpty()) {
                progress.visibility = View.VISIBLE
                txtResultado.text = "Consultando base de dados NIST (NVD)..."
                consultarCVE(termo, txtResultado, progress)
            } else {
                Toast.makeText(this, "Digite um software ou vendor (ex: nginx)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun consultarCVE(termo: String, txtResultado: TextView, progress: ProgressBar) {
        val url = "https://services.nvd.nist.gov/rest/json/cves/2.0?keywordSearch=$termo&resultsPerPage=10"

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "Mozilla/5.0")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    progress.visibility = View.GONE
                    txtResultado.text = "Erro de conexão: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                runOnUiThread {
                    progress.visibility = View.GONE
                    try {
                        val json = JSONObject(body ?: "")
                        val vulnerabilities = json.optJSONArray("vulnerabilities")

                        if (vulnerabilities == null || vulnerabilities.length() == 0) {
                            txtResultado.text = "Nenhuma vulnerabilidade encontrada para '$termo'."
                        } else {
                            val sb = StringBuilder()
                            val total = json.optInt("totalResults", 0)

                            sb.append("✅ Sucesso! $total falhas catalogadas.\n")
                            sb.append("Abaixo, links para detalhes e correção:\n")
                            sb.append("==========================\n\n")

                            for (i in 0 until vulnerabilities.length()) {
                                val vObj = vulnerabilities.getJSONObject(i).getJSONObject("cve")
                                val id = vObj.optString("id", "N/A")

                                // Pegando a descrição
                                val descArray = vObj.optJSONArray("descriptions")
                                val descricao = descArray?.getJSONObject(0)?.optString("value") ?: "Sem descrição."

                                // Pegando a nota CVSS (Severidade)
                                val metrics = vObj.optJSONObject("metrics")
                                val cvssMetricV3 = metrics?.optJSONArray("cvssMetricV31")?.optJSONObject(0)
                                    ?: metrics?.optJSONArray("cvssMetricV30")?.optJSONObject(0)
                                val score = cvssMetricV3?.optJSONObject("cvssData")?.optDouble("baseScore", 0.0) ?: 0.0

                                sb.append("🛡️ ID: $id\n")
                                sb.append("📊 Pontuação CVSS: $score\n")
                                sb.append("📝 Resumo: ${descricao.take(150)}...\n")
                                // Link para remediação e detalhes técnicos
                                sb.append("🔗 Detalhes e Correção: https://nvd.nist.gov/vuln/detail/$id\n")
                                sb.append("\n--------------------------\n\n")
                            }

                            txtResultado.text = sb.toString()
                            // TORNA OS LINKS CLICÁVEIS
                            Linkify.addLinks(txtResultado, Linkify.WEB_URLS)
                        }
                    } catch (e: Exception) {
                        Log.e("CVE_ERROR", "Erro no Parse", e)
                        txtResultado.text = "Erro ao processar dados. Tente 'linux' ou 'nginx'."
                    }
                }
            }
        })
    }
}