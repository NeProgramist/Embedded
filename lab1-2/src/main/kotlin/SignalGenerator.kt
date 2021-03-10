import kotlin.math.sin
import kotlin.random.Random

class SignalGenerator(
    private val harmonic: Int,
    private val frequency: Int,
    private val discreteCountDown: Int,
) {
    fun generate(): Map<Double, Double> {
        val res = mutableMapOf<Double, Double>()

        for (i in 0 until harmonic) {
            val currentFreq = (i + 1) * frequency / harmonic

            val a = Random.nextDouble(0.0, 1.0)
            val fi = Random.nextDouble(-Math.PI, Math.PI)

            for (j in 0 until discreteCountDown) {
                val t = j.toDouble()
                res[t] = res.getOrDefault(t, 0.0) + a * sin(currentFreq * t + fi)
            }
        }

        return res
    }

    fun generateList(): Array<Double> {
        val res = Array(discreteCountDown) { 0.0 }
        for (i in 0 until harmonic) {
            val currentFreq = (i + 1) * frequency / harmonic

            val a = Random.nextDouble(0.0, 1.0)
            val fi = Random.nextDouble(-Math.PI, Math.PI)

            for (j in 0 until discreteCountDown) {
                res[j] += a * sin(currentFreq * j + fi)
            }
        }
        return res
    }
}