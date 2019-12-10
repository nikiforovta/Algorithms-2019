package lesson7;

import kotlin.NotImplementedError;
import lesson5.Graph;
import lesson5.Path;
import lesson6.knapsack.Fill;
import lesson6.knapsack.Item;

import java.util.List;
import java.util.Random;

// Примечание: в этом уроке достаточно решить одну задачу
@SuppressWarnings("unused")
public class JavaHeuristicsTasks {

    /**
     * Решить задачу о ранце (см. урок 6) любым эвристическим методом
     * <p>
     * Очень сложная
     * <p>
     * load - общая вместимость ранца, items - список предметов
     * <p>
     * Используйте parameters для передачи дополнительных параметров алгоритма
     * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
     * <p>
     * Time Complexity: O(k * n * m)
     * Memory Complexity: O(n)
     * k - GENERATION
     * n - POPULATION
     * m - items.size
     */

    static final int POPULATION = 10000;
    static final int GENERATIONS = 1500;
    static Chromosome[] population = new Chromosome[POPULATION];

    public static Fill fillKnapsackHeuristics(int load, List<Item> items) {
        int genesCount = items.size();
        for (int i = 0; i < POPULATION; i++) {
            population[i] = new Chromosome();
            Chromosome.createChromosome(population[i], genesCount);
        }
        for (int i = 0; i < GENERATIONS; i++) {
            for (int j = 0; j < POPULATION; j++) {
                population[j].calculateFitness(items, load);
            }
            population = createNextGeneration(genesCount, i + 1);
        }
        if ((population[0].getFitness()) == 0) return new Fill(0);
        return new Fill(population[0].getFilledKnapsack(items));
    }

    private static Chromosome[] createNextGeneration(int genesCount, int generation) {
        Chromosome[] nextGeneration = new Chromosome[POPULATION];
        Random random = new Random();
        nextGeneration[0] = findBestChromosome();
        for (int i = 1; i < POPULATION; i++) {
            Chromosome firstParent = population[random.nextInt(POPULATION)];
            Chromosome secondParent = population[random.nextInt(POPULATION)];
            while (secondParent == firstParent) {
                secondParent = population[random.nextInt(POPULATION)];
            }
            Chromosome offspring = firstParent.crossover(secondParent, genesCount);
            nextGeneration[i] = offspring.mutation(genesCount, generation);
        }
        return nextGeneration;
    }

    private static Chromosome findBestChromosome() {
        int bestFitness = 0;
        Chromosome result = population[0];
        for (int i = 0; i < POPULATION; i++) {
            if (population[i].getFitness() > bestFitness) {
                result = population[i];
                bestFitness = population[i].getFitness();
            }
        }
        return result;
    }

    /**
     * Решить задачу коммивояжёра (см. урок 5) методом колонии муравьёв
     * или любым другим эвристическим методом, кроме генетического и имитации отжига
     * (этими двумя методами задача уже решена в под-пакетах annealing & genetic).
     * <p>
     * Очень сложная
     * <p>
     * Граф передаётся через параметр graph
     * <p>
     * Используйте parameters для передачи дополнительных параметров алгоритма
     * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
     */
    public static Path findVoyagingPathHeuristics(Graph graph, Object... parameters) {
        throw new NotImplementedError();
    }
}
