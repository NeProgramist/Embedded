package ua.kpi.comsys.lab3_3

import kotlin.math.abs

data class Chromosome(val a: Int, val b: Int, val c: Int, val d: Int) {
    fun toMutableList() = mutableListOf(a, b, c, d)

    fun mutate() : Chromosome{
        val random = (0..100).random()
        return when {
            random < 5 -> this.copy(d = d + 1)
            random < 10 -> this.copy(d = d - 1)
            random > 95 -> this.copy(c = c + 1)
            random > 90 -> this.copy(c = c - 1)
            else -> this
        }
    }
}

fun List<Int>.toChromosome(): Chromosome {
    require(size > 3)
    return Chromosome(get(0), get(1), get(2), get(3))
}

class GenAlgorithm(
    private val func: (Int, Int, Int, Int) -> Int,
    private val y: Int,
    private val genesDiapason: List<Int>,
) {
    private val accuracy = 1000
    private fun fitness(res: Int) = abs(res - y)

    private fun generatePrimaryPopulation() = List(4) {
        Chromosome(
            genesDiapason.random(),
            genesDiapason.random(),
            genesDiapason.random(),
            genesDiapason.random(),
        )
    }

    private fun getRandomIndex(chances: List<Double>): Int {
        val random = (0..accuracy).random() / accuracy.toDouble()

        var chosen = chances.lastIndex - 1
        for (i in 0 until chances.size - 1) {
            if (random in chances[i]..chances[i + 1]) {
                chosen = i
                break
            }
        }
        return chosen
    }

    private fun step(population: List<Chromosome>, fitness: List<Int>): List<Chromosome> {
        val param = fitness.sumByDouble { 1.0 / it }
        val chances = fitness.map { (1.0 / it) / param }
        val periods = chances.fold(mutableListOf(0.0)) { s, e -> s.apply { add(e + s.last()) } }

        val pairs = List(population.size / 2) {
            val index1 = getRandomIndex(periods)
            val index2 = getRandomIndex(periods)
            population[index1] to population[index2]
        }

        pairs.forEach { it.swap(2) }
        return pairs.flatMap { listOf(it.first.mutate(), it.second.mutate()) }
    }

    fun launch(): Triple<List<Chromosome>, List<Int>, Int> {
        var population = generatePrimaryPopulation()
        var fitness = population.map { fitness(func(it.a, it.b, it.c, it.d)) }
        var countSteps = 1

        while (!fitness.any { it == 0 }) {
            population = step(population, fitness)
            fitness = population.map { fitness(func(it.a, it.b, it.c, it.d)) }
            countSteps++
        }

        return Triple(population, fitness, countSteps)
    }

    private fun Pair<Chromosome, Chromosome>.swap(
        startIndex: Int = 0,
        endIndex: Int = 3,
    ): Pair<Chromosome, Chromosome> {
        val first = this.first.toMutableList()
        val second = this.second.toMutableList()

        for (i in startIndex..endIndex) {
            val copy = first[i]
            first[i] = second[i]
            second[i] = copy
        }

        return first.toChromosome() to second.toChromosome()
    }
}