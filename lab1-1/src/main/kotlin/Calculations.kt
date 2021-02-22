import kotlin.math.pow

class Calculations {
    // Мат ожидание
    fun generateExpectedValue(data: Map<Double, Double>) = data.values.sum() / data.size

    fun generateDispersion(data: Map<Double, Double>): Double {
        val m = generateExpectedValue(data)

        return data.values.sumByDouble { x -> (x - m).pow(2.0)  } / (data.size - 1)
    }
}