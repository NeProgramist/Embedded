import kscience.plotly.*
import kscience.plotly.models.Trace
import kscience.plotly.models.TraceType

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

    fun createBarDiagram(
        xaxis: String,
        yaxis: String,
        vararg values: Pair<String, Map<Double, Double>>,
    ) {
        val plot = Plotly.plot {
            for (bar in values) {
                bar {
                    name = bar.first
                    x.set(bar.second.keys)
                    y.set(bar.second.values)
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