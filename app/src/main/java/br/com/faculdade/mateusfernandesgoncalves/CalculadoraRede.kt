package br.com.faculdade.mateusfernandesgoncalves

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.InetAddress
import kotlin.math.pow

class CalculadoraRede : AppCompatActivity() {

    private lateinit var editIp: EditText
    private lateinit var editCidr: EditText
    private lateinit var txtResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora_rede)
        supportActionBar?.hide()

        editIp = findViewById(R.id.editIp)
        editCidr = findViewById(R.id.editCidr)
        txtResultado = findViewById(R.id.txtResultado)
        val btCalcular = findViewById<Button>(R.id.btCalcular)

        btCalcular.setOnClickListener {
            val ipInput = editIp.text.toString()
            val cidrInput = editCidr.text.toString().replace("/", "")

            if (ipInput.isNotEmpty() && cidrInput.isNotEmpty()) {
                try {
                    val cidr = cidrInput.toInt()
                    if (cidr in 1..32) {
                        val calc = SubnetUtils(ipInput, cidr)
                        txtResultado.text = calc.getRelatorio()
                    }
                } catch (e: Exception) {
                    txtResultado.text = "Erro: Verifique o IP e o CIDR"
                }
            }
        }
    }

    // Classe interna para processar a lógica de bits (Infra pura!)
    class SubnetUtils(val ip: String, val cidr: Int) {
        private val ipInt = ipToLong(ip)
        private val mask = (-1 shl (32 - cidr)).toLong() and 0xffffffffL
        private val network = ipInt and mask
        private val broadcast = network or (mask.inv() and 0xffffffffL)

        fun getRelatorio(): String {
            return """
                Mascara: ${longToIp(mask)}
                Rede: ${longToIp(network)}
                Broadcast: ${longToIp(broadcast)}
                Primeiro Host: ${longToIp(network + 1)}
                Último Host: ${longToIp(broadcast - 1)}
                Total Hosts: ${2.0.pow(32 - cidr).toLong()}
            """.trimIndent()
        }

        private fun ipToLong(ip: String): Long {
            val parts = ip.split(".").map { it.toLong() }
            return (parts[0] shl 24) + (parts[1] shl 16) + (parts[2] shl 8) + parts[3]
        }

        private fun longToIp(l: Long): String {
            return "${(l shr 24) and 0xff}.${(l shr 16) and 0xff}.${(l shr 8) and 0xff}.${l and 0xff}"
        }
    }
}