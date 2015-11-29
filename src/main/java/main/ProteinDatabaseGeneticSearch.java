package main;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import main.geneticAlgorithm.FitnessCalculation;
import main.geneticAlgorithm.Protein;
import main.geneticAlgorithm.ProteinDatabase;
import main.geneticAlgorithm.ProteinMutator;
import main.proteins.ExperimentalSpectrum;
import main.proteins.TheoreticalSpectrum;

public class ProteinDatabaseGeneticSearch {
	private List<Protein> population;
	private List<Protein> survivors;
	private int populationSize;
	private int populationActualSize;
	private int maxGeneration;
	private int maxMutationTimes = 5;
	private ExperimentalSpectrum es;

	private double suvivorRatio = 0.5;
	private int minSuvivorNum = 20;

	private long initFitnessComputingTimeout = 120; //seconds
	private long fitnessComputingTimeout = 60; //seconds
	private long mutationTimeout = 60; //seconds

	private int fitnessComputingConcurrencyDegree = 10;
	private int mutationConcurrencyDegree = 10;
	private double parentMassThreshold = 200.0;
	private double parentMass;

	class ProteinFitnessComparator implements Comparator<Protein> {
		public int compare(Protein e1, Protein e2) {
			return -Double.compare(e1.getFitness(), e2.getFitness());
		}
	}

	public ProteinDatabaseGeneticSearch() throws InterruptedException, TimeoutException {
		populationSize = 1000;
		maxGeneration = 500;

		es = new ExperimentalSpectrum("test.spectra2");
		es.setIntensityThreshold(100);
		parentMass = es.getParentMass();

		population = new ArrayList<Protein>(populationSize);

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

		List<Protein> candidates = new ArrayList<Protein>();
		for (Protein p : database)
		{
			TheoreticalSpectrum theoreticalSpectrum = new TheoreticalSpectrum(p.getAminoAcidsequence());
			if (Math.abs(parentMass - theoreticalSpectrum.getParentMass()) <= parentMassThreshold) {
				candidates.add(p);
			}
		}

		runInitialFitnessCalculations(candidates);

		Collections.sort(candidates, new ProteinFitnessComparator());
		debug(candidates, 0);


		population = new ArrayList<Protein>(populationSize);

		populationActualSize = 0;
		for (Protein p : candidates)
		{
			population.add(p);
			populationActualSize++;
			if (populationActualSize == populationSize)
			{
				break;
			}
		}
	}

	private void runFitnessCalculations(List<Protein> proteins) throws InterruptedException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(fitnessComputingConcurrencyDegree);
		for (Protein protein : proteins) {
			executor.submit(new FitnessCalculation(protein));
		}

		executor.shutdown();

		boolean timeout = !executor.awaitTermination(fitnessComputingTimeout, TimeUnit.SECONDS);

		/*
		if (!timeout)
		{
			System.out.println("runFitnessCalculations Done!");
		}
		else
		{
			throw new TimeoutException("runFitnessCalculations is timeout");
		}*/
	}

	private void runInitialFitnessCalculations(List<Protein> proteins) throws InterruptedException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(fitnessComputingConcurrencyDegree);
		for (Protein protein : proteins) {
			executor.submit(new FitnessCalculation(protein));
		}

		executor.shutdown();

		boolean timeout = !executor.awaitTermination(initFitnessComputingTimeout, TimeUnit.SECONDS);

		/*
		if (!timeout)
		{
			System.out.println("runInitialFitnessCalculations Done!");
		}
		else
		{
			throw new TimeoutException("runInitialFitnessCalculations is timeout");
		}*/
	}

	private void runMutation(List<Protein> proteins) throws InterruptedException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(mutationConcurrencyDegree);
		for (Protein protein : proteins) {
			executor.submit(new ProteinMutator(protein));
		}

		executor.shutdown();

		boolean timeout = !executor.awaitTermination(mutationTimeout, TimeUnit.SECONDS);

		/*
		if (!timeout)
		{
			System.out.println("Done!");
		}
		else
		{
			throw new TimeoutException("runMutation is timeout");
		}
		*/
	}

	public void findProteins() throws InterruptedException, TimeoutException  {

		for (int generation = 0; generation < maxGeneration; generation++) {
			if (generation % 50 == 0) {
				System.out.println("******* Generation: " + generation
						+ "************");
			}
			runFitnessCalculations(population);

			//debugPopulation();
			survivors = cullPopulation(population);
			//debugSurvivors();
			population = breed(survivors);
		}
		runFitnessCalculations(population);
		survivors = cullPopulation(population);
		debug(survivors, 50);
	}

	private void debug(List<Protein> proteins, int topK)
	{
		int i = 0;
		for (Protein p : proteins) {
			i++;
			if (topK == 0 || i <= topK)
			{
				System.out.println(i + ": Member: "
						+ p.getAminoAcidsequence() + " score: "
						+ p.getFitness() + " mutation times: " + p.getMutationTimes());
			}

		}
	}
	private void debugPopulation() {

		debug(population, 0);
	}

	private void debugSurvivors() {
		debug(survivors, 10);
	}

	private List<Protein> breed(List<Protein> survivors) throws TimeoutException, InterruptedException {
		population = new ArrayList<Protein>(populationSize);

		int survivorsCnt = survivors.size();
		for (int i = 0; i < populationSize; i++)
		{
			population.add(survivors.get(i % survivorsCnt).clone());
		}

		runMutation(population);

		return population;
	}

	private List<Protein> cullPopulation(List<Protein> pop) {

		int suvivorNum = Math.max((int) (populationSize * suvivorRatio), minSuvivorNum);

		List<Protein> tmpSuvivors = new ArrayList<Protein>(suvivorNum);
		int totalNum = pop.size();
		if (survivors != null)
		{
			totalNum += survivors.size();
		}

		Set<String> existed = new HashSet<String>(totalNum);
		List<Protein> cands = new ArrayList<Protein>(totalNum);

		for (Protein p : pop) {
			if (!existed.contains(p.getAminoAcidsequence()) && p.getMutationTimes() <= maxMutationTimes ) {
				cands.add(p);
				existed.add(p.getAminoAcidsequence());
			}
		}

		if (survivors != null) {
			for (Protein p : survivors) {
				if (!existed.contains(p.getAminoAcidsequence())) {
					cands.add(p);
					existed.add(p.getAminoAcidsequence());
				}
			}
		}

		Collections.sort(cands, new ProteinFitnessComparator());

		suvivorNum = Math.min(suvivorNum, cands.size());

		for (int i = 0; i < suvivorNum; i++)
		{
			tmpSuvivors.add(cands.get(i));
		}

		return tmpSuvivors;
	}

	public static void main(String[] args) throws InterruptedException, TimeoutException {
		// FIXME create a parser for experimental spectrums that returns
		// double[] = {peak1 mass, peak2 mass, peak3 mass, ...}

		ProteinDatabaseGeneticSearch gmf = new ProteinDatabaseGeneticSearch();
		gmf.findProteins();


	}

}
