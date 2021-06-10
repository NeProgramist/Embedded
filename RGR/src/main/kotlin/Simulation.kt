import kscience.plotly.models.TraceType

data class SimulationSettings(
    val tacts: Int,
    val quantum: Int,
    val switchTime: Int,
    val wcetRange: IntRange,
    val intensityRange: IntRange,
)

fun List<TaskDescriptor>.averageWaitingTime() = sumBy { it.waitingTime } / size.toDouble()
fun List<TaskDescriptor>.averageIdleTime(fullTime: Int) = (fullTime - sumBy { it.progress }).toDouble()
fun List<TaskDescriptor>.rejectedPercent() = filter { it.task.wcet > it.progress }.size * 100.0 / size

fun simulate(settings: SimulationSettings) {
    val plotsDrawer = PlotsDrawer()

    val (tacts, quantum, switchTime, wcetRange, intensityRange) = settings

    val fifoAverageWaitingTime = mutableMapOf<Double, Double>()
    val fifoAverageIdleTime = mutableMapOf<Double, Double>()
    val fifoRejectedPercent = mutableMapOf<Double, Double>()
    val rmAverageWaitingTime = mutableMapOf<Double, Double>()
    val rmAverageIdleTime = mutableMapOf<Double, Double>()
    val rmRejectedPercent = mutableMapOf<Double, Double>()
    val edfAverageWaitingTime = mutableMapOf<Double, Double>()
    val edfAverageIdleTime = mutableMapOf<Double, Double>()
    val edfRejectedPercent = mutableMapOf<Double, Double>()

    val fifo = FIFO()
    val rm = RM()
    val edf = EDF()

    intensityRange.forEach { i ->
        val intensity = i.toDouble() / 100
        var tasks = generateTasks(intensity, tacts, wcetRange)

        val schedulerFifo = Scheduler(
            FIFO(),
            quantum,
            tacts,
            tasks.map {it.copy()}.toMutableList(),
            switchTime,
        )
        val resFifo = schedulerFifo.start()

        fifoAverageWaitingTime[intensity] = resFifo.averageWaitingTime()
        fifoAverageIdleTime[intensity] = resFifo.averageIdleTime(schedulerFifo.time)
        fifoRejectedPercent[intensity] = resFifo.rejectedPercent()

//        tasks = generateTasks(intensity, tacts, wcetRange)
        val schedulerRm = Scheduler(
            RM(),
            quantum,
            tacts,
            tasks.map {it.copy()}.toMutableList(),
            switchTime,
        )
        val resRm = schedulerRm.start()

        rmAverageWaitingTime[intensity] = resRm.averageWaitingTime()
        rmAverageIdleTime[intensity] = resRm.averageIdleTime(schedulerRm.time)
        rmRejectedPercent[intensity] = resRm.rejectedPercent()


//        tasks = generateTasks(intensity, tacts, wcetRange)
        val schedulerEdf = Scheduler(
            EDF(),
            quantum,
            tacts,
            tasks.map {it.copy()}.toMutableList(),
            switchTime,
        )
        val resEdf = schedulerEdf.start()

        edfAverageWaitingTime[intensity] = resEdf.averageWaitingTime()
        edfAverageIdleTime[intensity] = resEdf.averageIdleTime(schedulerEdf.time)
        edfRejectedPercent[intensity] = resEdf.rejectedPercent()
        if (i == 20) {
            println()
        }
        println(i)
    }

    plotsDrawer.createPlot(
        "Intensity",
        "AverageWaitingTime",
        fifo.name to fifoAverageWaitingTime,
        rm.name to rmAverageWaitingTime,
        edf.name to edfAverageWaitingTime,
    )
    plotsDrawer.createPlot(
        "Intensity",
        "AverageIdleTime",
        fifo.name to fifoAverageIdleTime,
        rm.name to rmAverageIdleTime,
        edf.name to edfAverageIdleTime,
    )
    plotsDrawer.createBarDiagram(
        "Intensity",
        "RejectedPercent",
        fifo.name to fifoRejectedPercent,
        rm.name to rmRejectedPercent,
        edf.name to edfRejectedPercent,
    )
}

fun main() {
    simulate(SimulationSettings(
        1000,
        3,
        1,
        (1..2),
        (1..500)
    ))
}
