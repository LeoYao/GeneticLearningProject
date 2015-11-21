package main.geneticAlgorithm;

import main.proteins.ExperimentalSpectrum;
import main.proteins.TheoreticalSpectrum;

public class Protein {
	private int mutationTimes; // num mutations
	private String aminoAcidsequence;
	private Protein origin;
	private ExperimentalSpectrum experimental;
	private int latestMutatePos;
	private double fitness;

	public Protein(String seq) {
		setMutationTimes(0);
		this.aminoAcidsequence = seq;
		latestMutatePos = -1;
	}

	public Protein(String seq, int mutationTimes, int latestMutatePos, Protein origin) {
		setMutationTimes(mutationTimes);
		this.aminoAcidsequence = seq;
		this.latestMutatePos = latestMutatePos;
		this.origin = origin;
	}


	public String getAminoAcidsequence() {
		return aminoAcidsequence;
	}

	public void setAminoAcidsequence(String seq) {
		this.aminoAcidsequence = seq;
	}

	public int getMutationTimes() {
		return mutationTimes;
	}

	public void setMutationTimes(int mutationTimes) {
		this.mutationTimes = mutationTimes;
	}

	public int getLatestMutatePos() {
		return latestMutatePos;
	}

	public void setLatestMutatePos(int pos) {
		 latestMutatePos = pos;
	}

	public void append(String append) {
		this.aminoAcidsequence += append;
	}

	public Protein clone() {
		Protein p = new Protein(aminoAcidsequence, mutationTimes, latestMutatePos, origin != null ? origin : this);
		p.setExperimental(this.experimental);
		return p;
	}

	public void setFitness(double f) {
		fitness = f;
	}

	public double getFitness() {
		return fitness;
	}

	public Protein getOrigin() {
		return origin;
	}

	public void setOrigin(Protein origin) {
		this.origin = origin;
	}

	public ExperimentalSpectrum getExperimental() {
		return experimental;
	}

	public void setExperimental(ExperimentalSpectrum experimental) {
		this.experimental = experimental;
	}

}
