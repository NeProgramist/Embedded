import kotlin.math.sqrt

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
    val calculations = Calculations()


    val autoCorrelation = mutableMapOf<Double, Double>()
    val signal = signalGenerator.generate()
    for (bias in 0..1024) {
        val biased = signal.filter { p -> p.key >= bias }.mapKeys { p -> p.key - bias }
        autoCorrelation[bias.toDouble()] = calculations.calculateCorrelation(signal, biased)
    }

    val correlation = mutableMapOf<Double, Double>()
    val signal2 = signalGenerator.generate()
    for (bias in 0..1024) {
        val biased = signal2.filter { p -> p.key >= bias }.mapKeys { p -> p.key - bias }
        correlation[bias.toDouble()] = calculations.calculateCorrelation(signal, biased)
    }

    plotsDrawer.createPlot(autoCorrelation, "T(bias)", "AutoCorrelation")
    plotsDrawer.createPlot(correlation, "T(bias)", "Correlation")
}
