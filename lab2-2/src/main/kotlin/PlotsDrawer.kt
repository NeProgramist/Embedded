import kscience.plotly.*

class PlotsDrawer {
    fun createPlot(
        xaxis: String,
        yaxis: String,
        vararg values: Map<Double, Double>,
    ) {
        val plot = Plotly.plot {
            for (plot in values) {
                trace {

                    x.set(plot.keys)
                    y.set(plot.values)
                }
            }
            layout {
                width = 1900
                height = 800
                margin {
                    l = 20
                    t = 20
                    r = 20
                    b = 20
                }

                xaxis {
                    margin { l = 50 }
                    title = xaxis
                }

                yaxis {
                    margin { b = 50 }
                    title = yaxis
                }
            }
        }

        plot.makeFile()
    }

    fun createPlot(
        xaxis: String,
        yaxis: String,
        values: List<Double>,
    ) {
        val map = values
            .indices
            .map { it.toDouble() }
            .zip(values).toMap()
        createPlot(xaxis, yaxis, map)
    }
}