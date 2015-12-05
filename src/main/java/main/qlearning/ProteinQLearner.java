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
	private static final double DEFAULT_REWARD = -0.1;
	private int k; // num mutations
	private String aminoAcidsequence;
	private String originalSequence;
	private Map<String, List<Transition>> transitions;
	private Transition previousTransition;
	private ExperimentalSpectrum experimental;
	private TheoreticalSpectrum theoretical;
	private double currentScore;
	private int maxIterations;

	private double discountFactor = 0.9;
	private double learningRate = 0.9;
	private double epsilon = 0.9;

	public ProteinQLearner(String seq, int k, int maxIterations) {
		transitions = new HashMap<String, List<Transition>>();
		this.setMaxIterations(maxIterations);
		setK(k);
		this.aminoAcidsequence = seq;
		this.setOriginalSequence(seq);

		// transitions.put(seq, new ArrayList<Transition>());
		theoretical = new TheoreticalSpectrum(aminoAcidsequence);
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
		currentScore = f;
	}

	public double getFitness() {
		return currentScore;
	}

	public void run() {
		printCurrentBest();
		double epsilonReduction = (epsilon) / (double) getMaxIterations();
		for (int i = 0; i < getMaxIterations(); i++) {
			if ((i % 1000) == 0)
				System.out.println(i + " iterations...");
			aminoAcidsequence = getOriginalSequence();
			previousTransition = null;
			boolean terminated = false;
			for (int j = 0; j < k; j++) {
				takeAction();
				if (previousTransition.isTerminal()) {
					terminated = true;
					break;
				}
			}
			if (!terminated) {
				double score = scoreSequence();
				previousTransition.setExpectedReward(score);
				propogateReward(score + DEFAULT_REWARD);
			}
			epsilon -= epsilonReduction;
		}
	}

	private void printCurrentBest() {
		double firstScore = scoreSequence();
		System.out.println("Protein: " + aminoAcidsequence
				+ " with score: " + firstScore);
	}

	public void runToSolve() {
		aminoAcidsequence = getOriginalSequence();
		boolean terminated = false;
		int onK = 0;
		while (!terminated && onK < k) {
			onK++;
			List<Transition> transactionsFromThisState = transitions
					.get(aminoAcidsequence);
			if (transactionsFromThisState != null
					&& transactionsFromThisState.size() > 0) {
				Transition best = transactionsFromThisState.get(0);
				double bestScore = best.getExpectedReward();
				for (Transition t : transactionsFromThisState) {
					if (t.getExpectedReward() > bestScore) {
						best = t;
						bestScore = t.getExpectedReward();
					}
				}
				applyAction(best);
				if (best.isTerminal())
					terminated = true;
			} else {
				terminated = true;
			}
		}
		printCurrentBest();
	}

	public void takeAction() {
		boolean terminate = false;
		String oldSeq = aminoAcidsequence;
		Transition decision = actionPolicy(epsilon);

		applyAction(decision);

		List<Transition> possibleActions = transitions.get(oldSeq);
		double currentReward = DEFAULT_REWARD;

		if (!decision.isTerminal()) {
			if (possibleActions != null && possibleActions.contains(decision)) {
				currentReward = possibleActions.get(
						possibleActions.indexOf(decision)).getExpectedReward()
						+ DEFAULT_REWARD;
			} else {
				addNewAction(decision, possibleActions);
			}

		} else {
			// this is a terminal state
			double score = scoreSequence();
			if (possibleActions != null && possibleActions.contains(decision)) {
				currentReward = score;
			} else {
				addNewAction(decision, possibleActions);
			}
		}
		propogateReward(currentReward);
		previousTransition = decision;
	}

	private void applyAction(Transition decision) {
		char[] seq = aminoAcidsequence.toCharArray();
		seq[decision.getMutationPostion()] = decision.getMutationChar();
		aminoAcidsequence = new String(seq);
	}

	private void addNewAction(Transition decision,
			List<Transition> possibleActions) {
		if (possibleActions == null) {
			possibleActions = new ArrayList<Transition>();
			transitions.put(aminoAcidsequence, possibleActions);
		}
		possibleActions.add(decision);
	}

	private void propogateReward(double currentReward) {
		if (previousTransition != null) {
			double oldReward = previousTransition.getExpectedReward();
			double newReward = oldReward + (learningRate) * (discountFactor)
					* currentReward;
			previousTransition.setExpectedReward(newReward);
		}
	}

	private Transition actionPolicy(double epsilon) {
		if (!transitions.containsKey(aminoAcidsequence)
				|| transitions.get(aminoAcidsequence).isEmpty()) {
			List<Transition> newList = new ArrayList<Transition>();
			transitions.put(aminoAcidsequence, newList);
			Transition t = generateRandomTransition(newList);
			return t;
		} else {
			double random = Math.random();
			List<Transition> transactionsFromThisState = transitions
					.get(aminoAcidsequence);
			if (random > epsilon) {
				if (transactionsFromThisState.size() > 0) {
					Transition best = transactionsFromThisState.get(0);
					double bestScore = best.getExpectedReward();
					for (Transition t : transactionsFromThisState) {
						if (t.getExpectedReward() > bestScore) {
							best = t;
							bestScore = t.getExpectedReward();
						}
					}
					if (bestScore <= 0.0) {
						Transition t = generateRandomTransition(transactionsFromThisState);
						return t;
					}
					return best;
				} else {
					Transition t = generateRandomTransition(transactionsFromThisState);
					return t;
				}
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

	public double scoreSequence() {
		double score = 0.0;
		try {
			theoretical = new TheoreticalSpectrum(aminoAcidsequence);
			theoretical.calculate();
			// double highestScore = 0.0;
			// for (int i = -150; i < 150; i++) {
			double tempScore = theoretical.scoreAllPeaks(
					experimental.getMass(), 0);
			// if (tempScore > highestScore)
			// highestScore = tempScore;
			// }
			score = tempScore;
		} catch (Exception e) {
			e.printStackTrace();
		}
		currentScore = score;
		return score;
	}

	public ExperimentalSpectrum getExperimental() {
		return experimental;
	}

	public void setExperimental(ExperimentalSpectrum experimental) {
		this.experimental = experimental;
	}

	public String toString() {
		return String.valueOf(currentScore) + ": " + aminoAcidsequence;
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
		List<Transition> actions = new ArrayList<Transition>();
		transitions.put(originalSequence, actions);
	}

	public double getParentMass() {
		theoretical = new TheoreticalSpectrum(aminoAcidsequence);
		return theoretical.getParentMass();
	}

}
