package main.qlearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.proteins.ExperimentalSpectrum;
import main.proteins.RandomAminoAcidGenerator;
import main.proteins.RandomNumberGenerator;
import main.proteins.TheoreticalSpectrum;

public class ProteinQLearner implements Runnable {
	private int k; // num mutations
	private String aminoAcidsequence;
	private String originalSequence;
	private Map<String, List<Transition>> transitions;
	private Transition previousTransition;
	private ExperimentalSpectrum experimental;
	private double fitness;
	private int maxIterations;

	private double discountFactor = 0.9;
	private double learningRate = 0.9;

	public ProteinQLearner(String seq, int k, int maxIterations) {
		this.setMaxIterations(maxIterations);
		setK(k);
		this.aminoAcidsequence = seq;
		this.setOriginalSequence(seq);
		transitions = new HashMap<String, List<Transition>>();
		transitions.put(seq, new ArrayList<Transition>());
	}

	public ProteinQLearner() {
		k = 0;
		aminoAcidsequence = "";
		transitions = new HashMap<String, List<Transition>>();
	}

	public String getAminoAcidsequence() {
		return aminoAcidsequence;
	}

	public void setAminoAcidsequence(String seq) {
		this.aminoAcidsequence = seq;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public void append(String append) {
		this.aminoAcidsequence += append;
	}

	public void setFitness(double f) {
		fitness = f;
	}

	public double getFitness() {
		return fitness;
	}

	public void run() {
		for (int i = 0; i < getMaxIterations(); i++) {
			aminoAcidsequence = getOriginalSequence();
			for (int j = 0; j < k; j++) {
				takeAction();
			}
		}
	}

	public void takeAction() {
		Transition decision = actionPolicy();

		if (!decision.isTerminal()) {

		} else {
			// this is a terminal state
			scoreSequence();
		}
	}

	private Transition actionPolicy() {
		// fixme, randomly do something worse than the best
		if (!transitions.containsKey(aminoAcidsequence)) {
			List<Transition> newList = new ArrayList<Transition>();
			transitions.put(aminoAcidsequence, newList);
			Transition t = generateRandomTransition(newList);
			return t;
		} else {
			List<Transition> transactionsFromThisState = transitions
					.get(aminoAcidsequence);
			if (transactionsFromThisState.size() > 0) {
				Transition best = transactionsFromThisState.get(0);
				double bestScore = best.getExpectedReward();
				for (Transition t : transactionsFromThisState) {
					if (t.getExpectedReward() > bestScore) {
						best = t;
						bestScore = t.getExpectedReward();
					}
				}
				return best;
			} else {
				Transition t = generateRandomTransition(transactionsFromThisState);
				return t;
			}
		}
	}

	private Transition generateRandomTransition(List<Transition> newList) {
		int mutationPos = RandomNumberGenerator.generate(0,
				aminoAcidsequence.length());
		char mutation = RandomAminoAcidGenerator.generate();
		double expectedReward = 0.0;
		int shouldTerm = RandomNumberGenerator.generate(0, 1);
		boolean terminal = (shouldTerm == 1) ? true : false;
		Transition t = new Transition(mutationPos, mutation, expectedReward,
				terminal);
		newList.add(t);
		return t;
	}

	public void scoreSequence() {
		try {
			TheoreticalSpectrum ts = new TheoreticalSpectrum(aminoAcidsequence);
			ts.calculate();
			fitness = ts.scoreAllPeaks(experimental.getMass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ExperimentalSpectrum getExperimental() {
		return experimental;
	}

	public void setExperimental(ExperimentalSpectrum experimental) {
		this.experimental = experimental;
	}

	public String toString() {
		return String.valueOf(fitness) + ": " + aminoAcidsequence;
	}

	public Transition getPreviousTransition() {
		return previousTransition;
	}

	public void setPreviousTransition(Transition previousTransition) {
		this.previousTransition = previousTransition;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public String getOriginalSequence() {
		return originalSequence;
	}

	public void setOriginalSequence(String originalSequence) {
		this.originalSequence = originalSequence;
		transitions.put(originalSequence, new ArrayList<Transition>());
	}

}
