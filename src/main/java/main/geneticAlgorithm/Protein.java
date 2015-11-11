package main.geneticAlgorithm;

public class Protein implements Runnable {
	private int k; // num mutations
	private String aminoAcidsequence;
	private Protein original;
	private ExperimentalSpectrum experimental;
	private int latestMutatePos;
	private double fitness;

	public Protein(String seq) {
		setK(0);
		this.aminoAcidsequence = seq;
		latestMutatePos = -1;
	}

	public Protein(String seq, int k, int mutatePos) {
		setK(k);
		this.aminoAcidsequence = seq;
		this.latestMutatePos = mutatePos;
	}

	public Protein() {
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

	public int getLatestMutatePos() {
		return latestMutatePos;
	}

	public void append(String append) {
		this.aminoAcidsequence += append;
	}

	public Protein clone() {
		return new Protein(aminoAcidsequence, k, latestMutatePos);
	}

	public void setFitness(double f) {
		fitness = f;
	}

	public double getFitness() {
		return fitness;
	}

	public void run() {
		try {
			TheoreticalSpectrum ts = new TheoreticalSpectrum(aminoAcidsequence);
			ts.calculate();
			fitness = ts.scoreAllPeaks(experimental.getMass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mutate() {
		// FIXME create a mutation method
	}

	public Protein getOriginal() {
		return original;
	}

	public void setOriginal(Protein original) {
		this.original = original;
	}

	public ExperimentalSpectrum getExperimental() {
		return experimental;
	}

	public void setExperimental(ExperimentalSpectrum experimental) {
		this.experimental = experimental;
	}

}
