package main.geneticAlgorithm;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import main.proteins.ExperimentalSpectrum;

public class ProteinDatabaseSearch {
	private Protein[] population;
	private int populationSize;
	private int maxGeneration;
	private ExperimentalSpectrum es;

	private double suvivorRatio = 0.1;
	private int minSuvivorNum = 200;

	private long initFitnessComputingTimeout = 120; //seconds
	private long fitnessComputingTimeout = 60; //seconds
	private long mutationTimeout = 60; //seconds

	private int fitnessComputingConcurrencyDegree = 10;
	private int mutationConcurrencyDegree = 10;

	class ProteinFitnessComparator implements Comparator<Protein> {
		public int compare(Protein e1, Protein e2) {
			return -Double.compare(e1.getFitness(), e2.getFitness());
		}
	}

	public ProteinDatabaseSearch() throws InterruptedException, TimeoutException {
		populationSize = 500;
		maxGeneration = 10;

		es = new ExperimentalSpectrum("test.spectra");
		es.setIntensityThreshold(10);
		population = new Protein[populationSize];

		createInitialPopulation();
	}

	private void createInitialPopulation() throws InterruptedException, TimeoutException {
		// FIXME set initial population with proteins from the database
		// possibly use TheoreticalSpectrum.getAvgMass?

		ProteinDatabase ident = new ProteinDatabase();
		ident.parseInput("proteins.fasta");
		List<Protein> database = ident.getDatabase();

		for (Protein data : database) {
			data.setExperimental(es);
		}

		runInitialFitnessCalculations(database);

		Collections.sort(database, new ProteinFitnessComparator());

		population = new Protein[populationSize];

		for (int i = 0; i < populationSize; i++)
		{
			population[i] = database.get(i);
		}
	}

	private void runFitnessCalculations(Protein[] proteins) throws InterruptedException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(fitnessComputingConcurrencyDegree);
		for (int i = 0; i < proteins.length; i++) {
			if (proteins[i] != null) {
				executor.submit(new FitnessCalculation(proteins[i]));
			} else {
				System.out.println("WTF? " + i);
			}
		}

		executor.shutdown();

		boolean timeout = !executor.awaitTermination(fitnessComputingTimeout, TimeUnit.SECONDS);

		if (!timeout)
		{
			System.out.println("Done!");
		}
		else
		{
			throw new TimeoutException("runFitnessCalculations is timeout");
		}
	}

	private void runInitialFitnessCalculations(List<Protein> proteins) throws InterruptedException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(fitnessComputingConcurrencyDegree);
		for (Protein protein : proteins) {
			executor.submit(new FitnessCalculation(protein));
		}

		executor.shutdown();

		boolean timeout = !executor.awaitTermination(initFitnessComputingTimeout, TimeUnit.SECONDS);

		if (!timeout)
		{
			System.out.println("Done!");
		}
		else
		{
			throw new TimeoutException("runFitnessCalculations is timeout");
		}
	}

	private void runMutation(Protein[] proteins) throws InterruptedException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(mutationConcurrencyDegree);
		for (int i = 0; i < proteins.length; i++) {
			if (proteins[i] != null) {
				executor.submit(new ProteinMutator(proteins[i]));
			} else {
				System.out.println("WTF? " + i);
			}
		}

		executor.shutdown();

		boolean timeout = !executor.awaitTermination(mutationTimeout, TimeUnit.SECONDS);

		if (!timeout)
		{
			System.out.println("Done!");
		}
		else
		{
			throw new TimeoutException("runMutation is timeout");
		}
	}

	public void findProteins() throws InterruptedException, TimeoutException  {

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

	private Protein[] breed(Protein[] survivors) throws TimeoutException, InterruptedException {
		int survivorsCnt = survivors.length;
		for (int i = 0; i < populationSize; i++)
		{
			population[i] = survivors[i % survivorsCnt];
		}

		runMutation(population);

		return null;
	}

	private Protein[] cullPopulation(Protein[] pop) {

		int suvivorNum = Math.max((int) (populationSize * suvivorRatio), minSuvivorNum);

		Protein[] suvivors = new Protein[suvivorNum];

		Arrays.sort(pop, new ProteinFitnessComparator());
		for (int i = 0; i < suvivorNum; i++)
		{
			suvivors[i] = pop[i];
		}

		return suvivors;
	}

	public static void main(String[] args) throws InterruptedException, TimeoutException {
		// FIXME create a parser for experimental spectrums that returns
		// double[] = {peak1 mass, peak2 mass, peak3 mass, ...}

		ProteinDatabaseSearch gmf = new ProteinDatabaseSearch();
		gmf.findProteins();
	}

}
