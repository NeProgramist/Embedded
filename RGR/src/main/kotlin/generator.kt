import kotlin.math.exp
import kotlin.random.Random

val tasksSpread = listOf(0.1, 0.2, 0.7)

fun generateTasks(intensity: Double, tacts: Int, wcetRange: IntRange): List<TaskDescriptor> {
    val tasks = mutableListOf<TaskDescriptor>()

    repeat(tasksSpread.size) {
        val fr = intensity * tasksSpread[it]
        var start = generatePoisson(1 / fr)
        val wcet = wcetRange.random()
        val deadline = wcet * ((0.1 + Random.nextDouble()) * 10).toInt()

        while (start + wcet <= tacts) {
            val task = TaskDescriptor(
                task = Task(frequency = fr, wcet = wcet, deadline = deadline),
                start = start,
            )
            tasks.add(task)
            start += generatePoisson(1 / fr)
        }
    }

    return tasks.sortedBy { it.start }
}

fun generatePoisson(lambda: Double): Int {
    val l = exp(-lambda)
    var p = 1.0
    var k = 0

    do {
        k++;
        p *= Random.nextDouble()
    } while (p > l);

    return k - 1
}
