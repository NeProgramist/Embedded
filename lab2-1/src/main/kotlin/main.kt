import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun main() {
    /*
        Grade book number - 8408
        Variant - 8
     */
    val harmonic = 6
    val frequency = 1500
    val discreteCountDown = 1024

    val signalGenerator = SignalGenerator(harmonic, frequency, discreteCountDown)
    val plotsDrawer = PlotsDrawer()

    val result = mutableMapOf<Double, Double>()
    val signal = signalGenerator.generate()
    for (p in 0 until discreteCountDown) {
        var f = Complex(0.0, 0.0)
        for (k in 0 until discreteCountDown) {
            f += result.getOrDefault(p.toDouble(), 0.0) + signal.getOrDefault(k.toDouble(), 0.0) *
                    (cos(2 * PI * p * k / discreteCountDown) - sin(2 * PI * p * k / discreteCountDown) * (1.0).j)
        }
        result[p.toDouble()] = f.abs() / discreteCountDown
    }

    val normalized = result.filter { it.key < discreteCountDown / 2 }

    plotsDrawer.createPlot(result, "p", "A")
    plotsDrawer.createPlot(normalized, "p", "A")
}

