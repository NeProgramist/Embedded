data class Task(
    val frequency: Double,
    val wcet: Int,
    val deadline: Int,
)

data class TaskDescriptor(
    val task: Task,
    val start: Int,
    var progress: Int = 0,
    var waitingTime: Int = 0,
) {
    fun isMissed(time: Int) = time > start + task.deadline
    fun isDone() = progress >= task.wcet
    fun canBeDone(time: Int) = !isMissed(time) && task.wcet - progress < task.deadline + start - time
}

interface SchedulerAlgorithm {
    val name: String
    var currentTask: TaskDescriptor?
    fun chooseTask(tasks: List<TaskDescriptor>): TaskDescriptor?
}

class FIFO: SchedulerAlgorithm {
    override val name = "FIFO"
    override var currentTask: TaskDescriptor? = null
    override fun chooseTask(tasks: List<TaskDescriptor>) = currentTask ?: tasks.firstOrNull()
}

class RM: SchedulerAlgorithm {
    override val name = "RM"
    override var currentTask: TaskDescriptor? = null
    override fun chooseTask(tasks: List<TaskDescriptor>) = tasks.minByOrNull { it.task.frequency }
}

class EDF: SchedulerAlgorithm {
    override val name = "EDF"
    override var currentTask: TaskDescriptor? = null
    override fun chooseTask(tasks: List<TaskDescriptor>) = tasks.minByOrNull { it.task.deadline }
}

class Scheduler(
    private val sa: SchedulerAlgorithm,
    private val quantum: Int,
    private val tacts: Int,
    private val tasks: MutableList<TaskDescriptor>,
    private val switchTime: Int = 0,
) {
    var time = 0
    private val result = mutableListOf<TaskDescriptor>()

    fun start(): List<TaskDescriptor> {
        while(tasks.isNotEmpty() && time < tacts) {
            tasks.dropMissed(sa.currentTask)

            if (time % quantum == 0) {
                val available = tasks.takeActual()
                val task = sa.chooseTask(available)
                if (task != null && sa.currentTask != task) {
                    time += switchTime
                    sa.currentTask = task
                }
            }

            sa.currentTask = sa.currentTask?.operate()
            time++
        }

        tasks.toMutableList().forEach { it.drop() }
        return result
    }

    private fun List<TaskDescriptor>.takeActual() = filter { it.start <= time }
    private fun List<TaskDescriptor>.dropMissed(td: TaskDescriptor?) = filter { !it.canBeDone(time) }.forEach {
        if (td !== it) it.drop()
    }
    private fun TaskDescriptor.drop() {
        tasks.remove(this)
        val waitingTime = time - start - progress
        result.add(this.also { it.waitingTime = waitingTime })
    }

    private fun TaskDescriptor.done() {
        tasks.remove(this)
        val waitingTime = time - start - progress
        if (waitingTime < 0) {
            println()
        }
        result.add(this.also { it.waitingTime = waitingTime })
    }

    private fun TaskDescriptor.operate(): TaskDescriptor? {
        progress++

        if (isMissed(time)) {
            drop()
            return null
        }

        if (isDone()) {
            drop()
            return null
        }

        return this
    }
}

fun main() {
    val tasks = listOf(
        TaskDescriptor(
            Task(0.1, 2, 4),
            start = 0,
        ),
        TaskDescriptor(
            Task(0.1, 2, 3),
            start = 2,
        ),
        TaskDescriptor(
            Task(0.2, 5, 6),
            start = 4,
        ),
        TaskDescriptor(
            Task(0.3, 2, 2),
            start = 6,
        ),
        TaskDescriptor(
            Task(0.1, 3, 4),
            start = 7,
        ),
        TaskDescriptor(
            Task(0.1, 2, 5),
            start = 10,
        ),
        TaskDescriptor(
            Task(0.3, 1, 4),
            start = 10,
        ),
    )

    val scheduler = Scheduler(
        EDF(),
        3,
        100,
        tasks.toMutableList(),
        1,
    )
    val resEdf = scheduler.start()
    println()
}
