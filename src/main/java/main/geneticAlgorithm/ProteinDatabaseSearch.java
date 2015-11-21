package main.geneticAlgorithm;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.proteins.ExperimentalSpectrum;

public class ProteinDatabaseSearch {
	private Protein[] population;
	private double[] scores;
	private int populationSize;
	private int maxGeneration;
	private ExperimentalSpectrum es;

	class ProteinFitnessComparator implements Comparator<Protein> {

		public int compare(Protein e1, Protein e2) {
			return Double.compare(e1.getFitness(), e2.getFitness());
		}
	}

	public ProteinDatabaseSearch() {
		populationSize = 500;
		maxGeneration = 10;

		es = new ExperimentalSpectrum("test.spectra");
		es.setIntensityThreshold(10);

		createInitialPopulation();
	}

	private void createInitialPopulation() {
		// FIXME set initial population with proteins from the database
		// possibly use TheoreticalSpectrum.getAvgMass?

		ProteinDatabase ident = new ProteinDatabase();
		ident.parseInput("proteins.fasta");
		List<Protein> database = ident.getDatabase();
		for (Protein data : database) {
			data.setExperimental(es);
		}
		TreeSet<Protein> initialPop = new TreeSet<Protein>(
				new ProteinFitnessComparator());
		runFitnessCalculations(database.toArray(new Protein[] {}));

		population = new Protein[populationSize];
		scores = new double[populationSize];
		for (int i = 0; i < populationSize; i++) {
			// population[i] = ...
			scores[i] = 0.0;
		}
	}

	private void runFitnessCalculations(Protein[] proteins) {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 0; i < proteins.length; i++) {
			if (proteins[i] != null) {
				executor.submit(proteins[i]);
			} else {
				System.out.println("WTF? " + i);
			}
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Done!");
	}

	public void findProteins() {

		for (int generation = 0; generation < maxGeneration; generation++) {
			System.out.println("******* Generation: " + generation
					+ "************");
			runFitnessCalculations(population);

			debugPopulation();
			Protein[] survivors = cullPopulation(population);
			population = breed(survivors);
		}
	}

	private void debugPopulation() {
		// XXX for debugging only
		for (int i = 0; i < populationSize; i++) {
			System.out.println("Member: "
					+ population[i].getAminoAcidsequence() + " score: "
					+ population[i].getFitness());
		}
	}

	private Protein[] breed(Protein[] survivors) {
		// FIXME create a method to replace culled population members
		// make sure that the population size is still populationSize
		return null;
	}

	private Protein[] cullPopulation(Protein[] pop) {
		// FIXME create a method to get rid of poorly scoring proteins
		return null;
	}

	public static void main(String[] args) {
		// FIXME create a parser for experimental spectrums that returns
		// double[] = {peak1 mass, peak2 mass, peak3 mass, ...}

		ProteinDatabaseSearch gmf = new ProteinDatabaseSearch();
		gmf.findProteins();
	}

}
