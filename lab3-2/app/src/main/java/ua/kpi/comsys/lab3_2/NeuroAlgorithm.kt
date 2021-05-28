package ua.kpi.comsys.lab3_2

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

data class NeuroResult(
    val success: Boolean,
    val w0: Float = 0f,
    val w1: Float = 0f,
    val iterations: Int = 0,
    val time: Long = 0,
    val errorMsg: String? = null,
)

class NeuroAlgorithm(
    private val iterations: Float,
    private val deadline: Float,
    private val learningSpeed: Float,
    private val points: List<Pair<Int, Int>>,
    private val p: Int,
) {
    private var job: Job? = null
    private var cancelJob: Job? = null

    fun start(onDone: suspend (NeuroResult) -> Unit) {
        job = GlobalScope.launch {
            var w0 = 0f
            var w1 = 0f
            var curIteration = 0
            val nr: NeuroResult

            val time = measureTimeMillis {
                for (iteration in 1..iterations.toInt()) {
                    yield()
                    curIteration = iteration
                    val pointIndex = (iteration - 1) % points.size
                    val point = points[pointIndex]
                    val y = w0 * point.first + w1 * point.second

                    val res = points.mapIndexed { index, pair ->
                        val tempY = w0 * pair.first + w1 * pair.second
                        if (index >= points.size / 2) {
                            tempY < p
                        } else {
                            tempY > p
                        }
                    }

                    if (res.all { it }) break


                    val d = p - y
                    w0 += d * point.first * learningSpeed
                    w1 += d * point.second * learningSpeed

                }
            }

            nr = if (iterations.toInt() == curIteration) {
                NeuroResult(
                    success = false,
                    errorMsg = "Iteration limit exceeded"
                )
            } else {
                NeuroResult(
                    success = true,
                    w0 = w0,
                    w1 = w1,
                    iterations = curIteration,
                    time = time
                )
            }

            cancelJob?.cancel()
            onDone(nr)
        }

        cancelJob = GlobalScope.launch {
            delay((deadline * 1000).toLong())
            yield()
            job?.cancel()

            val nr = NeuroResult(
                success = false,
                errorMsg = "Deadline time exceeded",
            )

            onDone(nr)
        }
    }
}

suspend fun main() {
    val res = sortedMapOf<Float, Float>()

    for (i in 1..300) {
        val ls = i / 1000f
        val na = NeuroAlgorithm(
            iterations = 400f,
            learningSpeed = i / ls,
            deadline = 4f,
            p = 4,
            points = listOf(0 to 6, 1 to 5, 3 to 3, 2 to 4)
        )

        val start = System.currentTimeMillis()

        na.start {
            val time = System.currentTimeMillis() - start
            res[ls] = time / 1000f
        }
    }

    delay(4000)

    for (i in res) {
        println("${i.value}")
    }
}
