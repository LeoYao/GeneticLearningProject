package main.qlearning;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.proteins.ExperimentalSpectrum;

public class ProteinDatabaseSearch {
	private ProteinQLearner[] topProteinsInDatabase;
	private int numLearners;
	private int maxIterations;
	private ExperimentalSpectrum es;

	class ProteinFitnessComparator implements Comparator<ProteinQLearner> {

		public int compare(ProteinQLearner e1, ProteinQLearner e2) {
			return Double.compare(e1.getFitness(), e2.getFitness());
		}
	}

	public ProteinDatabaseSearch() {
		numLearners = 50;
		maxIterations = 1000;

		es = new ExperimentalSpectrum("test.spectra");
		es.setIntensityThreshold(10);

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
		for (ProteinQLearner data : database) {
			data.setExperimental(es);
			data.scoreSequence();
		}
		TreeSet<ProteinQLearner> initialSet = new TreeSet<ProteinQLearner>(
				new ProteinFitnessComparator());
//		runFitnessCalculations(database.toArray(new ProteinQLearner[] {}));

		for (ProteinQLearner data : database) {
			initialSet.add(data);
		}

		topProteinsInDatabase = new ProteinQLearner[numLearners];
		Iterator<ProteinQLearner> it = initialSet.descendingIterator();
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