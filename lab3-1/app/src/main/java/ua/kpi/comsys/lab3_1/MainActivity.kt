package ua.kpi.comsys.lab3_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.*
import ua.kpi.comsys.lab3_1.databinding.ActivityMainBinding
import java.lang.Exception
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()

        binding.edittext.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                try {
                    binding.textView.text = "Calculating..."
                    binding.iterations.text = "Iterations: calculating..."
                    val num = it.toString().toLong()
                    GlobalScope.launch {
                        val (a, b, c) = factorization(num)
                        withContext(Dispatchers.Main) {
                            binding.textView.text = "$num = $a * $b"
                            binding.iterations.text = "Iterations: $c"
                        }
                    }
                } catch(e: Exception) {
                    binding.textView.text = "Invalid arguments"
                }
            }
        }
    }

    private fun factorization(n: Long): Triple<Double, Double, Long>{
        val k = ceil(sqrt(n.toDouble()))

        for (a in k.toInt()..n) {
            val b = a.toDouble().pow(2) - n
            val q = sqrt(b)
            if (q.isWhole()) return Triple(a + q, a - q, a - k.toLong() + 1)
        }

        return Triple(1.toDouble(), n.toDouble(), n - k.toLong() + 1)
    }

    private fun Double.isWhole() = this.toLong() - this == 0.0
}