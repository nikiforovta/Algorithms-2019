package lesson7

import lesson5.Graph
import lesson5.Path
import lesson5.impl.GraphBuilder
import lesson6.knapsack.Fill
import lesson6.knapsack.Item
import lesson6.knapsack.fillKnapsackGreedy
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractHeuristicsTests {

    fun fillKnapsackCompareWithGreedyTest(fillKnapsackHeuristics: (Int, List<Item>) -> Fill) {

        val fewItems = mutableListOf<Item>()
        val random = Random()
        for (j in 0 until 15) {
            fewItems += Item(1 + random.nextInt(10000), 300 + random.nextInt(600))
        }
        val fillFewHeuristics = fillKnapsackHeuristics(1000, fewItems)
        println("Heuristics score = " + fillFewHeuristics.cost)
        val fillFewGreedy = fillKnapsackGreedy(1000, fewItems)
        println("Greedy score = " + fillFewGreedy.cost)
        assertTrue(fillFewHeuristics.cost >= fillFewGreedy.cost)

        val littleItems = mutableListOf<Item>()
        for (j in 0 until 15) {
            littleItems += Item(1 + random.nextInt(1000), 25 + random.nextInt(50))
        }
        try {
            val fillLittleHeuristics = fillKnapsackHeuristics(1000, littleItems)
            println("Heuristics score = " + fillLittleHeuristics.cost)
            val fillLittleGreedy = fillKnapsackGreedy(1000, littleItems)
            println("Greedy score = " + fillLittleGreedy.cost)
            assertTrue(fillLittleHeuristics.cost >= fillLittleGreedy.cost)
        } catch (e: StackOverflowError) {
            println("Greedy failed with Stack Overflow")
        }

        for (i in 0..9) {
            val items = mutableListOf<Item>()
            for (j in 0 until 10000) {
                items += Item(1 + random.nextInt(10000), 300 + random.nextInt(600))
            }
            try {
                val fillHeuristics = fillKnapsackHeuristics(1000, items)
                println("Heuristics score = " + fillHeuristics.cost)
                val fillGreedy = fillKnapsackGreedy(1000, items)
                println("Greedy score = " + fillGreedy.cost)
                assertTrue(fillHeuristics.cost >= fillGreedy.cost)
            } catch (e: StackOverflowError) {
                println("Greedy failed with Stack Overflow")
            }
        }
    }

    fun findVoyagingPathHeuristics(findVoyagingPathHeuristics: Graph.() -> Path) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            addConnection(a, b, 10)
            addConnection(b, c, 15)
            addConnection(c, f, 30)
            addConnection(a, d, 20)
            addConnection(d, e, 25)
            addConnection(e, f, 15)
            addConnection(a, f, 40)
            addConnection(b, d, 10)
            addConnection(c, e, 5)
        }.build()
        val path = graph.findVoyagingPathHeuristics()
        assertEquals(105, path.length)
        val vertices = path.vertices
        assertEquals(vertices.first(), vertices.last(), "Voyaging path $vertices must be loop!")
        val withoutLast = vertices.dropLast(1)
        val expected = listOf(graph["A"], graph["D"], graph["B"], graph["C"], graph["E"], graph["F"])
        assertEquals(expected.size, withoutLast.size, "Voyaging path $vertices must travel through all vertices!")
        expected.forEach {
            assertTrue(it in vertices, "Voyaging path $vertices must travel through all vertices!")
        }
    }

}