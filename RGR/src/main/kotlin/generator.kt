import kotlin.math.ln
import kotlin.random.Random

val tasksSpread = listOf(0.2, 0.3, 0.5)

fun generateTasks(intensity: Double, tacts: Int, wcetRange: IntRange): List<TaskDescriptor> {
    val tasks = mutableListOf<TaskDescriptor>()

    repeat(tasksSpread.size) {
        val fr = intensity * tasksSpread[it]
        var start = generatePoisson(fr)
        while (start < tacts) {
            val wcet = wcetRange.random()
            val task = TaskDescriptor(
                task = Task(frequency = fr, wcet = wcet, deadline = wcet * ((0.1 + Random.nextDouble()) * 10).toInt()),
                start = start.toInt(),
            )
            tasks.add(task)
            start += generatePoisson(fr)
        }
    }

    return tasks.sortedBy { it.start }
}

fun generatePoisson(intensity: Double) = (-1.0 / intensity) * ln(Random.nextDouble())
