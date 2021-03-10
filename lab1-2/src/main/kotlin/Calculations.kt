import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class Calculations {
    // Мат ожидание
    fun generateExpectedValue(data: Map<Double, Double>) = data.values.sum() / data.size

    fun generateExpectedValue(data: Array<Double>) = data.sum() / data.size

    fun generateDispersion(data: Map<Double, Double>): Double {
        val m = generateExpectedValue(data)

        return data.values.sumByDouble { x -> (x - m).pow(2.0)  } / (data.size - 1)
    }

    fun generateDispersion(data: Array<Double>): Double {
        val m = generateExpectedValue(data)

        return data.sumByDouble { x -> (x - m).pow(2.0)  } / (data.size - 1)
    }

    fun calculateCorrelation(data1: Map<Double, Double>, data2: Map<Double, Double>): Double {
        var result = 0.0
        val size = max(data1.size, data2.size)

        val m1 = generateExpectedValue(data1)
        val m2 = generateExpectedValue(data2)
        val d1 = generateDispersion(data1)
        val d2 = generateDispersion(data2)

        for (n in 0 until size) {
            result += (data1.getOrDefault(n.toDouble(), 0.0) - m1) * (data2.getOrDefault(n.toDouble(), 0.0) - m2)
        }

        return result / (size * sqrt(d1) * sqrt(d2))
    }

    fun calculateCorrelation(data1: Array<Double>, data2: Array<Double>): Double {
        var result = 0.0
        val size = max(data1.size, data2.size)

        val m1 = generateExpectedValue(data1)
        val m2 = generateExpectedValue(data2)
        val d1 = generateDispersion(data1)
        val d2 = generateDispersion(data2)


        for (n in 0 until size) {
            val v1 = if (data1.size > n) data1[n] else 0.0
            val v2 = if (data2.size > n) data2[n] else 0.0
            result += (v1 - m1) * (v2 - m2)
        }

        return result / (size * sqrt(d1) * sqrt(d2))
    }
}