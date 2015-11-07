package main;

import java.util.HashSet;
import java.util.List;


public class EvolutionaryAlgorithm {
	//private List<Protein> database;

	private Protein[] survivors;
	private int survivorSize;
	private int actualSurvivorCount;

	private Protein[] generation;
	private int actualDescendantCount;

	//How many children a parent can breed
	private int childrenSize;

	private Protein ancestor;
	private double[] experimentalPeak;

	private List<Protein> database;
	
	public EvolutionaryAlgorithm(Protein ancestor, int survivorSize, int childrenSize, double[] experimentalPeak){
		this.ancestor = ancestor;
		this.childrenSize = childrenSize;
		this.survivorSize = survivorSize;

		survivors = new Protein[survivorSize];
		generation = new Protein[survivorSize * childrenSize + survivorSize]; //the parent will also live in that generation
		this.experimentalPeak = experimentalPeak;
		survivors[0] = ancestor;
		actualSurvivorCount = 1;
//		for(int i = 0; i < populationSize; i++){
//			//population[i] = new Protein(new char[]{'G','S','F','D','A'});
//			population[i] = new Protein(new char[]{'A','S','A','F','A'});
//			fitness[i] = 0.0;
//		}
//		population[0] = new Protein(new char[]{'G','S','F','D','A'});
	}
	
	public static void main(String[] args){

		//ProteinIdentification parser = new ProteinIdentification();
		//List<Protein> proteins = parser.parseInput("main/proteins.fasta");

		Protein ancestor = new Protein("INNVCFPR");
		double[] expPeaks = {114.1668,
				228.2706,
				342.3744,
				441.507,
				780.84,
				928.0166,
				1025.1333
		};

		EvolutionaryAlgorithm alg = new EvolutionaryAlgorithm(ancestor, 10, 20 , expPeaks); //Todo: to materalize

		alg.evolve(0, 0.01f);

		System.out.println("--------------------------------------------------------------------------");
		System.out.println("AminoAcidsequence: Fitness");
		System.out.println("--------------------------------------------------------------------------");
		for(Protein p : alg.getTopK(10))
		{
			System.out.println(p.getAminoAcidsequence() + ": " + p.getFitness());
		}

		//EvolutionaryAlgorithm ident = new EvolutionaryAlgorithm();
		//System.out.println(ident.protMass(new char[]{'G','P','F','N','A'}));
//		SpectralData data = new SpectralData(486.22267);
//		ident.protIdentification(data);
	}

	public void evolve(int maxGenerationNum, float minNewDiffSuvivorRatio)
	{
		for (int i = 0; i < maxGenerationNum; i++)
		{
			getOneGeneration();
			Protein[] newSurvivors = getSurvivors();
			int newDiffSurvivorCount = getNewDiffSurvivorCount(survivors, newSurvivors);
			float newDiffSurvivorRatio = (float) newDiffSurvivorCount / (float) actualSurvivorCount;
			actualSurvivorCount = copySurvivors(newSurvivors);
			if (newDiffSurvivorRatio < minNewDiffSuvivorRatio)
			{
				break;
			}
		}
	}


	private Protein[] getSurvivors()
	{
		int newSurvivorsSize = Math.min(survivorSize, actualDescendantCount);
		Protein[] newSurvivors = new Protein[newSurvivorsSize];
		for (int i = 0; i < newSurvivorsSize; i++)
		{
			newSurvivors[i] = generation[i];
		}

		return newSurvivors;
	}

	private int copySurvivors(Protein[] newSuvivors)
	{
		int len = newSuvivors.length;
		for(int i = 0; i < len; i++)
		{
			survivors[i] = newSuvivors[i];
		}

		return len;
	}

