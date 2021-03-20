import kotlin.math.*
import kotlin.system.measureTimeMillis

/*
    Grade book number - 8408
    Variant - 8
 */
const val harmonic = 6
const val frequency = 1500
const val discreteCountDown = 1024

fun main() {

    val signalGenerator = SignalGenerator(harmonic, frequency, discreteCountDown)
    val plotsDrawer = PlotsDrawer()

    val signal = signalGenerator.generateArray().toList()
    val resultFft = fft(signal).map { it.abs() / signal.size }.filterIndexed { index, _ -> index < signal.size / 2 }
    val resultDft = dft(signal).map { it.abs() / signal.size }.filterIndexed { index, _ -> index < signal.size / 2 }

    plotsDrawer.createPlot("p(fft)", "A", resultFft)
    plotsDrawer.createPlot("p(dft)", "A", resultDft)

    val (fft, dft) = checkTime()

    plotsDrawer.createPlot("discreteCountDown", "time", fft, dft)

    for (k in dft.keys) {
        val d = dft[k]
        val f = fft[k]
        if (d == null || f == null) break
        println("""
            $k: dft - ${d}s, fft - ${f}s, diff - ${d/f}
        """.trimIndent())
    }
}

fun Double.round2(num: Int) = round(this * 10.0.pow(num)) / 10.0.pow(num)

fun W(n: Int): (Int) -> Complex {
    val table = mutableMapOf<Int, Complex>()
    val value = 2 * PI / n

    return { k: Int ->
        val arg = (value * k).round2(5)
        table.getOrPut(k % n) {
            Complex(cos(arg), -sin(arg))
        }
    }
}

fun dft(signal: List<Double>): List<Complex> {
    val n = signal.size
    val result = MutableList(n) { complex }

    for (p in 0 until n) {
        var f = complex
        for (k in 0 until n) {
            f += result[p] + signal[k] *
                    (cos(2 * PI * p * k / n) - sin(2 * PI * p * k / n) * (1.0).j)
        }
        result[p] = f
    }
    return result
}

fun fft(signal: List<Double>): List<Complex> {
    val n = signal.size
    val m = n / 2
    val w = W(n)
    return when  {
        n <= 32 -> dft(signal)
        else -> {
            val even = MutableList(m) { 0.0 }
            val odd = MutableList(m) { 0.0 }

            for (i in 0 until m) {
                even[i] = signal[2 * i]
                odd[i] = signal[2 * i + 1]
            }

            val fftEven = fft(even)
            val fftOdd = fft(odd)

            val result = MutableList(n) { complex }

            for (i in 0 until m) {
                result[i] = fftEven[i] + w(i) * fftOdd[i]
                result[m + i] = fftEven[i] - w(i) * fftOdd[i]
            }

            result
        }
    }
}

fun checkTime(): Pair<Map<Double, Double>, Map<Double, Double>> {
    val timeFft = mutableMapOf<Double, Double>()
    val timeDft = mutableMapOf<Double, Double>()

    var df = 1024
    while (df < 1_048_576) {
        val signal = SignalGenerator(harmonic, frequency, df).generateArray().toList()
        val fft = measureTimeMillis {
            fft(signal)
        }
        timeFft[df.toDouble()] = fft / 1000.0
        df *= 2
        if (fft > 1_000) break
    }

    var dd = 1024
    while (dd < 1_048_576) {
        val signal = SignalGenerator(harmonic, frequency, dd).generateArray().toList()

        val dft = measureTimeMillis {
            dft(signal)
        }
        timeDft[dd.toDouble()] = dft / 1000.0
        dd *= 2
        if (dft > 5_000) break
    }

    return timeFft to timeDft
}
