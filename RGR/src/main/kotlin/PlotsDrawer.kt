import kscience.plotly.Plotly
import kscience.plotly.layout
import kscience.plotly.makeFile
import kscience.plotly.trace

class PlotsDrawer {
    fun createPlot(
        xaxis: String,
        yaxis: String,
        vararg values: Pair<String, Map<Double, Double>>,
    ) {
        val plot = Plotly.plot {
            for (plot in values) {
                trace {
                    name = plot.first
                    x.set(plot.second.keys)
                    y.set(plot.second.values)
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


}