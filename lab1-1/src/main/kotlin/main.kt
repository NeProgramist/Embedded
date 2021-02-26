import java.io.File
import kotlin.system.measureTimeMillis

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
    val calculator = Calculations()

    // general signal plot
    val start = System.currentTimeMillis()
    val signal = signalGenerator.generate()
    val time = System.currentTimeMillis() - start

    val m = calculator.generateExpectedValue(signal)
    val d = calculator.generateDispersion(signal)
    plotsDrawer.createPlot(signal, "t", "x(t)")
    println("""
        -----------------------------------
        m = $m
        d = $d
        time = $time
        -----------------------------------
    """.trimIndent())

    //Time Complexity
    measureSignalComplexity((1..1028), frequency, discreteCountDown, "./res/complexity.csv")

    //Additional task
    val resAd = mutableMapOf<Double, Double>()
    for (N in 2..1024 step 2) {
        val signalAd = SignalGenerator(harmonic, frequency, N).generate()

        val mAd = calculator.generateExpectedValue(signalAd)
        val dAd = calculator.generateDispersion(signalAd)
        val key = N.toDouble()

        resAd[key] = mAd * dAd
    }

    plotsDrawer.createPlot(resAd, "Mx * Dx", "N")
}

/*
    here I measure complexity for number of harmonics
 */
fun measureSignalComplexity(
    harmonics: IntRange,
    frequency: Int,
    discreteCountDown: Int,
    fileName: String,
) {
    val writer = File(fileName)
    val res = mutableMapOf<Double, Double>()

    for (n in harmonics) {
        val time = measureTimeMillis {
            SignalGenerator(n, frequency, discreteCountDown).generate()
        }
        res[n.toDouble()] = time.toDouble()
    }
    writer.writeText(res.entries.joinToString("\n") { el -> "${el.key},${el.value}" })

    PlotsDrawer().createPlot(res, "n", "O(n)")
}