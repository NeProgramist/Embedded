import kscience.plotly.*

class PlotsDrawer {
    fun createPlot(
        values: Map<Double, Double>,
        xaxis: String,
        yaxis: String
    ) {
        val xValues = values.keys
        val yValues = values.values

        val plot = Plotly.plot {
            trace {
                x.set(xValues)
                y.set(yValues)
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
}