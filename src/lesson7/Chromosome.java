package lesson7;

import lesson6.knapsack.Item;

import java.util.List;
import java.util.Random;

public class Chromosome {

    private String genome = "";
    private int fitness;

    public String getGenome() {
        return genome;
    }

    public void setGenome(String genome) {
        this.genome = genome;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public static void createChromosome(Chromosome chromosome, int genesCount) {
        Random random = new Random();
        StringBuilder resultGenome = new StringBuilder();

        for (int i = 0; i < genesCount; i++) {
            resultGenome.append(' ');
            resultGenome.setCharAt(i, random.nextInt(2) == 0 ? '0' : '1');
        }
        chromosome.setGenome(resultGenome.toString());
    }

    public Item getFilledKnapsack(List<Item> items) {
        int costSum = 0;
        int weightSum = 0;
        for (int i = 0; i < items.size(); i++) {
            if (this.genome.charAt(i) == '1') {
                costSum += items.get(i).getCost();
                weightSum += items.get(i).getWeight();
            }
        }
        return new Item(costSum, weightSum);
    }

    public void calculateFitness(List<Item> items, int load) {
        Item filledKnapsack = getFilledKnapsack(items);
        if (filledKnapsack.getWeight() > load) {
            this.setFitness(0);
            return;
        }
        this.setFitness(filledKnapsack.getCost());
    }

    @Override
    protected Object clone() {
        Chromosome resultChromosome = new Chromosome();
        resultChromosome.setFitness(this.getFitness());
        resultChromosome.setGenome(genome);
        return resultChromosome;
    }

    public Chromosome mutation(int genesCount, int generation) {
        Chromosome result = (Chromosome) this.clone();
        StringBuilder resultGenome = new StringBuilder(genome);

        for (int i = 0; i < genesCount; i += generation) {
            double randomPercent = Math.random() * 100;
            if (randomPercent < 5) {
                char oldValue = genome.charAt(i);
                char newValue = oldValue == '1' ? '0' : '1';
                resultGenome.setCharAt(i, newValue);
            }
        }
        result.setGenome(resultGenome.toString());
        return result;
    }

    public String toString() {
        return "Genome: " + genome + "\n" +
                "Fitness: " + fitness + "\n";
    }

    public Chromosome crossover(Chromosome chromosome, int genesCount) {
        Chromosome result = new Chromosome();
        Random random = new Random();
        StringBuilder resultGenome = new StringBuilder();

        for (int i = 0; i < genesCount; i++) {
            resultGenome.append(' ');
            int xor = this.getGenome().charAt(i) ^ chromosome.getGenome().charAt(i);
            if (xor == 0) {
                resultGenome.setCharAt(i, this.getGenome().charAt(i));
            } else {
                resultGenome.setCharAt(i, (char) (random.nextInt(2) + '0'));
            }
        }
        result.setGenome(resultGenome.toString());
        return result;
    }
}