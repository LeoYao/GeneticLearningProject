package main.qlearning;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.geneticAlgorithm.Protein;
import main.proteins.ExperimentalSpectrum;
import main.proteins.TheoreticalSpectrum;

public class ProteinSequenceImprover {
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

	public ProteinSequenceImprover() {
		//numLearners = 50;
		numLearners = 1;
		// numLearners = 3;
		maxIterations = 1000;
		parentMassThreshold = 500.0;

		es = new ExperimentalSpectrum("test.spectra3");
		es.setIntensityThreshold(20);
		parentMass = es.getParentMass();
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

	public void improveProteinSequence() {
		ProteinQLearner pql = new ProteinQLearner("YVVDEPHNLLF", 2, 200000);
//		ProteinQLearner pql = new ProteinQLearner("YVVDEPHNLLF", 3, 100000);
//		ProteinQLearner pql = new ProteinQLearner("YVVDEPHNLLF", 3, 50000);
//		ProteinQLearner pql = new ProteinQLearner("YVVDEPQNLLK", 3, 50000);
		
		pql.setExperimental(es);
		pql.run();
		pql.runToSolve();
		System.out.println(pql.getAminoAcidsequence());
		//runFitnessCalculations(topProteinsInDatabase);
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

		ProteinSequenceImprover gmf = new ProteinSequenceImprover();
		gmf.improveProteinSequence();
	}

}
