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
    fun chooseTask(tasks: List<TaskDescriptor>): TaskDescriptor?
}

class FIFO: SchedulerAlgorithm {
    override val name = "FIFO"
    override fun chooseTask(tasks: List<TaskDescriptor>) = tasks.firstOrNull()
}

class RM: SchedulerAlgorithm {
    override val name = "RM"
    override fun chooseTask(tasks: List<TaskDescriptor>) = tasks.maxByOrNull { it.task.frequency }
}

class EDF: SchedulerAlgorithm {
    override val name = "EDF"
    override fun chooseTask(tasks: List<TaskDescriptor>) = tasks.minByOrNull { it.task.deadline }
}

class Scheduler(
    private val schedulerAlgorithm: SchedulerAlgorithm,
    private val quantum: Int,
    private val tacts: Int,
    private val tasks: MutableList<TaskDescriptor>,
    private val switchTime: Int = 0,
) {
    var time = 0
    private val result = mutableListOf<TaskDescriptor>()

    fun start(): List<TaskDescriptor> {
        var currentTask: TaskDescriptor? = null

        while(tasks.isNotEmpty() && time < tacts) {
            if (time % quantum == 0) {
                tasks.dropMissed()
                val available = tasks.takeActual()
                val task = schedulerAlgorithm.chooseTask(available)
                if (currentTask != task && task != null) time += switchTime
                currentTask = task
            }

            currentTask = currentTask.operate()
            time++
        }

        result.addAll(tasks)
        return result
    }

    private fun List<TaskDescriptor>.takeActual() = filter { it.start <= time }
    private fun List<TaskDescriptor>.dropMissed() = filter { !it.canBeDone(time) }.forEach { it.drop() }
    private fun TaskDescriptor.drop() {
        tasks.remove(this)
        val waitingTime = time - start - progress
        result.add(this.also { it.waitingTime = waitingTime })
    }

    private fun TaskDescriptor?.operate() = this?.let {
        it.progress++

        if (isMissed(time) || isDone()) {
            it.drop()
            return@let null
        }

        it
    }
}
