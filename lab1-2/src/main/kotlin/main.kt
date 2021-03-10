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
//    val plotsDrawer = PlotsDrawer()
//    val calculations = Calculations()
//
//    val autoCorrelation = mutableMapOf<Double, Double>()
//    val signal = signalGenerator.generate()
//    for (bias in 0 until 1024) {
//        val biased = signal.filter { p -> p.key >= bias }.mapKeys { p -> p.key - bias }
//        autoCorrelation[bias.toDouble()] = calculations.calculateCorrelation(signal, biased)
//    }
//
//    val correlation = mutableMapOf<Double, Double>()
//    val signal2 = signalGenerator.generate()
//    for (bias in 0 until 1024) {
//        val biased = signal2.filter { p -> p.key >= bias }.mapKeys { p -> p.key - bias }
//        correlation[bias.toDouble()] = calculations.calculateCorrelation(signal, biased)
//    }


    testDifferentDataStructures(signalGenerator)
//
//    plotsDrawer.createPlot(autoCorrelation, "T(bias)", "AutoCorrelation")
//    plotsDrawer.createPlot(correlation, "T(bias)", "Correlation")
}

fun testDifferentDataStructures(signalGenerator: SignalGenerator) {
    val calculations = Calculations()

    val signalMap1 = signalGenerator.generate()
    val signalMap2 = signalGenerator.generate()

    val timeMap = mutableListOf<Long>()
    repeat(10) {
        val t = measureTimeMillis {
            for (bias in 0 until 1024) {
                val biased = signalMap2.filter { p -> p.key >= bias }.mapKeys { p -> p.key - bias }
                calculations.calculateCorrelation(signalMap1, biased)
            }
        }

        timeMap.add(t)
    }

    val signalArray1 = signalGenerator.generateList()
    val signalArray2 = signalGenerator.generateList()

    val timeArray = mutableListOf<Long>()
    repeat(10) {
        val t = measureTimeMillis {
            for (bias in 0 until 1024) {
                val biased = signalArray2.drop(bias).toTypedArray()
                calculations.calculateCorrelation(signalArray1, biased)
            }
        }

        timeArray.add(t)
    }

    println("""
        timeMap: ${timeMap.joinToString(" ")}
        timeArray: ${timeArray.joinToString(" ") }
    """.trimIndent())
}
