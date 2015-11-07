package main.java.main.geneticAlgorithm;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProteinDatabaseSearch {
	private Protein[] population;
	private double[] scores;
	private int populationSize;
	private int maxGeneration;

	public ProteinDatabaseSearch() {
		populationSize = 500;
		maxGeneration = 10;
		
		ProteinDatabase ident = new ProteinDatabase();
		ident.parseInput("main/proteins.fasta");
		List<Protein> database = ident.getDatabase();

		// set initial population
		population = new Protein[populationSize];
		scores = new double[populationSize];
		for (int i = 0; i < populationSize; i++) {
			//population[i] = ...
			scores[i] = 0.0;
		}
	}

	public void findProteins() {

		for (int generation = 0; generation < maxGeneration; generation++) {
			System.out.println("******* Generation: " + generation
					+ "************");
			ExecutorService executor = Executors.newFixedThreadPool(10);

			for (int i = 0; i < populationSize; i++) {
				if (population[i].getScore() < generation + 1)
					population[i].mutate();
				executor.submit(population[i]);
			}

			executor.shutdown();
			while (!executor.isTerminated()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			for (int i = 0; i < populationSize; i++) {
				System.out.println("Member: " + population[i].getProteinSequence() + " score: "
						+ population[i].getScore());
			}
		}
	}

	public static void main(String[] args) {
		ProteinDatabaseSearch gmf = new ProteinDatabaseSearch();
		gmf.findProteins();
	}

}
