data class SimulationSettings(
    val tacts: Int,
    val quantum: Int,
    val switchTime: Int,
    val wcetRange: IntRange,
    val intensityRange: IntRange,
)

fun List<TaskDescriptor>.averageWaitingTime() = this.sumBy { it.waitingTime } / size.toDouble()
fun List<TaskDescriptor>.averageIdleTime(fullTime: Int) = (fullTime - this.sumBy { it.progress }) / size.toDouble()

fun simulate(settings: SimulationSettings) {
    val plotsDrawer = PlotsDrawer()

    val (tacts, quantum, switchTime, wcetRange, intensityRange) = settings

    val fifoAverageWaitingTime = mutableMapOf<Double, Double>()
    val fifoAverageIdleTime = mutableMapOf<Double, Double>()
    val rmAverageWaitingTime = mutableMapOf<Double, Double>()
    val rmAverageIdleTime = mutableMapOf<Double, Double>()
    val edfAverageWaitingTime = mutableMapOf<Double, Double>()
    val edfAverageIdleTime = mutableMapOf<Double, Double>()

    val fifo = FIFO()
    val rm = RM()
    val edf = EDF()

    intensityRange.forEach { i ->
        val intensity = i.toDouble()
        var tasks = generateTasks(intensity, tacts, wcetRange)

        val schedulerFifo = Scheduler(
            fifo,
            quantum,
            tasks.toMutableList(),
            switchTime,
        )
        val resFifo = schedulerFifo.start()

        fifoAverageWaitingTime[intensity] = resFifo.averageWaitingTime()
        fifoAverageIdleTime[intensity] = resFifo.averageIdleTime(schedulerFifo.time)

        tasks = generateTasks(intensity, tacts, wcetRange)
        val schedulerRm = Scheduler(
            rm,
            quantum,
            tasks.toMutableList(),
            switchTime,
        )
        val resRm = schedulerRm.start()

        rmAverageWaitingTime[intensity] = resRm.averageWaitingTime()
        rmAverageIdleTime[intensity] = resRm.averageIdleTime(schedulerRm.time)

        tasks = generateTasks(intensity, tacts, wcetRange)
        val schedulerEdf = Scheduler(
            edf,
            quantum,
            tasks.toMutableList(),
            switchTime,
        )
        val resEdf = schedulerEdf.start()

        edfAverageWaitingTime[intensity] = resEdf.averageWaitingTime()
        edfAverageIdleTime[intensity] = resEdf.averageIdleTime(schedulerEdf.time)
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
}

fun main() {
    simulate(SimulationSettings(
        5000,
        3,
        1,
        (1..10),
        (1..30)
    ))
}
