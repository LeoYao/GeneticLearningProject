package main.qlearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.proteins.ExperimentalSpectrum;
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
		this.maxIterations = maxIterations;
		setK(k);
		this.aminoAcidsequence = seq;
		this.originalSequence = seq;
		transitions = new HashMap<String, List<Transition>>();
		transitions.put(seq, new ArrayList<Transition>());
	}

	public ProteinQLearner() {
		k = 0;
		aminoAcidsequence = "";
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
		for (int i = 0; i < maxIterations; i++) {
			aminoAcidsequence = originalSequence;
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
		//fixme, randomly do something worse than the best
		List<Transition> transactionsFromThisState = transitions
				.get(aminoAcidsequence);
		if(transactionsFromThisState == null){
			List<Transition> newList = new ArrayList<Transition>();
			transitions.put(aminoAcidsequence, newList);
			int mutationPos;
			char mutation;
			double expectedReward;
			boolean terminal;
			Transition t = new Transition(mutationPos, mutation, expectedReward, terminal);
			return t;
		} else {
		Transition best = transactionsFromThisState.get(0);
		double bestScore = best.getExpectedReward();
		for(Transition t : transactionsFromThisState){
			if(t.getExpectedReward() > bestScore){
				best = t;
				bestScore = t.getExpectedReward();
			}
		}
		return best;
		}
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

}