	private int getNewDiffSurvivorCount(Protein[] o, Protein[] n)
	{
		HashSet<String> set = new HashSet<String>(o.length);
		for (Protein p : o)
		{
			set.add(p.getAminoAcidsequence());
		}

		int newSurvivorCount = 0;
		for (Protein p : n)
		{
			if (!set.contains(p.getAminoAcidsequence()))
			{
				newSurvivorCount++;
			}
		}

		return newSurvivorCount;
	}


	private void getOneGeneration()
	{
		int actualDescendantCount = 0;
		for(int i = 0; i < actualSurvivorCount; i++)
		{
			Protein[] tmpChildren = breed(survivors[i]);
			for (int j = 0; j < tmpChildren.length; j++)
			{
				generation[actualDescendantCount++] = tmpChildren[j];
			}
		}

		calcReward();

		for(int i = 0; i < actualSurvivorCount; i++) {
			generation[actualDescendantCount++] = survivors[i];
		}

		sort();
	}

	//Todo
	private void calcReward()
	{

	}

	//Todo
	private void sort()
	{

	}

	public Protein[] getTopK(int k)
	{
		int len = Math.min(k, actualSurvivorCount);
		Protein[] topK = new Protein[len];

		for(int i = 0; i < len; i++)
		{
			topK[i] = survivors[i].clone();
		}

		return topK;
	}

	private Protein[] breed(Protein parent)
	{
		Protein[] tmpChildren = new Protein[childrenSize];
		for (int i = 0; i < childrenSize; i++)
		{
			tmpChildren[i] = ProteinMutator.mutate(parent, ancestor);
		}

		return tmpChildren;
	}


	/*
	public void protIdentification(TheoreticalSpectralPeak data){
		int generations = 10;
		for(int generation = 0; generation < generations; generation++){
			for(int popMember = 0; popMember < populationSize; popMember++){
				calculateFitness(data, popMember);				
			}
			
			double avgFitness = 0.0;
			for(int popMember = 0; popMember < populationSize; popMember++){
				avgFitness += fitness[popMember];
			}
			avgFitness /= ((double)populationSize);
			Protein[] survivors = new Protein[populationSize];
			int onSurvivor = 0;
			for(int popMember = 0; popMember < populationSize; popMember++){
				if(fitness[popMember] > avgFitness){
					survivors[onSurvivor] = population[popMember];
				}
			}
		}
	}

	private void calculateFitness(TheoreticalSpectralPeak data, int popMember) {
//		fitness[popMember] = Math.abs(protMass(population[popMember].get) - data.getAvg());
//		System.out.println("Fitness: " + fitness[popMember]);
	}
	
	public double protMass(char[] protBuff){
		double mass = 0.0;
		for(int i = 0; i < protBuff.length; i++){
			if(protBuff[i] == 'A') mass += 71.03711;
			else if(protBuff[i] == 'C') mass += 103.00919;
			else if(protBuff[i] == 'D') mass += 115.02694;
			else if(protBuff[i] == 'E') mass += 129.04259;
			else if(protBuff[i] == 'F') mass += 147.06841;
			else if(protBuff[i] == 'G') mass += 57.02146;
			else if(protBuff[i] == 'H') mass += 137.05891;
			else if(protBuff[i] == 'I') mass += 113.08406;
			else if(protBuff[i] == 'K') mass += 128.09496;
			else if(protBuff[i] == 'L') mass += 113.08406;
			else if(protBuff[i] == 'M') mass += 131.04049;
			else if(protBuff[i] == 'N') mass += 114.04293;
			else if(protBuff[i] == 'P') mass += 97.05276;
			else if(protBuff[i] == 'Q') mass += 128.05858;
			else if(protBuff[i] == 'R') mass += 156.10111;
			else if(protBuff[i] == 'S') mass += 87.03203;
			else if(protBuff[i] == 'T') mass += 101.04786;
			else if(protBuff[i] == 'V') mass += 99.06841;
			else if(protBuff[i] == 'W') mass += 186.07931;
			else if(protBuff[i] == 'Y') mass += 163.06333;
		}
		return mass;
	}*/

}
