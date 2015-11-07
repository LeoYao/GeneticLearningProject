package main;

public class Protein {
	private int k; //num mutations
	private String aminoAcidsequence;
	private int latestMutatePos;
	private double fitness;
	private String ancestorSequence;

	public Protein(String seq){
		setK(0);
		this.aminoAcidsequence = seq;
		latestMutatePos = -1;
		ancestorSequence = seq;
	}

	public Protein(String seq, String ancestorSequence, int k, int mutatePos){
		setK(k);
		this.aminoAcidsequence = seq;
		this.latestMutatePos = mutatePos;
		this.ancestorSequence = ancestorSequence;
	}

	public Protein(){
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

	public int getLatestMutatePos()
	{
		return latestMutatePos;
	}
	
	public void append(String append){
		this.aminoAcidsequence += append;
	}

	public Protein clone()
	{
		return new Protein(aminoAcidsequence, ancestorSequence, k, latestMutatePos);
	}

	public void setFitness(double f)
	{
		fitness = f;
	}

	public double getFitness()
	{
		return fitness;
	}

	public String getAncestorSequence()
	{
		return ancestorSequence;
	}

}
