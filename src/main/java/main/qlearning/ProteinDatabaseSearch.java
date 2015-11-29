package main.qlearning;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.geneticAlgorithm.Protein;
import main.proteins.ExperimentalSpectrum;
import main.proteins.TheoreticalSpectrum;

public class ProteinDatabaseSearch {
	private ProteinQLearner[] topProteinsInDatabase;
	private int numLearners;
	private int maxIterations;
	private ExperimentalSpectrum es;
	private double parentMass;
	private double parentMassThreshold;

	class ProteinFitnessComparator implements Comparator<ProteinQLearner> {

		public int compare(ProteinQLearner e1, ProteinQLearner e2) {
			return -Double.compare(e1.getFitness(), e2.getFitness());
		}
	}

	public ProteinDatabaseSearch() {
		numLearners = 50;
		// numLearners = 3;
		maxIterations = 1000;
		parentMassThreshold = 500.0;

		es = new ExperimentalSpectrum("test.spectra2");
		es.setIntensityThreshold(100);
		parentMass = es.getParentMass();

		initialSearch();
	}

	/**
	 * Get some results from the database that score fairly high without
	 * mutations.
	 */
	private void initialSearch() {
		ProteinDatabase ident = new ProteinDatabase();
		ident.parseInput("proteins.fasta");
		List<ProteinQLearner> database = ident.getDatabase();

		List<ProteinQLearner> candidates = new ArrayList<ProteinQLearner>();

		for (ProteinQLearner data : database) {
			data.setOriginalSequence(data.getAminoAcidsequence());
			
			if (Math.abs(parentMass - data.getParentMass()) <= parentMassThreshold) {	
				data.setExperimental(es);	
				data.scoreSequence();
				data.setK(5);
				data.setMaxIterations(maxIterations);

				candidates.add(data);
			}
		}

		Collections.sort(candidates, new ProteinFitnessComparator());

		topProteinsInDatabase = new ProteinQLearner[numLearners];
		Iterator<ProteinQLearner> it = candidates.iterator();
		int onProtein = 0;
		while (it.hasNext() && onProtein < numLearners) {
			ProteinQLearner learner = it.next();
			topProteinsInDatabase[onProtein] = learner;
			onProtein++;
		}
	}

	private void runFitnessCalculations(ProteinQLearner[] proteins) {
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
		runFitnessCalculations(topProteinsInDatabase);
	}

	private void debugPopulation() {
		// XXX for debugging only
		for (int i = 0; i < numLearners; i++) {
			System.out.println("Member: "
					+ topProteinsInDatabase[i].getAminoAcidsequence()
					+ " score: " + topProteinsInDatabase[i].getFitness());
		}
	}

	public static void main(String[] args) {
		// FIXME create a parser for experimental spectrums that returns
		// double[] = {peak1 mass, peak2 mass, peak3 mass, ...}

		ProteinDatabaseSearch gmf = new ProteinDatabaseSearch();
		gmf.findProteins();
	}

}
