package ua.kpi.comsys.lab3_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.kpi.comsys.lab3_3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var x1 = Int.MIN_VALUE
        var x2 = Int.MIN_VALUE
        var x3 = Int.MIN_VALUE
        var x4 = Int.MIN_VALUE
        var y = Int.MIN_VALUE

        binding.x1.addTextChangedListener {
            x1 = it.toString().toInt()
        }

        binding.x2.addTextChangedListener {
            x2 = it.toString().toInt()
        }

        binding.x3.addTextChangedListener {
            x3 = it.toString().toInt()
        }

        binding.x4.addTextChangedListener {
            x4 = it.toString().toInt()
        }

        binding.y.addTextChangedListener {
            y = it.toString().toInt()
        }

        binding.launch.setOnClickListener {
            val genAlgorithm = GenAlgorithm(
                func = { a, b, c, d -> a * x1 + b * x2 + c * x3 + d * x4 },
                genesDiapason = binding.genesDiapason.text.toString().split(",").map { it.toInt() },
                y = y
            )

            GlobalScope.launch {
                val (population, fitness, stepCount) = genAlgorithm.launch()

                val result = buildString {
                    append("steps: $stepCount\n\n")
                    for (i in population.indices) {
                        append("${population[i]}\nfitness: ${fitness[i]}\n")
                    }
                }

                withContext(Dispatchers.Main) {
                    binding.result.text = result
                }
            }
        }
    }
}