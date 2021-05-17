package ua.kpi.comsys.lab3_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.slider.Slider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.kpi.comsys.lab3_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.runBtn.setOnClickListener {
            val iterations = binding.iterations.value
            val learningSpeed = binding.learningSpeed.value
            val deadline = binding.deadline.value

            val na = NeuroAlgorithm(
                iterations = iterations,
                learningSpeed = learningSpeed,
                deadline = deadline,
                p = 4,
                points = listOf(0 to 6, 1 to 5, 3 to 3, 2 to 4)
            )

            CoroutineScope(Dispatchers.Default).launch {
                binding.result.text = ""
                na.start {
                    withContext(Dispatchers.Main) {
                        binding.result.text = if (it.success) {
                            """
                                w0: ${it.w0}  w1: ${it.w1}
                                time: ${it.time} ms
                                iterations: ${it.iterations}
                            """.trimIndent()
                        } else {
                            """
                                error: ${it.errorMsg}
                            """.trimIndent()
                        }
                    }
                }
            }
        }

        binding.iterations.setUp(100f, 1000f, 100f, 100f)
        binding.learningSpeed.setUp(0.01f, 0.3f, 0.01f, 0.01f)
        binding.deadline.setUp(0.5f, 5f, 0.5f, 0.5f)
    }

    private fun Slider.setUp(from: Float, to: Float, start: Float, step: Float? = null) {
        valueFrom = from
        valueTo = to
        value = start
        step?.let { stepSize = it }
    }
}
